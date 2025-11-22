package unam.pcic.io;

import unam.pcic.dominio.RegistroCSV;

import java.io.File;
import java.io.Writer;

/**
 * - Escribe los resultados filtrados a un archivo de salida CSV.
 * - Es Threading-Safe (usa sincronización para el acceso a la lista de registros y para la escritora)
 * - Acumula los resultados de múltiples hilos.
 * - Debe usar un Buffer interno para eficientemente escribir los registros.
 */
public class EscritorCSV {

    public static void escribeRegistro(RegistroCSV registro, File archivo) {

    }
}
