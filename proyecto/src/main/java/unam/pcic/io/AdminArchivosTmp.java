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
    private static void creaCarpetaTemporal(String rutaCarpeta) {
        File carpetaTemporal = new File(rutaCarpeta);
        boolean creada = false;
        if (!carpetaTemporal.exists()){
            creada = carpetaTemporal.mkdirs();
        }

        if (creada) {
            // TODO: Manejarlo con el Logger
            System.out.println("Carpeta temporal creada: " + carpetaTemporal.getAbsolutePath());
        }
    }

    /**
     * Elimina una carpeta temporal con el nombre dado.
     *
     * @param carpetaTemporal La carpeta temporal a eliminar.
     */
    public static void eliminaCarpetaTemporal(File carpetaTemporal) {
        if (carpetaTemporal.exists()) {
           File[] archivos = carpetaTemporal.listFiles();

           // Si la carpeta no tiene archivos, elimina la carpeta
           if ( archivos == null || archivos.length == 0) {
               boolean eliminada = carpetaTemporal.delete();
               return;
            }

           // Si la carpeta no está vacía, eliminamos los archivos dentro de ella
           for (File archivo : archivos) {
               boolean eliminado = archivo.delete();
           }
           // Elimina la carpeta
           boolean carpetaEliminada = carpetaTemporal.delete();
        }
    }

    /**
     * Crea un archivo temporal con el nombre dado con sufijo _tmp_indice.csv
     *
     * @param nombre El nombre del archivo.
     * @param indice La parte del archivo que se escribirá
     * @return Un archivo con el nombre dado con sufijo indicando que es temporal y la parte.
     */
    private static File creaArchivoTemporal(String nombre, int indice) {
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
        String nombreCarpetaTemporal = archivo.getParent() + File.separator + "tmp";
        creaCarpetaTemporal(nombreCarpetaTemporal);

        // Ruta de los archivos temporales adentro de la carpeta temporal
        String rutaArchivo = nombreCarpetaTemporal + File.separator + archivo.getName();

        List<File> temporales = new ArrayList<>();
        for (int i = 0; i < cantidad; i++) {
            temporales.add(creaArchivoTemporal(rutaArchivo, i + 1));
        }

        return temporales;
    }

}
