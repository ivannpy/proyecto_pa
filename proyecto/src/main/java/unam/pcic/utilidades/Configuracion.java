package unam.pcic.utilidades;


import unam.pcic.Procesamiento;
import unam.pcic.dominio.*;
import unam.pcic.io.LectorCSV;
import unam.pcic.io.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;



/**
 * - Lee parámetros de entrada del usuario
 * - Valida criterios de búsqueda o filtrado.
 * - Configura rutas de entrada y salida.
 */
public class Configuracion {

    /**
     * Menú interactivo.
     *
     * @param args Se espera que args[0] pueda ser el nombre del archivo a procesar (opcional).
     * @return Un arreglo con las banderas dadas por el usuario.
     */
    public static String[] menuInteractivo(String[] args) {
        Logger logger = Logger.getInstancia();
        Scanner sc = new Scanner(System.in);
        String archivo;

        // 1) Solicitar archivo si no viene por args
        if (args.length == 0) {
            System.out.print("Ruta del archivo CSV a procesar: ");
            archivo = sc.nextLine().trim();
        } else {
            archivo = args[0];
            logger.debug("Archivo csv (desde parámetros): " + archivo);
            System.out.println("Archivo a procesar (desde parámetros): " + archivo);
        }

        // 1.1) Validación de existencia del archivo
        File file = new File(archivo);

        while (!file.exists() || !file.isFile()) {
            System.out.println("Error: El archivo no existe o no es un archivo válido.");
            logger.error("Archivo no válido: " + archivo);
            System.out.print("Ingrese nuevamente la ruta del archivo CSV a procesar: ");
            archivo = sc.nextLine().trim();
            file = new File(archivo); // actualizar referencia
        }

        logger.debug("Archivo válido encontrado: " + archivo);
        System.out.println("Archivo válido encontrado: " + archivo);

        // 2) Mostrar columnas disponibles leyendo el encabezado
        String[] encabezado = new String[0];
        try {
            LectorCSV lector = new LectorCSV(new File(archivo), true);
            encabezado = lector.leerEncabezado();
            System.out.println("\nColumnas disponibles:");
            for (int i = 0; i < encabezado.length; i++) {
                System.out.println(i + " - " + encabezado[i]);
            }
        } catch (Exception e) {
            logger.error("Error al leer encabezado del archivo: " + e.getMessage());
            System.out.println("Error al leer encabezado del archivo: " + e.getMessage());
        }

        // 3) Selección de columnas
        System.out.println();
        System.out.println("Seleccione las columnas a procesar separadas por comas (ej. 2,11,15)");
        System.out.print("O escriba '*' para procesar todas las columnas: ");
        String columnas = sc.nextLine().trim();


        // 4) Mostrar columnas seleccionadas
        System.out.println("\nColumnas seleccionadas:");
        if (columnas.equals("*")) {
            // Si el usuario pone '*', convertimos a "0,1,2,...,n-1"
            StringBuilder columnasTodas = new StringBuilder();
            for (int i = 0; i < encabezado.length; i++) {
                System.out.println(i + " - " + encabezado[i]);
                columnasTodas.append(i).append(",");
            }
            if (encabezado.length > 0) {
                columnas = columnasTodas.substring(0, columnasTodas.length() - 1);
            } else {
                columnas = "";
            }
        } else {
            String[] columnasSeleccionadas = columnas.split(",");
            for (int i = 0; i < columnasSeleccionadas.length; i++) {
                try {
                    int indiceReal = Integer.parseInt(columnasSeleccionadas[i].trim());
                    if (indiceReal >= 0 && indiceReal < encabezado.length) {
                        System.out.println(i + " - " + encabezado[indiceReal]);
                    } else {
                        System.out.println(i + " - [Índice fuera de rango: " + indiceReal + "]");
                    }
                } catch (NumberFormatException e) {
                    logger.error("Las columnas deben ser enteros no negativos separados por comas.");
                    System.out.println("Las columnas deben ser enteros no negativos separados por comas.");
                    System.exit(1);
                }
            }
        }

        // 5) Solicitar filtros
        System.out.println();
        System.out.println("Escriba los filtros a usar separados por comas.");
        System.out.println("Se tienen los filtros: mayor que (>), menor que (<), igual a (=),");
        System.out.println("mayor o igual que (>=), menor o igual que (<=).");
        System.out.println("Sintaxis: cN<valor, cN>valor, cN=valor, cN>=valor, cN<=valor donde N es el número de columna.");
        System.out.println("Ejemplos: c1=spanish, c2>10, c3<=100");
        System.out.print("Filtros: ");
        String filtros = sc.nextLine().trim();

        // 5.5) Solicitar el LÍMITE de filas (-l)
        System.out.println();
        System.out.println("Límite de filas a escribir en resultado.csv:");
        System.out.println("  - Escriba un número (ej. 100)");
        System.out.println("  - Escriba '*' para escribir TODAS las filas");
        System.out.print("Límite: ");

        String limiteInput = sc.nextLine().trim();
        String limite;

        if (limiteInput.equals("*")) {
            limite = "999999999";  // valor muy alto = todas
            System.out.println("Se imprimirán todas las filas coincidentes.");
        } else {
            limite = limiteInput;
            System.out.println("Se imprimirán máximo " + limite + " filas.");
        }

        // 6) Preguntar modo de ejecución (SEC / CONC / BOTH)
        System.out.println();
        System.out.println("Seleccione el modo de ejecución:");
        System.out.println("  1) Secuencial");
        System.out.println("  2) Concurrente (con hilos)");
        System.out.println("  3) Ambos (primero secuencial y luego concurrente)");
        System.out.print("Opción [1/2/3]: ");

        String opcionModo = sc.nextLine().trim();
        String modo;

        switch (opcionModo) {
            case "2":
                modo = "conc";
                break;
            case "3":
                modo = "both";
                break;
            case "1":
            default:
                modo = "sec";
                break;
        }

        logger.debug("Columnas seleccionadas por usuario: " + columnas);
        logger.debug("Filtros seleccionados por usuario: " + filtros);
        logger.debug("Límite de impresión: " + limite);
        logger.debug("Modo de procesamiento: " + modo);

        // 7) Mensaje de ejecución
        System.out.println();
        System.out.println("Ejecución en proceso...");

        // Retornar args sintéticos para parsea()
        return new String[]{
                archivo,
                "-c", columnas,
                "-f", filtros,
                "-l", limite,
                "-m", modo
        };
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
            int columnaInt;
            try {
                columnaInt = Integer.parseInt(numeroColumnaStr);
            } catch (NumberFormatException e) {
                logger.error("Número de columna inválido en filtro: " + filtro);
                System.exit(1);
                return List.of(); // para calmar al compilador
            }

            if (parteValor.isEmpty()) {
                logger.error("Valor vacío en filtro: " + filtro);
                System.exit(1);
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
                    logger.error("Operador no soportado: " + operador);
                    System.exit(1);
                    return List.of();
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
            opciones.setRutaArchivo(args[0]);
            opciones.setArchivoDeEntrada();
            opciones.setCarpetaTemporal();
        }

        boolean columnasRepetido = false;
        boolean filtrosRepetido = false;
        boolean limiteRepetido = false;

        Procesamiento modo = Procesamiento.SECUENCIAL;
        boolean ejecutarAmbos = false;

        for (int i = 0; i < args.length; i++) {
            String arg = args[i];

            switch (arg) {
                case "-l":
                case "--limite":
                    if (limiteRepetido) {
                        logger.warn("La opción límite se repite.");
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
                    } catch (NumberFormatException e) {
                        logger.error("Las columnas deben ser enteros no negativos separados por comas.", e);
                        System.out.println("Las columnas deben ser enteros no negativos separados por comas.");
                        System.exit(1);
                    }

                    opciones.setColumnas(columnas);
                    columnasRepetido = true;
                    break;
                case "-m":
                case "--modo":
                    if (i + 1 >= args.length) {
                        logger.error("La opción modo requiere un valor: sec | conc | both");
                        System.exit(1);
                    }
                    String valorModo = args[++i];
                    if ("conc".equalsIgnoreCase(valorModo)) {
                        modo = Procesamiento.CONCURRENTE;
                    } else if ("both".equalsIgnoreCase(valorModo)) {
                        ejecutarAmbos = true;
                        modo = Procesamiento.SECUENCIAL; // base
                    } else {
                        modo = Procesamiento.SECUENCIAL;
                    }
                    opciones.setModoProcesamiento(modo);
                    opciones.setEjecutarAmbos(ejecutarAmbos);
                    break;
            }
        }

        return opciones;
    }

}


