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
        // Usar AdministradorTrabajo para procesar los archivos

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
        // HAcer validaciones (e,g que existan los archivos, etc)
        /**
         * // 1) Construimos el DivisorArchivo SOLO para saber dónde está la carpeta tmp
         *         //    NO volvemos a llamar divide()
         *
         *         File carpetaTemporal = opciones.getCarpetaTemporal();
         *         if (!carpetaTemporal.exists() || !carpetaTemporal.isDirectory()) {
         *             System.out.println("La carpeta temporal no existe: " + carpetaTemporal.getAbsolutePath());
         *             System.out.println("Asegúrate de que el procesador secuencial ya corrió y creó los archivos temporales.");
         *             return;
         *         }
         *
         *         File[] archivosTemporales = carpetaTemporal.listFiles(
         *                 (dir, name) -> name.toLowerCase().endsWith(".csv")
         *         );
         *
         *         if (archivosTemporales == null || archivosTemporales.length == 0) {
         *             System.out.println("No se encontraron archivos temporales en: " + carpetaTemporal.getAbsolutePath());
         *             return;
         *         }
         *
         *         // 2) Obtenemos el filtro seleccionando desde Opciones,
         *         //    igual que se usa en el secuencialHilo
         *         CriterioFiltro<RegistroCSV> filtro = opciones.getCriterioFiltro();
         *
         */

        procesaArchivos(opciones.getCarpetaTemporal(), opciones.getCriterioFiltro());
    }
}
