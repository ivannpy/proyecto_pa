package unam.pcic.utilidades;


import unam.pcic.dominio.*;
import unam.pcic.io.LectorCSV;
import unam.pcic.io.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * - Lee parámetros de entrada del usuario
 * - Valida criterios de búsqueda o filtrado.
 * - Configura rutas de entrada y salida.
 */
public class Configuracion {

    /**
     * Menú interactivo.
     *
     * @param args Se espera que args[0] sea el nombre del archivo a procesar.
     * @return Un arreglo con las banderas dadas por el usuario.
     */
    public static String[] menuInteractivo(String[] args) {
        Logger logger = Logger.getInstancia();

        String nombreArchivo = "";

        try {
            nombreArchivo = args[0];
            logger.debug("Archivo csv: " + nombreArchivo);
        } catch (IndexOutOfBoundsException e) {
            logger.error("No se ha proporcionado el nombre del archivo csv.");
            System.exit(1);
        }

        File archivo = new File(nombreArchivo);
        LectorCSV lector = new LectorCSV(archivo, true);

        String[] encabezados = new String[0];
        try {
            encabezados = lector.leerEncabezado();
            System.out.println("Columnas disponibles:");
            for (int i = 0; i < encabezados.length; i++) {
                System.out.println(i + " --- " + encabezados[i]);
            }
        } catch (Exception e) {
            logger.error("Error al leer encabezado del archivo: " + e.getMessage());
            System.exit(1);
        }

        // TODO: Hacer validaciones de las entradas del usuario
        System.out.print("Seleccione las columnas a procesar separadas por comas (e.g 2,10,11)");
        System.out.println(" o escriba '*' para procesar todas las columnas.");
        String columnas = System.console().readLine();

        System.out.println("Columnas seleccionadas: ");

        if (columnas.equals("*")) {
            System.out.println("Procesando todas las columnas.");
            StringBuilder columnasTodas = new StringBuilder();
            for (int i = 0; i < encabezados.length; i++) {
                columnasTodas.append(i).append(",");
            }
            columnas = columnasTodas.toString();
            columnas = columnas.substring(0, columnas.length() - 1);
        } else {
            String[] columnasSeleccionadas = columnas.split(",");
            for (int i = 0; i < columnasSeleccionadas.length; i++) {
                try {
                    System.out.println(i + " --- " + encabezados[Integer.parseInt(columnasSeleccionadas[i])]);
                } catch (NumberFormatException e) {
                    logger.error("Las columnas deben ser enteros no negativos separados por comas.");
                    System.exit(1);
                }
            }
        }
        System.out.println("Escriba los filtros a usar separados por comas (e.g c1=spanish): ");
        String filtros = System.console().readLine();

        logger.debug("Columnas seleccionadas por usuario: " + columnas);
        logger.debug("Filtros seleccionados por usuario: " + filtros);

        return new String[]{nombreArchivo, "-c", columnas, "-f", filtros,  "-l", "10",};
    }

    /**
     * Parsea los filtros dados en una cadena y los regresa como una lista de CondicionFiltro.
     *
     * @param filtros La cadena con los filtros separados por comas.
     * @return Una lista de CondicionFiltro.
     */
    private static List<CondicionFiltro<RegistroCSV>> parseaFiltros(String filtros) {
        if (filtros.isEmpty()) return List.of();

        Logger logger = Logger.getInstancia();

        String[] filtrosArr = filtros.split(",");
        List<CondicionFiltro<RegistroCSV>> condiciones = new ArrayList<>();

        for (String filtroStr : filtrosArr) {
            String filtro = filtroStr.trim();
            if (filtro.isEmpty()) continue;

            String operador;
            int idxOp;

            if ((idxOp = filtro.indexOf(">=")) != -1) {
                operador = ">=";
            } else if ((idxOp = filtro.indexOf("<=")) != -1) {
                operador = "<=";
            } else if ((idxOp = filtro.indexOf("=")) != -1) {
                operador = "=";
            } else if ((idxOp = filtro.indexOf(">")) != -1) {
                operador = ">";
            } else if ((idxOp = filtro.indexOf("<")) != -1) {
                operador = "<";
            } else {
                throw new IllegalArgumentException("Filtro inválido: " + filtro);
            }

            String parteColumna = filtro.substring(0, idxOp).trim();
            String parteValor = filtro.substring(idxOp + operador.length()).trim();

            if (!parteColumna.startsWith("c")) {
                logger.error("La columna debe empezar con 'c': " + parteColumna);
                System.exit(1);
            }

            String numeroColumnaStr = parteColumna.substring(1);
            int columnaInt = 0;
            try {
                columnaInt = Integer.parseInt(numeroColumnaStr);
            } catch (NumberFormatException e) {
                logger.error("Número de columna inválido en filtro: " + filtro);
                System.exit(1);
            }

            if (parteValor.isEmpty()) {
                logger.error("Valor vacío en filtro: " + filtro);
                System.exit(1);
            }

            CondicionFiltro<RegistroCSV> condicion = null;
            switch (operador) {
                case "=":
                    condicion = new CondicionIgualdad(columnaInt, parteValor);
                    break;
                case ">":
                    condicion = new CondicionMayor(columnaInt, parteValor);
                    break;
                case "<":
                    condicion = new CondicionMenor(columnaInt, parteValor);
                    break;
                case ">=":
                    condicion = new CondicionMayorIgual(columnaInt, parteValor);
                    break;
                case "<=":
                    condicion = new CondicionMenorIgual(columnaInt, parteValor);
                    break;
                default:
                    logger.error("Operador no soportado: " + operador);
                    System.exit(1);
            }

            condiciones.add(condicion);
        }

        return condiciones;
    }

    /**
     * Parsea una lista de columnas separadas por comas.
     *
     * @param columnas La lista de columnas separadas por comas.
     * @return Un arreglo de enteros con las columnas.
     */
    private static int[] parseaColumnas(String columnas) {
        Logger logger = Logger.getInstancia();

        String[] listaColumnas = columnas.split(",");
        int[] columnasArr = new int[listaColumnas.length];
        for (int j = 0; j < listaColumnas.length; j++) {
            try {
                columnasArr[j] = Integer.parseInt(listaColumnas[j]);
                if (columnasArr[j] < 0) {
                    throw new NumberFormatException("Las columnas deben ser no negativas.");
                }
            } catch (NumberFormatException e) {
                logger.error("Las columnas deben ser enteros no negativos separados por comas.");
                throw e;
            }
        }
        return columnasArr;
    }

    /**
     * Parsea los argumentos de la linea de comandos o los dados por el menú interactivo.
     *
     * @param args los argumentos de la linea de comandos o del menú interactivo.
     * @return Una instancia de Opciones con los argumentos.
     */
    public static Opciones parsea(String[] args) {
        Logger logger = Logger.getInstancia();

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
                    if (limiteRepetido) {
                        logger.warn("La opción filtros se repite.");
                    }
                    if (i + 1 >= args.length) {
                        logger.error("La opción limite requiere numero entero");
                        System.exit(1);
                    }
                    String limiteStr = args[++i];
                    int limite = Integer.parseInt(limiteStr);
                    opciones.setLimiteImpresion(limite);
                    limiteRepetido = true;
                    break;
                case "-f":
                case "--filtros":
                    if (filtrosRepetido) {
                        logger.warn("La opción filtros se repite.");
                    }
                    String filtrosStr = args[++i];

                    List<CondicionFiltro<RegistroCSV>> filtros = parseaFiltros(filtrosStr);
                    opciones.setFiltros(filtros);

                    filtrosRepetido = true;
                    break;
                case "-c":
                case "--columnas":
                    if (columnasRepetido) {
                        logger.warn("La opción columnas se repite.");
                    }
                    if (i + 1 >= args.length) {
                        logger.error("La opción columnas requiere una lista de enteros o *");
                        System.exit(1);
                    }
                    String columnasStr = args[++i];

                    int[] columnas = new int[0];
                    try {
                        columnas = parseaColumnas(columnasStr);
                    } catch (NumberFormatException e ) {
                        logger.error("Las columnas deben ser enteros no negativos separados por comas.", e);
                        System.out.println("Las columnas deben ser enteros no negativos separados por comas.");
                        System.exit(1);
                    }

                    opciones.setColumnas(columnas);
                    columnasRepetido = true;
                    break;
            }
        }

        return opciones;
    }

}


