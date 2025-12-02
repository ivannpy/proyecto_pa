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
 * - Coordina el pool de hilos (HiloDeTrabajo)
 * - Distribuye subarchivos a cadqa hilo de trabajo.
 * - Consolida resultados parciales.
 * - Sincroniza la escritura de resultados.
 */
public class AdministradorTrabajo {
    /**
     * Carpeta temporal donde se encuentran los subarchivos.
     */
    private final File carpetaTemporal;

    /**
     * El criterio de filtrado que aplicará cada hilo sobre los Registros.
     */
    private final CriterioFiltro<RegistroCSV> filtro;

    /**
     * El archivo final.
     */
    private final File archivoSalida;

    private boolean encabezadoEscrito = false;

    /**
     * Constructor.
     * Inicializa un AdministradorTrabajo que coordinar los hilos de trabajo.
     *
     * @param carpetaTemporal Carpeta temporal con los subarchivos que procesará cada hilo.
     * @param filtro          El criterio de filtrado a aplicar a cada registro.
     * @param archivoSalida   El archivo final donde se escriben los resultados.
     */
    public AdministradorTrabajo(File carpetaTemporal, CriterioFiltro<RegistroCSV> filtro, File archivoSalida) {
        this.carpetaTemporal = carpetaTemporal;
        this.filtro = filtro;
        this.archivoSalida = archivoSalida;
    }

    /**
     * Coordina la creación de hilos de trabajo.
     */
    public void coordinaHilos() {
        Logger logger = Logger.getInstancia();
        File[] archivosTemporales = carpetaTemporal.listFiles();

        if (archivosTemporales == null || archivosTemporales.length == 0) {
            logger.error("No hay archivos temporales para procesar");
            return;
        }

        List<HiloDeTrabajo> hilos = new ArrayList<>();

        try (EscritorCSV escritorCompartido = new EscritorCSV(archivoSalida, false)) {
            File archivoParaEncabezado = archivosTemporales[0];
            LectorCSV lector = new LectorCSV(archivoParaEncabezado, true);
            if (!encabezadoEscrito) {
                try {
                    String[] encabezadoArr = lector.leerEncabezado();
                    RegistroCSV encabezado = new RegistroCSV(encabezadoArr, 0L);
                    if (filtro != null) {
                        encabezado = filtro.seleccionarColumnas(encabezado);
                    }
                    escritorCompartido.escribeRegistro(encabezado);
                    encabezadoEscrito = true;
                } catch (Exception e) {
                    logger.error("Error al leer encabezado del archivo: " + archivoParaEncabezado.getName() + " - " + e.getMessage());
                    System.exit(1);
                }
            }
            lector.cerrarLectorSecuencial();

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
