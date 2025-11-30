package unam.pcic.analisis;

import unam.pcic.utilidades.Opciones;

/**
 * - Mide tiempos de ejecución
 * - Compara la versión secuencial y la concurrente
 * - Genera datos para graficar (e.g tiempo vs número de hilos)
 */
public class AnalizadorRendimiento {
    /**
     * Cantidad de procesadores (hilos) disponibles
     */
    final int cantidadDeProcesadores;

    /**
     * Cantidad de subarchivos creados
     */
    final int cantidadDeSubarchivos;

    /**
     * Tiempo transcurrido en nanosegundos
     */
    long tiempoTranscurrido;

    /**
     * Tiempo inicial en nanosegundos
     */
    long tiempoInicial;

    /**
     * Tiempo final en nanosegundos
     */
    long tiempoFinal;

    /**
     * Constructor.
     * Crea un analizador de rendimiento según las opciones de ejecución.
     *
     * @param opciones Las opciones de ejecución
     */
    public AnalizadorRendimiento(Opciones opciones) {
        this.cantidadDeProcesadores = opciones.getCantidadProcesadores();
        this.cantidadDeSubarchivos = opciones.getCantidadSubarchivos();
    }

    /**
     * Regresa el tiempo transcurrido en segundos.
     *
     * @return El tiempo transcurrido en segundos.
     */
    public long getTiempoTranscurrido() {
        return tiempoTranscurrido / 1_000_000_000;
    }

    /**
     * Regresa el tiempo transcurrido dividido entre la cantidad de procesadores.
     *
     * @return El tiempo transcurrido por hilo en segundos.
     */
    public long getTiempoPorCantidadDeProcesadores() {
        return getTiempoTranscurrido() / cantidadDeProcesadores;
    }

    /**
     * Regresa el tiempo transcurrido entre la cantidad de subarchivos.
     *
     * @return El tiempo transcurrido en segundos.
     */
    public long getTiempoPorArchivo() {
        return getTiempoTranscurrido() / cantidadDeSubarchivos;
    }

    /**
     * Inicia el contador de tiempo.
     */
    public void iniciar() {
        tiempoInicial = System.nanoTime();
    }

    /**
     * Finaliza el contador de tiempo y calcula el tiempo transcurrido.
     */
    public void finalizar() {
        tiempoFinal = System.nanoTime();
        tiempoTranscurrido = tiempoFinal - tiempoInicial;
    }
}
