package unam.pcic;

import unam.pcic.dominio.RegistroCSV;
import unam.pcic.io.LectorCSV;
import unam.pcic.utilidades.Opciones;
import java.io.File;
import java.net.URL;
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

            List<RegistroCSV> registros = lector.leer();
            System.out.println("Total de registros leídos: " + registros.size());

            for (RegistroCSV registro : registros) {
                if (registro.getNumeroColumnas() != 24)
                    System.out.println("No hay 24 columnas en cada registro");
            }

            int n = 30;
            System.out.println("\nPrimeros " + n + " registros:");
            for (int i = 0; i < Math.min(n, registros.size()); i++) {
                RegistroCSV registro = registros.get(i);
                System.out.println("Línea " + registro.getNumeroLinea() + ": " + registro);
            }

        } catch (Exception e) {
            System.err.println("Error al leer archivo: " + e.getMessage());
        }
    }
}
