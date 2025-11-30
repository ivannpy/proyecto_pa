package unam.pcic.utilidades;

import java.util.Arrays;


public class Opciones {
    /** Indica si se pasaron argumentos al programa. */
    private boolean hayArgumentos;

    /** Indica si se puso un archivo csv de entrada. */
    private boolean hayArchivo;

    /** Ruta del archivo csv de entrada. */
    private String archivo;

    /** Lista de columnas a procesar. */
    private int[] columnas;

    /** Filtros a aplicar a las columnas. */
    private String[] filtros;

    /** Indica si se seleccionan todas las columnas. */
    private boolean todasLasColumnas = false;

    /** Indica cuantos registros se imprimen en la consola */
    private int limiteImpresion;

    /** Cantidad de procesadores disponibles */
    private final int cantidadProcesadores;

    /** Cantidad de subarchivos a crear */
    private final int cantidadSubarchivos;


    /**
     * Constructor para fijar la cantidad de subarchivos a crear.
     *
     * @param multiplo Factor para calcular la cantidad de subarchivos.
     */
    public Opciones(int multiplo) {
        this.cantidadProcesadores = Runtime.getRuntime().availableProcessors();
        this.cantidadSubarchivos = cantidadProcesadores * multiplo;
    }

    /**
     * Constructor por defecto.
     *  Por defecto se crean el doble de subarchivos que procesadores disponibles.
     */
    public Opciones() {
        this(2);
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
     * Regresa la cantidad de procesadores disponibles.
     * @return la cantidad de procesadores disponibles.
     */
    public int getCantidadProcesadores() {
        return cantidadProcesadores;
    }

    /**
     * Regresa si se pasaron argumentos al programa.
     *
     * @return <code>true</code> si se pasaron argumentos al programa;
     */
    public boolean getHayArgumentos() {
        return hayArgumentos;
    }

    /**
     * Regresa la ruta del archivo csv de entrada.
     *
     * @return la ruta del archivo csv de entrada.
     */
    public String getArchivo() {
        return archivo;
    }

    /**
     * Regresa si hay un archivo csv de entrada.
     *
     * @return <code>true</code> si hay un archivo csv de entrada;
     */
    public boolean getHayArchivo() {
        return hayArchivo;
    }

    /**
     * Regresa las columnas a procesar.
     *
     * @return las columnas a procesar.
     */
    public int[] getColumnas() {
        return columnas;
    }

    /**
     * Regresa los filtros a aplicar a las columnas.
     *
     * @return los filtros a aplicar a las columnas.
     */
    public String[] getFiltros() {
        return filtros;
    }

    /**
     * Modifica si hay argumentos al programa.
     *
     * @param hayArgumentos si hay argumentos al programa.
     */
    public void setHayArgumentos(boolean hayArgumentos) {
        this.hayArgumentos = hayArgumentos;
    }

    /**
     * Modifica la ruta del archivo csv de entrada.
     *
     * @param archivo la ruta del archivo csv de entrada.
     */
    public void setArchivo(String archivo) {
        this.archivo = archivo;
    }

    /**
     * Modifica si hay un archivo csv de entrada.
     *
     * @param hayArchivo si hay un archivo csv de entrada.
     */
    public void setHayArchivo(boolean hayArchivo) {
        this.hayArchivo = hayArchivo;
    }

    /**
     * Modifica las columnas a procesar.
     *
     * @param columnas las columnas a procesar.
     */
    public void setColumnas(int[] columnas) {
        this.columnas = columnas;
    }

    /**
     * Modifica si se seleccionan todas las columnas.
     *
     * @param todasLasColumnas Si se seleccionan todas las columnas.
     */
    public void setTodasLasColumnas(boolean todasLasColumnas) {
        this.todasLasColumnas = todasLasColumnas;
    }

    /**
     * Regresa si se seleccionan todas las columnas.
     *
     * @return si se seleccionan todas las columnas.
     */
    public boolean getTodasLasColumnas() {
        return todasLasColumnas;
    }

    /**
     * Modifica los filtros a aplicar a las columnas.
     *
     * @param filtros los filtros a aplicar a las columnas.
     */
    public void setFiltros(String[] filtros) {
        this.filtros = filtros;
    }

    /**
     * Modifica el limite de registros a imprimir.
     *
     * @param limiteImpresion cuantos registros se imprimen en la consola
     */
    public void setLimiteImpresion(int limiteImpresion) {
        this.limiteImpresion = limiteImpresion;
    }

    /**
     * Regresa el limite de registros a imprimir.
     *
     * @return el limite de registros a imprimir.
     */
    public int getLimiteImpresion() {
        return limiteImpresion;
    }

    /**
     * Representación en cadena de las opciones.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Opciones(hayArgumentos=");
        sb.append(hayArgumentos);
        sb.append(", archivo=");
        sb.append(archivo);
        sb.append(", columnas=");
        sb.append(Arrays.toString(columnas));
        sb.append(", filtros=");
        sb.append(Arrays.toString(filtros));
        sb.append(", todasLasColumnas=");
        sb.append(todasLasColumnas);
        sb.append(", limiteImpresion=");
        sb.append(limiteImpresion);
        sb.append(')');
        return sb.toString();
    }
}