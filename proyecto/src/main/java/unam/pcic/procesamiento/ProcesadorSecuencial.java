package unam.pcic.procesamiento;

import unam.pcic.dominio.CriterioFiltro;
import unam.pcic.dominio.RegistroCSV;
import unam.pcic.io.*;
import unam.pcic.utilidades.Opciones;

import java.io.File;


/**
 * - Implementa ProcesadorCSV.
 * - Lee el archivo completo secuencialmente.
 * - Aplica filtros y selecciona columnas.
 * - Genera un archivo de salida.
 * - Usa la implementación orientada a renglones (RegistroCSV).
 */
public class ProcesadorSecuencial implements ProcesadorCSV {

    /**
     * Procesa un archivo CSV.
     *
     * @param archivo  El archivo a procesar.
     * @param escritor El escritor para el archivo de salida.
     * @param filtro   El criterio de filtrado.
     */
    private void procesaArchivo(File archivo, EscritorCSV escritor, CriterioFiltro<RegistroCSV> filtro) {
        Logger logger = Logger.getInstancia();

        logger.info("Procesando archivo: " + archivo.getName());

        LectorCSV lector = new LectorCSV(archivo, true);

        boolean encabezadoEscrito = true;
        try {
            if (lector.leerEncabezado() == null) {
                encabezadoEscrito = false;
            }
        } catch (Exception e) {
            System.err.println("Error al leer encabezado del archivo: " + archivo.getName());
        }

        try {
            RegistroCSV registro;
            while ((registro = lector.siguienteRegistro()) != null) {
                boolean cumpleFiltros = true;
                if (filtro != null) {
                    registro = filtro.seleccionarColumnas(registro);
                    cumpleFiltros = filtro.aplicarFiltros(registro);
                }

                if (!cumpleFiltros) continue;

                if (!encabezadoEscrito) {
                    escritor.escribeRegistro(new RegistroCSV(lector.leerEncabezado(), 0L));
                }
                escritor.escribeRegistro(registro);
            }
        } catch (Exception e) {
            logger.error("Error al procesar archivo: " + archivo.getName() + " - " + e.getMessage());
            System.exit(1);
        }
    }

    /**
     * Procesa todos los archivos de la carpeta temporal.
     *
     * @param carpetaTemporal La carpeta temporal con los archivos a procesar.
     * @param filtro          El criterio de filtrado.
     */
    private void procesaArchivos(File carpetaTemporal, CriterioFiltro<RegistroCSV> filtro) {
        Logger logger = Logger.getInstancia();

        String rutaArchivoFinal = carpetaTemporal.getParent() + File.separator + "resultado_secuencial.csv";

        File archivoSalida = new File(rutaArchivoFinal);
        logger.info("Archivo de salida: " + archivoSalida.getAbsolutePath());

        File[] archivos = carpetaTemporal.listFiles();

        try (EscritorCSV escritorSalida = new EscritorCSV(archivoSalida, true)) {
            //escritorSalida.escribeRegistro(encabezado);

            for (int i = 0; i < archivos.length; i++) {
                procesaArchivo(archivos[i], escritorSalida, filtro);
            }
            escritorSalida.flush();
        } catch (Exception e) {
            logger.error("Error al procesar archivos temporales: " + e.getMessage());
            System.exit(1);
        }

    }

    /**
     * Hace el procesamiento secuencial.
     *
     * @param opciones Configuraciones para ejecutar el programa.
     */
    public void procesa(Opciones opciones) {
        procesaArchivos(opciones.getCarpetaTemporal(), opciones.getCriterioFiltro());
    }
}
