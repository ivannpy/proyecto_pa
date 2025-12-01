package unam.pcic.procesamiento;

import unam.pcic.dominio.CriterioFiltro;
import unam.pcic.dominio.RegistroCSV;
import unam.pcic.io.EscritorCSV;
import unam.pcic.io.LectorCSV;
import unam.pcic.io.Logger;

import java.io.File;


/**
 * - Extiende a Thread o implementa Runnable.
 * - Procesa un subarchivo específico.
 * - Aplica filtros y selecciona columnas.
 * - Acumula resultados parciales.
 * - Reporta errores al Logger.
 */
public class HiloDeTrabajo extends Thread {
    /**
     * El subarchivo que se va a procesar este hilo
     */
    private final File subarchivo;

    /**
     * El filtro que se va a aplicar a cada registro.
     */
    private final CriterioFiltro<RegistroCSV> filtro;

    /**
     * Escritor compartido entre los hilos.
     */
    private final EscritorCSV escritor;

    /**
     * Constructor.
     * Inicializa un hilo de trabajo.
     *
     * @param subarchivo El subarchivo que se va a procesar este hilo.
     * @param filtro     El filtro que se va a aplicar a cada registro del subarchivo.
     * @param escritor   Escritor compartido entre los hilos para escribir los resultados en el archivo final.
     */
    public HiloDeTrabajo(File subarchivo,
                         CriterioFiltro<RegistroCSV> filtro,
                         EscritorCSV escritor) {
        this.subarchivo = subarchivo;
        this.filtro = filtro;
        this.escritor = escritor;
    }

    /**
     * Método que se ejecuta en cada hilo.
     */
    @Override
    public void run() {
        Logger logger = Logger.getInstancia();

        logger.info("Inicia el thread con archivo parcial " + subarchivo.getAbsolutePath());

        LectorCSV lector = new LectorCSV(subarchivo, true);

        try {
            RegistroCSV registro;

            while ((registro = lector.siguienteRegistro()) != null) {
                boolean cumple = true;

                if (filtro != null) {
                    registro = filtro.seleccionarColumnas(registro);
                    cumple = filtro.aplicarFiltros(registro);
                }

                if (!cumple) continue;

                escritor.escribeRegistro(registro);
            }
        } catch (Exception e) {
            logger.error("Error al cargar archivo parcial" + subarchivo.getAbsolutePath(), e);
            System.exit(1);
        }
    }
}