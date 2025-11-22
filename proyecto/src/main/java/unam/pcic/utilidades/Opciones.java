package unam.pcic.utilidades;

import java.util.Arrays;

public class Opciones {
    /** Indica si se pasaron argumentos al programa. */
    private boolean hayArgumentos = false;

    /** Indica si se puso un archivo csv de entrada. */
    private boolean hayArchivo = false;

    /** Ruta del archivo csv de entrada. */
    private String archivo = null;

    /** Lista de columnas a procesar. */
    private int[] columnas = null;

    /** Filtros a aplicar a las columnas. */
    private String[] filtros = null;

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
     * @param hayArgumentos si hay argumentos al programa.
     */
    public void setHayArgumentos(boolean hayArgumentos) {
        this.hayArgumentos = hayArgumentos;
    }

    /**
     * Modifica la ruta del archivo csv de entrada.
     * @param archivo la ruta del archivo csv de entrada.
     */
    public void setArchivo(String archivo) {
        this.archivo = archivo;
    }

    /**
     * Modifica si hay un archivo csv de entrada.
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
     * Modifica los filtros a aplicar a las columnas.
     * @param filtros los filtros a aplicar a las columnas.
     */
    public void setFiltros(String[] filtros) {
        this.filtros = filtros;
    }

    /**
     * {@inheritDoc}
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
        sb.append(')');
        return sb.toString();
    }
}