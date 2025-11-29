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
    /** Cantidad de procesadores en el sistema */
    private final int cantidadProcesadores;

    /** Cantidad de subarchivos a crear */
    private final int cantidadSubarchivos;

    /** Archivo de entrada */
    private final File archivoDeEntrada;

    /** Carpeta temporal */
    private final File carpetaTemporal;

    /**
     * Constructor por defecto.
     * El DivisorArchivo sabe cuantos procesadores hay en el sistema.
     */
    public DivisorArchivo(String rutaArchivoDeEntrada) {
        int K = 2;
        this.cantidadProcesadores = Runtime.getRuntime().availableProcessors();
        this.cantidadSubarchivos = cantidadProcesadores * K;
        this.archivoDeEntrada = new File(rutaArchivoDeEntrada);
        this.carpetaTemporal = new File(archivoDeEntrada.getParentFile() + File.separator + "tmp");

    }

    /**
     * Regresa la cantidad de procesadores disponibles.
     *
     * @return la cantidad de procesadores disponibles.
     */
    public int getCantidadProcesadores() {
        return cantidadProcesadores;
    }

    /**
     * Regresa la cantidad de subarchivos a crear.
     *
     * @return la cantidad de subarchivos a crear.
     */
    public int getCantidadSubarchivos() {
        return cantidadSubarchivos;
    }

    public File getCarpetaTemporal() {
        return carpetaTemporal;
    }

    /**
     * Divide el archivo de entrada en tantos subarchivos como procesadores hay en el sistema.
     *
     */
    public void divide() {
        // Divide no hace ningun procesamiento.
        // Solo toma el archivo y lo divide en subarchivos.
        // Luego, cada subarchivo se procesa por separado.
        // Cuando se procesan los subarchivos, se procesan como RegistroCSV
        // Se aplican los filtros, etc, se hace limpieza y se escribe en un archivo nuevo.

        LectorCSV lector = new LectorCSV(archivoDeEntrada, true);

        List<File> archivosTemporales = AdminArchivosTmp.creaArchivosTemporales(archivoDeEntrada, cantidadSubarchivos);

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
            while ((registro = lector.siguienteRegistro()) != null) {
                EscritorCSV.escribeRegistro(registro, archivosTemporales.get(i % cantidadSubarchivos));
                i++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
