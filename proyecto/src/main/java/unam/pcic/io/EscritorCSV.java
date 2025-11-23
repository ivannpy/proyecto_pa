package unam.pcic.io;

import unam.pcic.dominio.RegistroCSV;
import java.io.File;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;


/**
 * - Escribe los resultados filtrados a un archivo de salida CSV.
 * - Es Threading-Safe (usa sincronización para el acceso a la lista de registros y para la escritora)
 * - Acumula los resultados de múltiples hilos.
 * - Debe usar un Buffer interno para eficientemente escribir los registros.
 */
public class EscritorCSV {

    /**
     * Escribe un registroCSV en el archivo dado.
     *
     * @param registro El registro a escribir.
     * @param archivo El archivo donde se va a escribir el registro.
     */
    public static void escribeRegistro(RegistroCSV registro, File archivo) {
        // Aplicar filtros, seleccionar columnas y limpiar los datos.
        escribeLinea(registro.serializa(), archivo);
    }

    /**
     * Escribe una línea en el archivo dado.
     *
     * @param linea La linea separada por comas.
     * @param archivo El archivo donde se va a escrbir la línea.
     */
    public static void escribeLinea(String linea, File archivo) {
        synchronized (EscritorCSV.class) {
            try (BufferedWriter writer = Files.newBufferedWriter(
                    archivo.toPath(),
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND))
            {
                writer.write(linea);
                writer.newLine();
            } catch (IOException e) {
                throw new RuntimeException("Error al escribir en el archivo CSV: " + archivo, e);
            }
        }
    }

}
