package unam.pcic;

import unam.pcic.dominio.*;
import unam.pcic.io.LectorCSV;
import unam.pcic.procesamiento.ProcesadorCSV;
import unam.pcic.utilidades.Opciones;
import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * - Orquesta el flujo completo
 * - Ejecuta la versión secuencial y la concurrente
 * - Invoca el analisis de rendimiento.
 * - Coordina la generación de outputs.
 */
public class ControladorAplicacion {

    /**
     * Ejecuta la aplicación según las configuraciones.
     *
     * @param opciones Configuraciones para ejecutar el programa.
     */
    public static void ejecutar(Opciones opciones) {
        System.out.println("Ejecutando app...");
        // TODO: Inicializar un AnalizadorRendimiento (posible metodo mideTiempo())
        // TODO: medir el tiempo (lo hace el analizador)

        // Ejecutar versión secuencial via ProcesadorCSV
        ProcesadorCSV procesadorSecuencial = FabricaProcesador.crearProcesador(Procesamiento.SECUENCIAL);
        if (procesadorSecuencial == null) {
            System.err.println("Error al crear ProcesadorCSV");
            return;
        }

        procesadorSecuencial.procesa(opciones);

        // TODO: El analizador de redimiento guarda los datos medidos.

        // TODO: einiciar el analizador de rendimiento.

        // Ejecutar versión concurrente via ProcesadorCSV
        ProcesadorCSV procesadorConcurrente = FabricaProcesador.crearProcesador(Procesamiento.CONCURRENTE);
        if (procesadorConcurrente == null) {
            System.err.println("Error al crear ProcesadorCSV");
            return;
        }

        procesadorConcurrente.procesa(opciones);

        // TODO: El analizador de redimiento guarda los datos medidos.
    }

    /**
     * Prueba de lectura y aplicación de filtros. Se quitará para la versión final.
     * @param opciones las opciones de ejecución.
     */
    private static void prueba(Opciones opciones) {
        Almacen<RegistroCSV> almacen;

        try {
            File inputFile = new File(opciones.getArchivo());

            LectorCSV lector = new LectorCSV(inputFile, true);

            String[] encabezados = lector.leerEncabezado();
            System.out.println("Columnas: " + encabezados.length);
            //for (int i = 0; i < encabezados.length; i++) {
            //    System.out.println("  [" + i + "] " + encabezados[i]);
            //}

            if (opciones.getTodasLasColumnas()) {
                List<Integer> columnas = new ArrayList<>();
                for (int i = 0; i < encabezados.length; i++) {
                    columnas.add(i);
                }

                opciones.setColumnas(columnas.stream().mapToInt(Integer::intValue).toArray());
            }

            // Desde aquí la implementación depende de si se usa row-oriented o column-oriented.
            // RegistroCSV para row-oriented
            // ColumnaCSV para column-oriented

            almacen = new AlmacenRenglones(lector.leerTodo());

            System.out.println("Total de registros leídos: " + almacen.getLongitud());

            for (RegistroCSV registro : almacen) {
                if (registro.getNumeroColumnas() != encabezados.length) {
                    System.out.println("No hay 24 columnas en cada registro");
                    System.out.println("R:" + registro);
                }
            }

            int n = opciones.getLimiteImpresion();
            System.out.println("\nPrimeros " + n + " registros:");
            for (int i = 0; i < Math.min(n, almacen.getLongitud()); i++) {
                RegistroCSV registro = almacen.get(i);
                System.out.println("Línea " + registro.getNumeroLinea() + ": " + registro);
            }

            // Seleccion de columnas
            if (opciones.getColumnas() != null) {
                CriterioFiltro<RegistroCSV> criterio = CriterioFiltro.paraRegistroCSV();
                almacen = criterio.seleccionarColumnas(almacen, opciones.getColumnas());

                System.out.println("\nPrimeros " + n + " registros (columnas seleccionadas):");
                for (int i = 0; i < Math.min(n, almacen.getLongitud()); i++) {
                    RegistroCSV registro = almacen.get(i);
                    System.out.println("Línea " + registro.getNumeroLinea() + ": " + registro);
                }
            }

            // Aplicar filtros
            if (opciones.getFiltros() != null) {
                // Hay que hacer un metodo auxiliar para crear las condiciones
                String[] filtros = opciones.getFiltros();
                List<CondicionFiltro> condiciones = new ArrayList<>();
                for (String filtro : filtros) {
                    String columna = filtro.split("=")[0].replace("c", "");
                    String valor = filtro.split("=")[1];
                    int columnaInt = Integer.parseInt(columna);

                    CondicionFiltro condicion = new CondicionIgualdad(columnaInt, valor);
                    condiciones.add(condicion);
                }

                // Hay que crear una forma de unir condiciones
                CondicionFiltro cond = condiciones.getFirst();

                System.out.println("\nRegistros que cumplen los filtros:");

                int j = 0;
                for (RegistroCSV registro : almacen) {
                    if (cond.cumple(registro)) {
                        j++;
                        System.out.println("Línea " + registro.getNumeroLinea() + ": " + registro);
                    }
                    if (j >= n) break;
                }
            }

        } catch (Exception e) {
            System.err.println("Error al leer archivo: " + e.getMessage());
        }
    }
}
