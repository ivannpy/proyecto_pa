package unam.pcic.utilidades;


import unam.pcic.dominio.*;
import unam.pcic.io.LectorCSV;
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
        String nombreArchivo = args[0];

        System.out.println("Archivo a procesar: " + nombreArchivo);

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
            // TODO: Manejarlo con el Logger.
            System.out.println("Error al leer encabezado del archivo: " + e.getMessage());
        }

        // TODO: Hacer validaciones de las entradas del usuario
        System.out.print("Seleccione las columnas a procesar separadas por comas (e.g 2,10,11)");
        System.out.println(" o escriba '*' para procesar todas las columnas.");
        String columnas = System.console().readLine();

        System.out.println("Columnas seleccionadas: ");

        String[] columnasSeleccionadas = columnas.split(",");
        for (int i = 0; i < columnasSeleccionadas.length; i++) {
            System.out.println(i + " --- " + encabezados[Integer.parseInt(columnasSeleccionadas[i])]);
        }

        System.out.println("Escriba los filtros a usar separados por comas (e.g c1=spanish): ");
        String filtros = System.console().readLine();

        return new String[]{nombreArchivo, "-c", columnas, "-f", filtros,  "-l", "10",};
    }

    /**
     * Parsea los filtros dados en una cadena y los regresa como una lista de CondicionFiltro.
     *
     * @param filtros La cadena con los filtros separados por comas.
     * @return Una lista de CondicionFiltro.
     */
    private static List<CondicionFiltro<RegistroCSV>> parseaFiltros(String filtros) {
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
                throw new IllegalArgumentException("La columna debe empezar con 'c': " + parteColumna);
            }

            String numeroColumnaStr = parteColumna.substring(1);
            int columnaInt;
            try {
                columnaInt = Integer.parseInt(numeroColumnaStr);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Número de columna inválido en filtro: " + filtro);
            }

            if (parteValor.isEmpty()) {
                throw new IllegalArgumentException("Valor vacío en filtro: " + filtro);
            }

            CondicionFiltro<RegistroCSV> condicion;
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
                    throw new IllegalStateException("Operador no soportado: " + operador);
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
        String[] listaColumnas = columnas.split(",");
        int[] columnasArr = new int[listaColumnas.length];
        for (int j = 0; j < listaColumnas.length; j++) {
            try {
                columnasArr[j] = Integer.parseInt(listaColumnas[j]);
                if (columnasArr[j] < 0)
                    throw new NumberFormatException("La columna debe ser mayor o igual a cero.");
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Las columnas deben ser enteros no negativos separados por comas.");
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
                    limiteRepetido = true;
                    break;
                case "-f":
                case "--filtros":
                    if (filtrosRepetido)
                        throw new IllegalArgumentException("La opción filtros se repite.");
                    String filtrosStr = args[++i];

                    List<CondicionFiltro<RegistroCSV>> filtros = parseaFiltros(filtrosStr);
                    opciones.setFiltros(filtros);

                    filtrosRepetido = true;
                    break;
                case "-c":
                case "--columnas":
                    if (columnasRepetido)
                        throw new IllegalArgumentException("La opción columnas se repite.");
                    if (i + 1 >= args.length)
                        throw new IllegalArgumentException("La opción columnas requiere una lista de enteros o *");
                    String columnasStr = args[++i];

                    if (columnasStr.equals("*")) {
                        opciones.setColumnas(new int[0]);
                        opciones.setTodasLasColumnas(true);
                        columnasRepetido = true;
                        break;
                    }

                    int[] columnas = parseaColumnas(columnasStr);
                    opciones.setColumnas(columnas);
                    columnasRepetido = true;
                    break;
            }
        }

        return opciones;
    }

}


