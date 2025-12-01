package unam.pcic.utilidades;

import java.io.File;

/**
 * - Validaciones de entrada de datos.
 * - Verifica formatos de archivos.
 * - Verifica banderas de entrada (o del menu interactivo)
 * - En caso de que algo no sea correcto, usar el Logger para registrar el error.
 */
public class Validaciones {

    public static boolean existeCarpeta(File carpeta) {
        return carpeta.exists();
    }
}
