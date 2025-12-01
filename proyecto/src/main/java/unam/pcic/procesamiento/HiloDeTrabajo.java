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

    private final File archivoParcial;
    private final CriterioFiltro<RegistroCSV> filtro;
    private final EscritorCSV escritor;

    public HiloDeTrabajo(File archivoParcial,
                         CriterioFiltro<RegistroCSV> filtro,
                         EscritorCSV escritor) {
        this.archivoParcial = archivoParcial;
        this.filtro = filtro;
        this.escritor = escritor;
    }

    @Override
    public void run() {
        Logger logger = Logger.getInstancia();

        logger.info("Inicia el thread con archivo parcial " + archivoParcial.getAbsolutePath());

        LectorCSV lector = new LectorCSV(archivoParcial, true);

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
            logger.error("Error al cargar archivo parcial" + archivoParcial.getAbsolutePath(), e);
            System.exit(1);
        }
    }
}