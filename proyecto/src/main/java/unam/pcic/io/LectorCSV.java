package unam.pcic.io;

import unam.pcic.dominio.RegistroCSV;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


/**
 * - Lee el archivo CSV línea por línea.
 * - Parsea los encabezados y convierte cada línea en un RegistroCSV.
 * - Maneja encoding y diferentes delimitadores.
 * - Convierte una linea en un array de Strings.
 */
public class LectorCSV {
    /** Delimitador por defecto */
    private static final String DELIMITADOR_POR_DEFECTO = ",";

    /** Codificación por defecto */
    private static final String CODIFICACION_POR_DEFECTO = "utf-8";

    /** Archivo a leer */
    private final File archivo;

    /** Delimitador a utilizar */
    private final String delimitador;

    /** Codificación a utilizar */
    private final String codificacion;

    /** Indica si el archivo CSV tiene un encabezado */
    private final boolean tieneEncabezado;

    /** El encabezado del archivo CSV */
    private String[] encabezado;

    /** Cantidad de columnas del archivo CSV */
    private int cantidadColumnas;

    /**
     * Construye un LectorCSV usando el delimitador y la codificación por defecto.
     *
     * @param archivo El archivo a leer.
     * @param tieneEncabezado Indica si el archivo tiene encabezado.
     */
    public LectorCSV(File archivo, boolean tieneEncabezado) {
        this(archivo, DELIMITADOR_POR_DEFECTO, CODIFICACION_POR_DEFECTO, tieneEncabezado);
    }

    /**
     * Construye un LectorCSV.
     *
     * @param archivo El archivo a leer.
     * @param delimitador El delimitador a utilizar.
     * @param codificacion La codificación a utilizar.
     * @param tieneEncabezado Indica si el archivo tiene encabezado.
     */
    public LectorCSV(File archivo, String delimitador, String codificacion, boolean tieneEncabezado) {
        if (archivo == null || !archivo.exists() || !archivo.isFile()) {
            throw new IllegalArgumentException("El archivo no existe o no es un archivo");
        }
        if (delimitador == null || delimitador.isEmpty()) {
            throw new IllegalArgumentException("El delimitador no puede ser null o vacío");
        }
        if (codificacion == null || codificacion.isEmpty()) {
            throw new IllegalArgumentException("El encoding no puede ser null o vacío");
        }

        this.archivo = archivo;
        this.delimitador = delimitador;
        this.codificacion = codificacion;
        this.tieneEncabezado = tieneEncabezado;
    }

    /**
     * Lee el encabezado del archivo CSV.
     *
     * @return El encabezado del archivo CSV.
     * @throws Exception Si ocurre un error al leer el archivo.
     */
    public String[] leerEncabezado() throws Exception {
        if (!tieneEncabezado) {
            return null;
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(archivo), codificacion))) {
            String linea = br.readLine();
            if (linea != null) {
                encabezado = parsearLinea(linea);
            }
            cantidadColumnas = encabezado.length;
            return encabezado;
        }
    }

    /**
     * Parsea una línea del archivo CSV y devuelve un array de Strings con los valores.
     *
     * @param linea La linea a parsear.
     * @return Un arreglo de Strings con los valores de la linea.
     */
    private String[] parsearLinea(String linea) {
        List<String> resultado = new ArrayList<>();
        StringBuilder campoActual = new StringBuilder();
        boolean entreComillas = false;

        for (int i = 0; i < linea.length(); i++) {
            char c = linea.charAt(i);

            if (c == '"') {
                if (entreComillas && i + 1 < linea.length() && linea.charAt(i + 1) == '"') {
                    campoActual.append('"');
                    i++;
                } else {
                    entreComillas = !entreComillas;
                }
            } else if (c == ',' && !entreComillas) {
                resultado.add(campoActual.toString());
                campoActual = new StringBuilder();
            } else {
                campoActual.append(c);
            }
        }
        resultado.add(campoActual.toString());

        return resultado.toArray(new String[0]);
    }

    /**
     * Lee el archivo CSV completo y devuelve una lista de registros.
     *
     * @return Una lista de registros.
     * @throws Exception Si ocurre un error al leer el archivo.
     */
    public List<RegistroCSV> leer() throws Exception {
        List<RegistroCSV> registros = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(archivo), codificacion))) {
            String linea;
            Long numeroLinea = 0L;

            if (tieneEncabezado) {
                br.readLine();
                numeroLinea++;
            }

            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) {
                    numeroLinea++;
                    continue;
                }

                String[] valores = parsearLinea(linea);

                if (valores.length != cantidadColumnas) {
                    numeroLinea++;
                    continue;
                }

                registros.add(new RegistroCSV(valores, numeroLinea));
                numeroLinea++;

                if (numeroLinea == 5_000_000) {
                    break;
                }
            }
        }
        return registros;
    }
}
