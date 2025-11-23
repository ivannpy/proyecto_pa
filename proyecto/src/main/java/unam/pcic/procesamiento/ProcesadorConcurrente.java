package unam.pcic.procesamiento;

import unam.pcic.utilidades.Opciones;

/**
 * - Usa el DividorArchivo para dividir el archivo en subarchivos de entrada.
 * - Instancia un AdministradorTrabajo para coordinar el pool de hilos.
 * - Espera a que todos los hilos de trabajo terminen y consolida los resultados.
 * - Limpia los subarchivos temporales.
 * - Usa la implementación orientada a columnas.
 */
public class ProcesadorConcurrente implements ProcesadorCSV {
    /**
     * Hace el procesamiento concurrente.
     *
     * @param opciones Configuraciones para ejecutar el programa.
     */
    public void procesa(Opciones opciones) {
    }
}
