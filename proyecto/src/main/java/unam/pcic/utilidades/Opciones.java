package unam.pcic.utilidades;

import unam.pcic.dominio.CondicionFiltro;
import unam.pcic.dominio.CriterioFiltro;
import unam.pcic.dominio.RegistroCSV;

import java.io.File;
import java.util.Arrays;
import java.util.List;


public class Opciones {
    /**
     * Indica si se pasaron argumentos al programa.
     */
    private boolean hayArgumentos;

    /**
     * Indica si se puso un archivo csv de entrada.
     */
    private boolean hayArchivo;

    /**
     * Ruta del archivo csv de entrada.
     */
    private String archivo;

    /**
     * Lista de columnas a procesar.
     */
    private int[] columnas;

    /**
     * Filtros a aplicar a las columnas.
     */
    private List<CondicionFiltro<RegistroCSV>> filtros;

    /**
     * Criterio de filtrado de registros
     */
    private CriterioFiltro<RegistroCSV> criterioFiltro;

    /**
     * Indica si se seleccionan todas las columnas.
     */
    private boolean todasLasColumnas = false;

    /**
     * Indica cuantos registros se imprimen en la consola
     */
    private int limiteImpresion;

    /**
     * Cantidad de procesadores disponibles
     */
    private final int cantidadProcesadores;

    /**
     * Cantidad de subarchivos a crear
     */
    private final int cantidadSubarchivos;

    /**
     * La carpeta temporal
     */
    private File carpetaTemporal;

    /**
     * El archivo de entrada
     */
    private File archivoDeEntrada;

    /**
     * El archivo de salida
     */
    private File archivoDeSalida;

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
     * Por defecto se crean el doble de subarchivos que procesadores disponibles.
     */
    public Opciones() {
        this(2);
    }

    /**
     * Regresa la carpeta temporal.
     *
     * @return la carpeta temporal.
     */
    public File getCarpetaTemporal() {
        return carpetaTemporal;
    }

    /**
     * Crea la carpeta temporal a partir de la ruta del archivo de entrada.
     */
    public void setCarpetaTemporal() {
        carpetaTemporal = new File(getArchivoDeEntrada().getParentFile() + File.separator + "tmp");
    }

    /**
     * Regresa el archivo de entrada.
     *
     * @return el archivo de entrada.
     */
    public File getArchivoDeEntrada() {
        return archivoDeEntrada;
    }

    /**
     * Crea el archivo de entrada a partir de la ruta del archivo de entrada.
     */
    public void setArchivoDeEntrada() {
        archivoDeEntrada = new File(getArchivo());
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
     *
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
     * Modifica si hay argumentos al programa.
     *
     * @param hayArgumentos si hay argumentos al programa.
     */
    public void setHayArgumentos(boolean hayArgumentos) {
        this.hayArgumentos = hayArgumentos;
    }

    /**
     * Regresa la ruta del archivo csv de entrada.
     *
     * @return la ruta del archivo csv de entrada.
     */
    public String getArchivo() {
        return archivo;
    }

    public void setArchivo(String archivo) {
        this.archivo = archivo;
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
     * Modifica si hay un archivo csv de entrada.
     *
     * @param hayArchivo si hay un archivo csv de entrada.
     */
    public void setHayArchivo(boolean hayArchivo) {
        this.hayArchivo = hayArchivo;
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
     * Modifica las columnas a procesar.
     *
     * @param columnas las columnas a procesar.
     */
    public void setColumnas(int[] columnas) {
        this.columnas = columnas;
    }

    /**
     * Regresa los filtros a aplicar a las columnas.
     *
     * @return los filtros a aplicar a las columnas.
     */
    public List<CondicionFiltro<RegistroCSV>> getFiltros() {
        return filtros;
    }

    /**
     * Modifica los filtros a aplicar a las columnas.
     *
     * @param filtros los filtros a aplicar a las columnas.
     */
    public void setFiltros(List<CondicionFiltro<RegistroCSV>> filtros) {
        this.filtros = filtros;
    }

    /**
     * Modifica la ruta del archivo csv de entrada.
     *
     * @param archivo la ruta del archivo csv de entrada.
     */
    public void setRutaArchivo(String archivo) {
        this.archivo = archivo;
    }

    public String getRutaArchivo() {
        return archivo;
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
     * Crea un criterio de filtrado a partir de las columnas y los filtros.
     */
    public void setCriterioFiltro() {
        this.criterioFiltro = CriterioFiltro.paraRegistroCSV(getColumnas(), getFiltros());
    }

    /**
     * Regresa el criterio de filtrado.
     *
     * @return el criterio de filtrado.
     */
    public CriterioFiltro<RegistroCSV> getCriterioFiltro() {
        if (criterioFiltro == null) setCriterioFiltro();
        return criterioFiltro;
    }

    public File getArchivoDeSalida() {
        setArchivoDeSalida(new File(carpetaTemporal.getParent() + File.separator + "resultado.csv"));
        return archivoDeSalida;
    }

    public void setArchivoDeSalida(File archivoDeSalida) {
        this.archivoDeSalida = archivoDeSalida;
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
        sb.append(filtros);
        sb.append(", todasLasColumnas=");
        sb.append(todasLasColumnas);
        sb.append(", limiteImpresion=");
        sb.append(limiteImpresion);
        sb.append(')');
        return sb.toString();
    }
}