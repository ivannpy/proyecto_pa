package unam.pcic.procesamiento;

import unam.pcic.dominio.CriterioFiltro;
import unam.pcic.dominio.RegistroCSV;
import unam.pcic.utilidades.Opciones;

import java.io.File;


/**
 * - Usa el DividorArchivo para dividir el archivo en subarchivos de entrada.
 * - Instancia un AdministradorTrabajo para coordinar el pool de hilos.
 * - Espera a que todos los hilos de trabajo terminen y consolida los resultados.
 * - Limpia los subarchivos temporales.
 * - Usa la implementación orientada a columnas.
 */
public class ProcesadorConcurrente implements ProcesadorCSV {

    private void procesaArchivos(File carpetaTemporal, CriterioFiltro<RegistroCSV> filtro) {
        String rutaArchivoFinal = carpetaTemporal.getParent() + File.separator + "resultado_concurrente.csv";
        File archivoSalida = new File(rutaArchivoFinal);

        AdministradorTrabajo administrador = new AdministradorTrabajo(carpetaTemporal, filtro, archivoSalida);
        administrador.coordinaHilos();
    }

    /**
     * Hace el procesamiento concurrente.
     *
     * @param opciones Configuraciones para ejecutar el programa.
     */
    @Override
    public void procesa(Opciones opciones) {
        procesaArchivos(opciones.getCarpetaTemporal(), opciones.getCriterioFiltro());
    }
}
