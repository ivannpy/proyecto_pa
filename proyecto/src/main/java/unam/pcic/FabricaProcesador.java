package unam.pcic;

import unam.pcic.procesamiento.ProcesadorCSV;
import unam.pcic.procesamiento.ProcesadorConcurrente;
import unam.pcic.procesamiento.ProcesadorSecuencial;

/**
 * - Crea instancias de ProcesadorCSV según sea secuencial o concurrente usando el patrón Factory.
 * - Inyecta dependencias.
 */
public class FabricaProcesador {
    /**
     * Regresa una forma de procesamiento.
     *
     * @param tipoProcesamiento El tipo de procesamiento (secuencial o concurrente).
     * @return Una forma de procesamiento.
     */
    public static ProcesadorCSV crearProcesador(String tipoProcesamiento) {
        switch (tipoProcesamiento) {
            case "secuencial":
                return new ProcesadorSecuencial();
            case "concurrente":
                return new ProcesadorConcurrente();
            default:
                return null;
        }
    }
}
