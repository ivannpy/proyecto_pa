package unam.pcic.procesamiento;

import unam.pcic.dominio.CriterioFiltro;
import unam.pcic.dominio.RegistroCSV;
import unam.pcic.io.AdminArchivosTmp;
import unam.pcic.io.DivisorArchivo;
import unam.pcic.io.EscritorCSV;
import unam.pcic.io.LectorCSV;
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
     * @param filtro El criterio de filtrado.
     */
    private void procesaArchivo(File archivo, EscritorCSV escritor, CriterioFiltro<RegistroCSV> filtro) {
        System.out.println("Procesando archivo: " + archivo.getName());

        LectorCSV lector = new LectorCSV(archivo, true);

        try {
            RegistroCSV registro;
            while ((registro = lector.siguienteRegistro()) != null) {
                // Aplicar seleccion de columnas
                boolean cumpleFiltros = true;
                if (filtro != null) {
                    registro = filtro.seleccionarColumnas(registro);
                    cumpleFiltros = filtro.aplicarFiltros(registro);
                }

                if (!cumpleFiltros) continue;

                escritor.escribeRegistro(registro);
            }
        } catch (Exception e) {
            // TODO: Manejar con el Logger
            System.out.println("Error al procesar archivo: " + archivo.getName());
        }
    }

    /**
     * Procesa todos los archivos de la carpeta temporal.
     *
     * @param carpetaTemporal La carpeta temporal con los archivos a procesar.
     * @param filtro        El criterio de filtrado.
     */
    private void procesaArchivos(File carpetaTemporal, CriterioFiltro<RegistroCSV> filtro) {
        String rutaArchivoFinal = carpetaTemporal.getParent() + File.separator + "resultado.csv";
        File archivoSalida = new File(rutaArchivoFinal);

        File[] archivos = carpetaTemporal.listFiles();

        if (archivos == null || archivos.length == 0) return;

        // TODO: Escribir el encabezado del archivo de salida

        try (EscritorCSV escritorSalida = new EscritorCSV(archivoSalida, true)) {
            // TODO: Pasar un CriterioFiltro en lugar de las opciones
            for (int i = 0; i < archivos.length; i++) {
                procesaArchivo(archivos[i], escritorSalida, filtro);
            }
            escritorSalida.flush();
        } catch (Exception e) {
            System.out.println("Error al procesar archivos temporales: " + e.getMessage());
        }

    }

    /**
     * Hace el procesamiento secuencial.
     *
     * @param opciones Configuraciones para ejecutar el programa.
     */
    public void procesa(Opciones opciones) {
        DivisorArchivo divisor = new DivisorArchivo(opciones.getArchivo(), opciones.getCantidadSubarchivos());

        try {
            System.out.println("Dividiendo archivo en subarchivos...");
            divisor.divide();
        } catch (Exception e) {
            System.out.println("Error al dividir archivo: " + e.getMessage());
        }

        CriterioFiltro<RegistroCSV> filtro = opciones.getCriterioFiltro();
        procesaArchivos(divisor.getCarpetaTemporal(), filtro);

        AdminArchivosTmp.eliminaCarpetaTemporal(divisor.getCarpetaTemporal());
    }
}
