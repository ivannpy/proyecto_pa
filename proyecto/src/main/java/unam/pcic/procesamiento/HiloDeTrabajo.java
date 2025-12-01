package unam.pcic.procesamiento;

import unam.pcic.dominio.CriterioFiltro;
import unam.pcic.dominio.RegistroCSV;
import unam.pcic.io.EscritorCSV;
import unam.pcic.io.LectorCSV;
import unam.pcic.io.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * - Extiende a Thread o implementa Runnable.
 * - Procesa un subarchivo específico.
 * - Aplica filtros y selecciona columnas.
 * - Acumula resultados parciales.
 * - Reporta errores al Logger.
 */
public class HiloDeTrabajo extends Thread {

    private final List<RegistroCSV> resultadoParcial = new ArrayList<>();
    private final File archivoParcial;
    private final CriterioFiltro<RegistroCSV> filtro;

    public HiloDeTrabajo(File archivoParcial,
                         CriterioFiltro<RegistroCSV> filtro) {
        this.archivoParcial = archivoParcial;
        this.filtro = filtro;
    }

    @Override
    public void run() {
        Logger logger = Logger.getInstancia();

        logger.debug("Inicia el thread con archivo parcial " + archivoParcial.getAbsolutePath());

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

                resultadoParcial.add(registro);
            }

        } catch (Exception e) {
            logger.error("Error al cargar archivo parcial" + archivoParcial.getAbsolutePath(), e);
            System.exit(1);
        }
    }

    public void escribirResultadoParcial(File archivo) {
        try (EscritorCSV escritor = new EscritorCSV(archivo, true)) {
            for (RegistroCSV registro : resultadoParcial) {
                escritor.escribeRegistro(registro);
            }
        }


    }
}