package unam.pcic;

import unam.pcic.utilidades.Configuracion;
import unam.pcic.utilidades.Opciones;


public class Main {
    /**
     * Imprime las instrucciones de uso.
     *
     * @param args los argumentos de la linea de comandos.
     */
    private static void ayuda(String[] args) {
        if (args.length == 0) {
            System.out.println("Uso: java -jar proyecto.jar archivo.csv [ banderas ]");
            System.exit(1);
        }

        if (args.length == 1 && (args[0].equals("-h") || args[0].equals("--help"))) {
            System.out.println("Uso: java -jar proyecto.jar [archivo.csv | banderas]");
            System.out.println("Banderas:");
            System.out.println("-h, --help: Imprime esta ayuda.");
            System.out.println("-c --columnas: Lista de columnas separadas por comas.");
            System.out.println("-f --filtros: Filtro por aplicar.");
            System.exit(1);
        }
    }

    /**
     * Punto de entrada al programa.
     *
     * @param args
     */
    public static void main(String[] args) {
        Main.ayuda(args);

        Opciones opciones = Configuracion.parsea(args);
        System.out.println(opciones);

        ControladorAplicacion.ejecutar(opciones);
    }
}