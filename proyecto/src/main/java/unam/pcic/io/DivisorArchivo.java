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
        int K = 5;
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

    /**
     * Regresa la carpeta temporal.
     * @return la carpeta temporal.
     */
    public File getCarpetaTemporal() {
        return carpetaTemporal;
    }

    /**
     * Divide el archivo de entrada en tantos subarchivos como procesadores hay en el sistema.
     *
     */
    public void divide() {
        // TODO: Permitir opcion sin encabezados (leerlo de Opciones)
        LectorCSV lector = new LectorCSV(archivoDeEntrada, true);

        List<File> archivosTemporales = AdminArchivosTmp.creaArchivosTemporales(archivoDeEntrada, cantidadSubarchivos);

        int i = 0;

        try (EscritorCSVMultiple escritor = new EscritorCSVMultiple(archivosTemporales)) {
            String[] valoresEncabezado = lector.leerEncabezado();
            if (valoresEncabezado != null) {
                RegistroCSV registroEncabezado = new RegistroCSV(valoresEncabezado, 0L);
                escritor.escribeEnTodos(registroEncabezado.serializa());
            }

            String linea;
            while ((linea = lector.siguienteLinea()) != null) {
                int indiceArchivoTmp = i % cantidadSubarchivos;
                escritor.escribeLineaEn(indiceArchivoTmp, linea);
                i++;
            }
            escritor.flush();
        } catch (Exception e) {
            // TODO: Manejar con el Logger
            System.out.println("Error al dividir archivo: " + archivoDeEntrada.getName());
        }
    }


}
