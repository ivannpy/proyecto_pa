package unam.pcic.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * - Escribe líneas en múltiples archivos CSV de forma eficiente.
 * - Mantiene un BufferedWriter abierto por cada archivo.
 * - Se encarga de abrir, hacer flush y cerrar todos los writers.
 */
public class EscritorCSVMultiple implements AutoCloseable {
    /**
     * Tamaño del buffer de escritura
     */
    private static final int WRITE_BUFFER_SIZE = 2 * 1024 * 1024;

    /**
     * Writers asociados a cada archivo temporal
     */
    private final BufferedWriter[] writers;

    /**
     * Archivos temporales administrados por este escritor
     */
    private final List<File> archivos;

    /**
     * Crea un escritor para múltiples archivos.
     *
     * @param archivos Lista de archivos en los que se escribirá.
     */
    public EscritorCSVMultiple(List<File> archivos) {
        this.archivos = archivos;
        this.writers = new BufferedWriter[archivos.size()];
        inicializarWriters();
    }

    private void inicializarWriters() {
        try {
            for (int i = 0; i < archivos.size(); i++) {
                File archivo = archivos.get(i);
                this.writers[i] = new BufferedWriter(
                                        new OutputStreamWriter(
                                                new FileOutputStream(archivo, false),
                                                StandardCharsets.UTF_8),
                                        WRITE_BUFFER_SIZE);
            }
        } catch (IOException e) {
            cerrar();
            throw new RuntimeException("Error al inicializar writers para archivos temporales", e);
        }
    }

    /**
     * Escribe una línea en el archivo correspondiente al índice.
     *
     * @param indiceArchivo Índice del archivo (0 <= indice < archivos.size()).
     * @param linea         Línea CSV a escribir (sin salto de línea).
     */
    public void escribeLineaEn(int indiceArchivo, String linea) {
        if (indiceArchivo < 0 || indiceArchivo >= writers.length) {
            throw new IndexOutOfBoundsException("Índice de archivo inválido: " + indiceArchivo);
        }
        BufferedWriter writer = writers[indiceArchivo];
        try {
            writer.write(linea);
            writer.newLine();
        } catch (IOException e) {
            throw new RuntimeException("Error al escribir en archivo: " + archivos.get(indiceArchivo), e);
        }
    }

    /**
     * Escribe la misma línea (por ejemplo, encabezado) en todos los archivos.
     *
     * @param lineaEncabezado Línea de encabezado (sin salto de línea).
     */
    public void escribeEncabezadoEnTodos(String lineaEncabezado) {
        for (int i = 0; i < writers.length; i++) {
            escribeLineaEn(i, lineaEncabezado);
        }
    }

    /**
     * Fuerza el flush de todos los writers.
     */
    public void flush() {
        for (BufferedWriter writer : writers) {
            if (writer != null) {
                try {
                    writer.flush();
                } catch (IOException e) {
                    throw new RuntimeException("Error al hacer flush de un writer CSV", e);
                }
            }
        }
    }

    private void cerrar() {
        for (BufferedWriter writer : writers) {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException ignore) {
                }
            }
        }
    }

    /**
     * Cierra todos los writers.
     */
    @Override
    public void close() {
        cerrar();
    }
}