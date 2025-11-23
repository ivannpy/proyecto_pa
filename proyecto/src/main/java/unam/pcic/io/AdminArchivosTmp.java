package unam.pcic.io;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * - Administra la carpeta temporal.
 * - Crea y elimina archivos temporales.
 * - Hace la limpieza automática al finalizar.
 */
public class AdminArchivosTmp {

    /**
     * Construtor privado.
     */
    private AdminArchivosTmp() {}

    /**
     * Crea una carpeta temporal con el nombre dado.
     *
     * @param rutaCarpeta la ruta de la carpeta temporal.
     */
    public static void creaCarpetaTemporal(String rutaCarpeta) {

    }

    /**
     * Elimina una carpeta temporal con el nombre dado.
     *
     * @param rutaCarpeta la ruta de la carpeta temporal.
     */
    public static void eliminaCarpetaTemporal(String rutaCarpeta) {

    }

    /**
     * Crea un archivo temporal con el nombre dado con sufijo _tmp_indice.csv
     *
     * @param nombre El nombre del archivo.
     * @param indice La parte del archivo que se escribirá
     * @return Un archivo con el nombre dado con sufijo indicando que es temporal y la parte.
     */
    public static File creaArchivoTemporal(String nombre, int indice) {
        String nombreArchivoTmp = nombre.replace(".csv", "_tmp_" + indice + ".csv");
        return new File(nombreArchivoTmp);
    }

    /**
     * Crea una lista de archivos temporales para un archivo dado.
     *  La lista de archivos creada es para escribir las partes del archivo base.
     *
     * @param archivo el archivo base.
     * @param cantidad la cantidad de archivos temporales a crear.
     * @return una lista de archivos temporales.
     */
    public static List<File> creaArchivosTemporales(File archivo, int cantidad) {
        // Si se crean en una carpeta temporal, no necesitamos la ruta absoluta para crear los archivos
        // Solo hay que crear los archivos temporales adentro de la carpeta temporal.
        String rutaArchivo = archivo.getAbsolutePath();

        List<File> temporales = new ArrayList<>();
        for (int i = 0; i < cantidad; i++) {
            temporales.add(creaArchivoTemporal(rutaArchivo, i + 1));
        }

        return temporales;
    }

}
