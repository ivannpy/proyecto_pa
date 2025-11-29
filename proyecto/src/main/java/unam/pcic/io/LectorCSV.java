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

    /** Lector para lectura secuencial tipo "generador" */
    private BufferedReader lectorSecuencial;

    /** Indica si el lector secuencial ya fue inicializado */
    private boolean lectorSecuencialInicializado = false;

    /** Número de línea correspondiente a la lectura secuencial */
    private long numeroLineaSecuencial = 0L;

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
    public List<RegistroCSV> leerTodo() throws Exception {
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

            }
        }
        return registros;
    }

    // ************ Lector secuencial, sin leer el csv completo a memoria ************

    /**
     * Inicializa el lector secuencial si aún no ha sido creado.
     * Si el archivo tiene encabezado, lo consume y no lo devuelve en nextLine().
     */
    private void inicializarLectorSecuencial() throws Exception {
        if (lectorSecuencialInicializado) return;

        lectorSecuencial = new BufferedReader(new InputStreamReader(new FileInputStream(archivo), codificacion));
        numeroLineaSecuencial = 0L;

        if (tieneEncabezado) {
            String lineaEncabezado = lectorSecuencial.readLine();
            if (lineaEncabezado != null) {
                numeroLineaSecuencial++;
                // Si aún no tenemos encabezado/cantidadColumnas, las calculamos aquí
                if (encabezado == null) {
                    encabezado = parsearLinea(lineaEncabezado);
                    cantidadColumnas = encabezado.length;
                }
            }
        }

        lectorSecuencialInicializado = true;
    }

    public RegistroCSV siguienteRegistro() throws Exception {
        inicializarLectorSecuencial();

        String linea;
        while ((linea = lectorSecuencial.readLine()) != null) {
            numeroLineaSecuencial++;

            if (linea.trim().isEmpty()) {
                continue;
            }

            String[] valores = parsearLinea(linea);

            if (valores.length != cantidadColumnas) {
                // Registro inválido (no se pudo parsear bien)
                RegistroCSV registroIncorrecto = new RegistroCSV(valores, numeroLineaSecuencial);
                System.out.println("Registro invalido" + registroIncorrecto);
                continue;
            }

            return new RegistroCSV(valores, numeroLineaSecuencial);
        }

        cerrarLectorSecuencial();
        return null;
    }

    /**
     * Cierra el lector secuencial si está abierto.
     *
     * @throws Exception Si ocurre un error al cerrar.
     */
    private void cerrarLectorSecuencial() throws Exception {
        if (lectorSecuencial != null) {
            lectorSecuencial.close();
            lectorSecuencial = null;
            lectorSecuencialInicializado = false;
        }
    }

}
