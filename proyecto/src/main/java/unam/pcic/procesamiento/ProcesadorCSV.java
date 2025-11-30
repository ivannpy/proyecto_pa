package unam.pcic.procesamiento;

import unam.pcic.utilidades.Opciones;


/**
 * Define la interfaz para procesar archivos CSV.
 */
public interface ProcesadorCSV {
    /**
     * Procesa un archivo CSV según las opciones especificadas.
     *
     * @param opciones Las opciones de ejecución
     */
    void procesa(Opciones opciones);
}
