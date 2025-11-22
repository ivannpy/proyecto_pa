package unam.pcic.utilidades;

/**
 * - Lee parámetros de entrada del usuario
 * - Valida criterios de búsqueda o filtrado.
 * - Configura rutas de entrada y salida.
 */
public class Configuracion {
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

        for (int i = 0; i < args.length; i++) {
            String arg = args[i];

            switch (arg) {
                case "-f":
                case "--filtros":
                    if (filtrosRepetido)
                        throw new IllegalArgumentException("La opción filtros se repite.");
                    String filtrosStr = args[++i];
                    opciones.setFiltros(filtrosStr.split(",", -1));
                    filtrosRepetido = true;
                    break;
                case "-c":
                case "--columnas":
                    if (columnasRepetido)
                        throw new IllegalArgumentException("La opción columnas se repite.");
                    if (i + 1 >= args.length)
                        throw new IllegalArgumentException("La opción columnas requiere una lista de enteros.");
                    String columnasStr = args[++i];
                    String[] listaColumnas = columnasStr.split(",");
                    int[] columnas = new int[listaColumnas.length];
                    for (int j = 0; j < listaColumnas.length; j++) {
                        try {
                            columnas[j] = Integer.parseInt(listaColumnas[j]);
                            if (columnas[j] <= 0)
                                throw new NumberFormatException("La columna debe ser mayor a cero.");
                        } catch (NumberFormatException e) {
                            throw new IllegalArgumentException("Las columnas deben ser enteros positivos separados por comas.");
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


