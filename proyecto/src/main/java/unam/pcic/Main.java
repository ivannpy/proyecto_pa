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
    private static void ayuda(String[] args) {
        if (args.length == 0) {
            System.out.println("Uso: java -jar proyecto.jar path/archivo.csv");
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

        Logger logger = Logger.getInstancia();
        logger.debug("Inicia la aplicación");

        Opciones opciones = Configuracion.parsea(Configuracion.menuInteractivo(args));
        System.out.println(opciones);

        ControladorAplicacion.ejecutar(opciones);
    }
}