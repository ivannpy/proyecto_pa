package unam.pcic.utilidades;


import unam.pcic.io.LectorCSV;

import java.io.File;
import java.util.Arrays;

/**
 * - Lee parámetros de entrada del usuario
 * - Valida criterios de búsqueda o filtrado.
 * - Configura rutas de entrada y salida.
 */
public class Configuracion {

    /**
     * Menú interactivo. Se necesita el archivo a procesar.
     *
     * @param args Los argumentos de la linea de comandos
     * @return Un arreglo con las banderas dadas por el usuario.
     */
    public static String[] menuInteractivo(String[] args) {
        String nombreArchivo = args[0];

        System.out.println("Archivo a procesar: " + nombreArchivo);

        File archivo = new File(nombreArchivo);
        LectorCSV lector = new LectorCSV(archivo, true);
        try {
            String[] encabezados = lector.leerEncabezado();
            System.out.println("Columnas disponibles:");
            for (int i = 0; i < encabezados.length; i++) {
                System.out.println(i +" --- "+ encabezados[i]);
            }
        } catch (Exception e) {
            // TODO: Manejarlo con el Logger.
            System.out.println("Error al leer encabezado del archivo: " + e.getMessage());
        }

        // TODO: Hacer validaciones de las entradas del usuario
        System.out.print("Seleccione las columnas a procesar separadas por comas");
        System.out.println("o escriba 'todas' para procesar todas las columnas.");

        System.out.print("Escriba los filtros a usar separados por comas: ");

        // El arreglo que se retorna se debe crear con lo que se lea del usuario
        return new String[]{nombreArchivo, "-c", "2,10,11", "-l", "10", "-f", "c1=spanish"};

    }
    /** Parsea los argumentos de la linea de comandos.
     *
     * @param args los argumentos de la linea de comandos.
     * @return una instancia de {@link Opciones}.
     */
    public static Opciones parsea(String[] args) {
        Opciones opciones = new Opciones();

        opciones.setHayArgumentos(args.length > 0);
        opciones.setHayArchivo(opciones.getHayArgumentos() && args[0].endsWith(".csv"));

        if (opciones.getHayArchivo()) {
            opciones.setArchivo(args[0]);
        }

        boolean columnasRepetido = false;
        boolean filtrosRepetido = false;
        boolean limiteRepetido = false;

        for (int i = 0; i < args.length; i++) {
            String arg = args[i];

            switch (arg) {
                case "-l":
                case "--limite":
                    if (limiteRepetido)
                        throw new IllegalArgumentException("La opción filtros se repite.");
                    if (i + 1 >= args.length)
                        throw new IllegalArgumentException("La opción limite requiere numero entero");
                    String limiteStr = args[++i];
                    int limite = Integer.parseInt(limiteStr);
                    opciones.setLimiteImpresion(limite);
                    break;
                case "-f":
                case "--filtros":
                    if (filtrosRepetido)
                        throw new IllegalArgumentException("La opción filtros se repite.");
                    String filtrosStr = args[++i];
                    opciones.setFiltros(filtrosStr.split(",", -1));
                    // TODO: Los filtros se pueden parsear desde aquí.
                    filtrosRepetido = true;
                    break;
                case "-c":
                case "--columnas":
                    if (columnasRepetido)
                        throw new IllegalArgumentException("La opción columnas se repite.");
                    if (i + 1 >= args.length)
                        throw new IllegalArgumentException("La opción columnas requiere una lista de enteros o *");
                    String columnasStr = args[++i];
                    if (columnasStr.equals("todas") || columnasStr.equals("all")) {
                        opciones.setColumnas(new int[0]);
                        opciones.setTodasLasColumnas(true);
                        columnasRepetido = true;
                        break;
                    }
                    String[] listaColumnas = columnasStr.split(",");
                    int[] columnas = new int[listaColumnas.length];
                    for (int j = 0; j < listaColumnas.length; j++) {
                        try {
                            columnas[j] = Integer.parseInt(listaColumnas[j]);
                            if (columnas[j] < 0)
                                throw new NumberFormatException("La columna debe ser mayor o igual a cero.");
                        } catch (NumberFormatException e) {
                            throw new IllegalArgumentException("Las columnas deben ser enteros no negativos separados por comas.");
                        }
                    }
                    opciones.setColumnas(columnas);
                    columnasRepetido = true;
                    break;
            }
        }

        return opciones;
    }

}


