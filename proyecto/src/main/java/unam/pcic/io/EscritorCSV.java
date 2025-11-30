package unam.pcic.io;

import unam.pcic.dominio.RegistroCSV;
import java.io.BufferedWriter;
import java.io.File;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;


/**
 * - Escribe en un solo archivo CSV
 * - Mantiene un BufferedWriter abierto.
 * - Es seguro para múltiples hilos.
 */
public class EscritorCSV implements AutoCloseable {

    /** Tamaño del buffer de escritura */
    private static final int LONGITUD_BUFFER_ESCRITURA = 2 * 1024 * 1024;

    /** Archivo donde se va a escribir */
    private final File archivoDestino;

    /** Escritor (BufferedWriter) para escribir en el archivo */
    private final BufferedWriter escritor;

    /**
     * Constructor.
     *      Crea un escritor para un archivo CSV.
     *
     * @param archivoDestino El archivo CSV al que se va a escribir.
     * @param sobrescribir True si se debe sobrescribir el archivo (si existe).
     */
    public EscritorCSV(File archivoDestino, boolean sobrescribir) {
        this.archivoDestino = archivoDestino;

        try {
            this.escritor = new BufferedWriter(
                                    new OutputStreamWriter(
                                            new FileOutputStream(archivoDestino,
                                                                !sobrescribir),
                                            StandardCharsets.UTF_8),
                                    LONGITUD_BUFFER_ESCRITURA);
        } catch (IOException e) {
            // TODO: Manejar con el Logger
            throw new RuntimeException("No se pudo abrir el archivo de salida: " + archivoDestino, e);
        }
    }

    /**
     * Escribe un registro en el archivo CSV.
     *  Es sincronizado para evitar problemas de concurrencia.
     *
     * @param registro El registro a escribir.
     */
    public synchronized void escribeRegistro(RegistroCSV registro) {
        escribeLinea(registro.serializa());
    }

    /**
     * Escribe una linea en el archivo CSV.
     *  Es sincronizado para evitar problemas de concurrencia.
     *
     * @param linea La linea a escribir.
     */
    public synchronized void escribeLinea(String linea) {
        try {
            escritor.write(linea);
            escritor.newLine();
        } catch (IOException e) {
            // TODO: Manejar con el Logger
            throw new RuntimeException("Error al escribir en archivo final: " + archivoDestino, e);
        }
    }

    /**
     * Hace flush en el archivo CSV.
     *  Es sincronizado para evitar problemas de concurrencia.
     *  Hacer flush significa que se escriben todos los datos en el buffer.
     */
    public synchronized void flush() {
        try {
            escritor.flush();
        } catch (IOException e) {
            // TODO: Manejar con el Logger
            throw new RuntimeException("Error al hacer flush en archivo final: " + archivoDestino, e);
        }
    }

    /**
     * Cierra el archivo CSV.
     *  Es sincronizado para evitar problemas de concurrencia.
     *  Sobreescribe el metodo de AutoCloseable.
     */
    @Override
    public synchronized void close() {
        try {
            escritor.close();
        } catch (IOException e) {
            // TODO: Manejar con el Logger
            System.err.println("Error al cerrar el archivo de salida: " + archivoDestino);
        }
    }
}