package unam.pcic;

import unam.pcic.dominio.RegistroCSV;
import unam.pcic.io.LectorCSV;
import unam.pcic.utilidades.Configuracion;
import unam.pcic.utilidades.Opciones;

import java.io.*;
import java.net.URL;
import java.util.List;


public class Main {
    /**
     * Imprime las instrucciones de uso.
     *
     * @param args los argumentos de la linea de comandos.
     */
    private static void ayuda(String[] args) {
        if (args.length == 1 && (args[0].equals("-h") || args[0].equals("--help"))) {
            System.out.println("Uso: java -jar proyecto.jar [archivo.csv | banderas]");
            System.out.println("Banderas:");
            System.out.println("-h, --ayuda: Imprime esta ayuda.");
            System.out.println("-c --columnas: Lista de columnas separadas por comas.");
            System.out.println("-f --filtros: Filtro por aplicar.");
            System.exit(1);
        }
    }

    public static void main(String[] args) {
        Main.ayuda(args);

        Opciones opciones = Configuracion.parsea(args);

        // ControladorAplicacion.run(opciones)

        System.out.println(opciones);

        prueba(opciones);
    }

    private static void prueba(Opciones opciones) {
        try {
            File inputFile;
            // Si la ejecución de hace sin argumentos
            if (!opciones.getHayArgumentos() || !opciones.getHayArchivo()){
                ClassLoader classLoader = Main.class.getClassLoader();
                URL resource = classLoader.getResource("sample.csv");

                if (resource == null) {
                    // Meterlo a Logger
                    System.err.println("Error: No se encontró el archivo en resources/");
                    return;
                }
                inputFile = new File(resource.getFile());
            } else {
                // Si la ejecución se hace con argumentos
                inputFile = new File(opciones.getArchivo());
            }

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