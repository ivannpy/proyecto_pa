package unam.pcic.utilidades;


/**
 * - Lee parámetros de entrada del usuario
 * - Valida criterios de búsqueda o filtrado.
 * - Configura rutas de entrada y salida.
 */
public class Configuracion {

    public static Opciones menuInteractivo() {
        System.out.println("Ingrese el archivo a procesar...");
        // TODO: regresar un objeto Opciones con lo que elija el usuario
        return null;
    }
    /** Parsea los argumentos de la linea de comandos.
     *
     * @param args los argumentos de la linea de comandos.
     * @return una instancia de {@link Opciones}.
     */
    public static Opciones parsea(String[] args) {
        // Aquí se tienen que hacer las validaciones de los argumentos de la linea de comandos

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


