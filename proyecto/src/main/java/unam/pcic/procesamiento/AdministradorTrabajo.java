package unam.pcic.procesamiento;

import unam.pcic.dominio.CriterioFiltro;
import unam.pcic.dominio.RegistroCSV;

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
    private List<RegistroCSV> registrosFinales;
    private final File archivoSalida;

    public AdministradorTrabajo(File carpetaTemporal, CriterioFiltro<RegistroCSV> filtro, File archivoSalida) {
        this.carpetaTemporal = carpetaTemporal;
        this.filtro = filtro;
        this.archivoSalida = archivoSalida;
    }

    public List<RegistroCSV> getRegistrosFinales() {
        return registrosFinales;
    }

    public void coordinaHilos(){
        File[] archivosTemporales = carpetaTemporal.listFiles();

        // validar que archivosTemporales no sea vacio o null

        List<HiloDeTrabajo> hilos = new ArrayList<>();
        for (File archivoTmp : archivosTemporales) {
            HiloDeTrabajo hilo = new HiloDeTrabajo(archivoTmp, filtro);
            hilos.add(hilo);
        }

        // 4) Arrancamos todos los hilos
        for (HiloDeTrabajo hilo : hilos) {
            hilo.start();
        }

        // 5) SINCRONIZACIÓN: esperamos a que terminen (join)
        for (HiloDeTrabajo hilo : hilos) {
            try {
                hilo.join();
            } catch (InterruptedException e) {
                System.err.println("Hilo interrumpido: " + e.getMessage());
            }
        }

        for (HiloDeTrabajo hilo : hilos) {
            hilo.escribirResultadoParcial(archivoSalida);
        }

    }
}
