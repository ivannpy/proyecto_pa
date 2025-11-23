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

    private AdminArchivosTmp() {}

    public static void creaCarpetaTemporal() {

    }

    public static void eliminaCarpetaTemporal() {

    }

    public static File creaArchivoTemporal(String nombre, int indice) {
        String archivoTemporal = nombre.replace(".csv", "_tmp_" + indice + ".csv");
        return new File(archivoTemporal);
    }

    public static List<File> creaArchivosTemporales(File archivo, int cantidad) {
        String nombre = archivo.getAbsolutePath();
        List<File> temporales = new ArrayList<>();
        for (int i = 0; i < cantidad; i++) {
            temporales.add(creaArchivoTemporal(nombre, i + 1));
        }
        return temporales;
    }

}
