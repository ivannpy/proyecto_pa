package unam.pcic.analisis;

import unam.pcic.utilidades.Opciones;

/**
 * - Mide tiempos de ejecución
 * - Compara la versión secuencial y la concurrente
 * - Genera datos para graficar (e.g tiempo vs número de hilos)
 */
public class AnalizadorRendimiento {
    final int cantidadDeProcesadores;
    final int cantidadDeSubarchivos;
    long tiempoTranscurrido;
    long tiempoInicial;
    long tiempoFinal;

    public AnalizadorRendimiento(Opciones opciones) {
        this.cantidadDeProcesadores = opciones.getCantidadProcesadores();
        this.cantidadDeSubarchivos = opciones.getCantidadSubarchivos();
    }

    public long getTiempoTranscurrido() {
        return tiempoTranscurrido;
    }

    public long getTiempoPorCantidadDeProcesadores() {
        return tiempoTranscurrido / cantidadDeProcesadores;
    }

    public long getTiempoPorArchivo() {
        return tiempoTranscurrido / cantidadDeSubarchivos;
    }

    public void iniciar() {
        tiempoInicial = System.nanoTime();
    }

    public void finalizar() {
        tiempoFinal = System.nanoTime();
        tiempoTranscurrido = tiempoFinal - tiempoInicial;
    }
}
