package unam.pcic.io;

import unam.pcic.dominio.RegistroCSV;
import java.io.File;
import java.util.List;


/**
 * - Divide el archivo en N subarchivos temporales.
 * - Calcula N según la cantidad de procesadores disponibles.
 * - Distribuye registros equitativamente entre los subarchivos.
 * - Preserva el encabezado de cada subarchivo.
 */
public class DivisorArchivo {
    private final int cantidadProcesadores;

    public DivisorArchivo() {
        this.cantidadProcesadores = Runtime.getRuntime().availableProcessors();
    }

    public int getCantidadProcesadores() {
        return cantidadProcesadores;
    }

    /**
     * Divide el archivo de entrada en subarchivos.
     *
     * @param archivo el archivo de entrada.
     */
    public void divide(File archivo){
        // Divide no hace ningun procesamiento.
        // Solo toma el archivo y lo divide en subarchivos.
        // Luego, cada subarchivo se procesa por separado.
        // Cuando se procesan los subarchivos, se procesan como RegistroCSV
        // Se aplican los filtros, etc, se hace limpieza y se escribe en un archivo nuevo.

        LectorCSV lector = new LectorCSV(archivo, true);

        // Hacer limpieza de otros archivos temporales
        List<File> archivosTemporales = AdminArchivosTmp.creaArchivosTemporales(archivo, cantidadProcesadores);

        // Escribe el encabezado en cada subarchivo.
        try {
            RegistroCSV encabezado = new RegistroCSV(lector.leerEncabezado(), 0L);
            for (File archivoTemporal : archivosTemporales) {
                EscritorCSV.escribeRegistro(encabezado, archivoTemporal);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        int i = 0;

        try {
            RegistroCSV registro;
            while ((registro = lector.nextRegistro()) != null) {
                EscritorCSV.escribeRegistro(registro, archivosTemporales.get(i % cantidadProcesadores));
                i++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
