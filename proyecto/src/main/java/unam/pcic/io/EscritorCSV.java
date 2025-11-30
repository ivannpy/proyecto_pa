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

    private static final int WRITE_BUFFER_SIZE = 2 * 1024 * 1024;
    private final File archivoDestino;
    private final BufferedWriter writer;

    public EscritorCSV(File archivoDestino, boolean sobrescribir) {
        this.archivoDestino = archivoDestino;
        try {
            this.writer = new BufferedWriter(
                                new OutputStreamWriter(
                                        new FileOutputStream(archivoDestino,
                                                            !sobrescribir), // append si !sobrescribir
                                        StandardCharsets.UTF_8),
                                WRITE_BUFFER_SIZE);
        } catch (IOException e) {
            throw new RuntimeException("No se pudo abrir el archivo de salida: " + archivoDestino, e);
        }
    }

    public synchronized void escribeRegistro(RegistroCSV registro) {
        escribeLinea(registro.serializa());
    }

    public synchronized void escribeLinea(String linea) {
        try {
            writer.write(linea);
            writer.newLine();
        } catch (IOException e) {
            throw new RuntimeException("Error al escribir en archivo final: " + archivoDestino, e);
        }
    }

    public synchronized void flush() {
        try {
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException("Error al hacer flush en archivo final: " + archivoDestino, e);
        }
    }

    @Override
    public synchronized void close() {
        try {
            writer.close();
        } catch (IOException e) {
            System.err.println("Error al cerrar el archivo de salida: " + archivoDestino);
        }
    }
}