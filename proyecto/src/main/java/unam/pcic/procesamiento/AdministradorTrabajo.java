package unam.pcic.procesamiento;

import unam.pcic.dominio.CriterioFiltro;
import unam.pcic.dominio.RegistroCSV;
import unam.pcic.io.EscritorCSV;
import unam.pcic.io.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * - Coordina el pool de hilos (HiloDeTrabajo)
 * - Distribuye subarchivos a cadqa hilo de trabajo.
 * - Consolida resultados parciales.
 * - Sincroniza la escritura de resultados.
 */
public class AdministradorTrabajo {

    private final File carpetaTemporal;
    private final CriterioFiltro<RegistroCSV> filtro;
    private final File archivoSalida;


    public AdministradorTrabajo(File carpetaTemporal, CriterioFiltro<RegistroCSV> filtro, File archivoSalida) {
        this.carpetaTemporal = carpetaTemporal;
        this.filtro = filtro;
        this.archivoSalida = archivoSalida;
    }

    public void coordinaHilos() {
        Logger logger = Logger.getInstancia();
        File[] archivosTemporales = carpetaTemporal.listFiles();

        if (archivosTemporales == null || archivosTemporales.length == 0) {
            logger.error("No hay archivos temporales para procesar");
            return;
        }

        List<HiloDeTrabajo> hilos = new ArrayList<>();

        try (EscritorCSV escritorCompartido = new EscritorCSV(archivoSalida, false)) {
            for (File archivoTmp : archivosTemporales) {
                HiloDeTrabajo hilo = new HiloDeTrabajo(archivoTmp, filtro, escritorCompartido);
                hilos.add(hilo);
            }

            for (HiloDeTrabajo hilo : hilos) hilo.start();

            for (HiloDeTrabajo hilo : hilos) {
                try {
                    hilo.join();
                } catch (InterruptedException e) {
                    logger.error("Hilo interrumpido: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            logger.error("Error al coordinar hilos", e);
        }

    }

}
