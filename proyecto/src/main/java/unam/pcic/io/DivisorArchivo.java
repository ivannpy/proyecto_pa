package unam.pcic.io;

import unam.pcic.dominio.RegistroCSV;
import java.io.File;
import java.util.List;
import java.io.BufferedWriter;
import java.io.Writer;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;

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
        int K = 10;
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

        LectorCSV lector = new LectorCSV(archivoDeEntrada, true);

        List<File> archivosTemporales = AdminArchivosTmp.creaArchivosTemporales(archivoDeEntrada, cantidadSubarchivos);

        int i = 0;

        try (EscritorCSVMultiple escritor = new EscritorCSVMultiple(archivosTemporales)) {
            String[] encabezadoArr = lector.leerEncabezado();
            if (encabezadoArr != null) {
                RegistroCSV encabezadoRegistro = new RegistroCSV(encabezadoArr, 0L);
                escritor.escribeEncabezadoEnTodos(encabezadoRegistro.serializa());
            }

            String linea;
            while ((linea = lector.siguienteLinea()) != null) {
                int idx = i % cantidadSubarchivos;
                escritor.escribeLineaEn(idx, linea);
                i++;

                if (i % 100_000 == 0) {
                    System.out.println("Lineas procesadas: " + i);
                }
            }
            escritor.flush();
        } catch (Exception e) {
            System.err.println("Error al dividir archivo: " + e.getMessage());
        }
    }


}
