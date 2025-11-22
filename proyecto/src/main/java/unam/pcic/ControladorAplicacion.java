package unam.pcic;

import unam.pcic.dominio.Almacen;
import unam.pcic.dominio.AlmacenRenglones;
import unam.pcic.dominio.CriterioFiltro;
import unam.pcic.dominio.RegistroCSV;
import unam.pcic.io.LectorCSV;
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
        prueba(opciones);
    }


    private static void prueba(Opciones opciones) {
        try {
            File inputFile = new File(opciones.getArchivo());

            LectorCSV lector = new LectorCSV(inputFile, true);

            String[] encabezados = lector.leerEncabezado();
            System.out.println("Columnas: " + encabezados.length);
            for (int i = 0; i < encabezados.length; i++) {
                System.out.println("  [" + i + "] " + encabezados[i]);
            }

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

            Almacen<RegistroCSV> almacen = new AlmacenRenglones(lector.leer());

            System.out.println("Total de registros leídos: " + almacen.getLongitud());

            for (RegistroCSV registro : almacen) {
                if (registro.getNumeroColumnas() != 24)
                    System.out.println("No hay 24 columnas en cada registro");
            }

            int n = 10;
            System.out.println("\nPrimeros " + n + " registros:");
            for (int i = 0; i < Math.min(n, almacen.getLongitud()); i++) {
                RegistroCSV registro = almacen.get(i);
                System.out.println("Línea " + registro.getNumeroLinea() + ": " + registro);
            }

            if (opciones.getColumnas() != null) {
                CriterioFiltro<RegistroCSV> criterio = CriterioFiltro.paraRegistroCSV();
                Almacen<RegistroCSV> seleccionados = criterio.seleccionarColumnas(almacen, opciones.getColumnas());

                int m = 10;
                System.out.println("\nPrimeros " + m + " registros (columnas seleccionadas):");
                for (int i = 0; i < Math.min(m, almacen.getLongitud()); i++) {
                    RegistroCSV registro = seleccionados.get(i);
                    System.out.println("Línea " + registro.getNumeroLinea() + ": " + registro);
                }

            }
        } catch (Exception e) {
            System.err.println("Error al leer archivo: " + e.getMessage());
        }
    }
}
