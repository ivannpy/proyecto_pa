package unam.pcic.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * - Escribe líneas en múltiples archivos CSV.
 * - Mantiene un BufferedWriter abierto por cada archivo.
 * - Se encarga de abrir, hacer flush y cerrar todos los writers.
 */
public class EscritorCSVMultiple implements AutoCloseable {
    /**
     * Tamaño del buffer de escritura
     */
    private static final int LONGITUD_BUFFER_ESCRITURA = 2 * 1024 * 1024;

    /**
     * Arreglo de escritores asociados a cada archivo temporal
     */
    private final BufferedWriter[] escritores;

    /**
     * Archivos temporales en los que se escribirá
     */
    private final List<File> archivos;

    /**
     * Constructor.
     * Crea un escritor para múltiples archivos.
     *
     * @param archivos Lista de archivos en los que se escribirá.
     */
    public EscritorCSVMultiple(List<File> archivos) {
        this.archivos = archivos;
        this.escritores = new BufferedWriter[archivos.size()];
        inicializarEscritores();
    }

    /**
     * Inicializa los escritores.
     * Para cada archivo temporal, crea un BufferedWriter para el archivo.
     */
    private void inicializarEscritores() {
        try {
            for (int i = 0; i < archivos.size(); i++) {
                File archivo = archivos.get(i);
                this.escritores[i] = new BufferedWriter(
                        new OutputStreamWriter(new FileOutputStream(archivo, false), StandardCharsets.UTF_8),
                        LONGITUD_BUFFER_ESCRITURA);
            }
        } catch (IOException e) {
            // TODO: Manejar con el Logger
            cerrar();
            throw new RuntimeException("Error al inicializar escritores para archivos temporales", e);
        }
    }

    /**
     * Escribe una línea en el archivo correspondiente al índice.
     *
     * @param indiceArchivo Índice del archivo en el que se va a escribir.
     * @param linea         Línea a escribir.
     */
    public void escribeLinea(int indiceArchivo, String linea) {
        BufferedWriter escritor = escritores[indiceArchivo];
        try {
            escritor.write(linea);
            escritor.newLine();
        } catch (IOException e) {
            // TODO: Manejar con el Logger
            throw new RuntimeException("Error al escribir en archivo: " + archivos.get(indiceArchivo), e);
        }
    }

    /**
     * Escribe la misma línea en todos los archivos administrados por este escritor.
     *
     * @param linea La linea a escribir.
     */
    public void escribeEnTodos(String linea) {
        for (int i = 0; i < escritores.length; i++) {
            escribeLinea(i, linea);
        }
    }

    /**
     * Hace flush a todos los escritores.
     *  Hacer flush significa que se escriben todos los datos en el buffer.
     */
    public void flush() {
        for (BufferedWriter escritor : escritores) {
            if (escritor != null) {
                try {
                    escritor.flush();
                } catch (IOException e) {
                    // TODO: Manejar con el Logger
                    throw new RuntimeException("Error al hacer flush de un writer CSV", e);
                }
            }
        }
    }

    /**
     * Cierra todos los escritores.
     */
    private void cerrar() {
        for (BufferedWriter escritor : escritores) {
            if (escritor != null) {
                try {
                    escritor.close();
                } catch (IOException ignore) {
                    // TODO: Manejar con el Logger
                }
            }
        }
    }

    /**
     * Sobreescribe el metodo de AutoCloseable.
     */
    @Override
    public void close() {
        cerrar();
    }
}