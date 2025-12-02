package unam.pcic;

import unam.pcic.io.Logger;
import unam.pcic.utilidades.Configuracion;
import unam.pcic.utilidades.Opciones;


public class Main {
    /**
     * Imprime las instrucciones de uso.
     *
     * @param args los argumentos de la linea de comandos.
     */
    private static void ayuda() {
        System.out.println("Uso:");
        System.out.println("  java -jar proyecto.jar                        (modo interactivo)");
        System.out.println("  java -jar proyecto.jar <archivo.csv>          (archivo + menú interactivo)");
        System.out.println("  java -jar proyecto.jar <archivo.csv> [opciones]");
        System.out.println();
        System.out.println("Opciones:");
        System.out.println("  -c <cols>   columnas a procesar, ej: 0,1,2 o *");
        System.out.println("  -f <filtros> filtros separados por comas, ej: c1>10,c2=\"CDMX\"");
        System.out.println("  -l <n|*>    límite de filas a escribir (número o * para todas)");
        System.out.println("  -m <modo>   modo de ejecución: sec | conc | both");
        System.out.println();
        System.out.println("Ejemplos:");
        System.out.println("  java -jar proyecto.jar data/sample.csv");
        System.out.println("  java -jar proyecto.jar data/sample.csv -c 0,1 -m sec");
        System.out.println("  java -jar proyecto.jar data/sample.csv -c * -f c1>10 -l 100 -m both");
    }

    /**
     * Punto de entrada al programa.
     *
     * @param args argumentos de la línea de comandos
     */
    public static void main(String[] args) {
        Logger logger = Logger.getInstancia();
        logger.debug("Inicia la aplicación");

        // Caso ayuda explícita
        if (args.length == 1 && ("-h".equals(args[0]) || "--help".equals(args[0]))) {
            ayuda();
            return;
        }

        String[] argsParaParsear;

        if (args.length == 0) {
            // Sin parámetros → modo interactivo completo
            logger.debug("Sin parámetros: iniciando menú interactivo completo.");
            argsParaParsear = Configuracion.menuInteractivo(args);
        } else if (args.length == 1 && !args[0].startsWith("-")) {
            // Solo archivo → usar ese archivo y completar el resto por menú
            logger.debug("Solo se proporcionó archivo: " + args[0] + " → completando opciones vía menú interactivo.");
            argsParaParsear = Configuracion.menuInteractivo(args);
        } else {
            // Archivo + banderas (-c, -f, -l, -m, etc.) → usar args tal cual
            logger.debug("Archivo + banderas detectadas, usando args tal cual.");
            argsParaParsear = args;
        }

        Opciones opciones = Configuracion.parsea(argsParaParsear);
        logger.debug("Opciones de ejecución: " + opciones);

        ControladorAplicacion.ejecutar(opciones);
    }
}