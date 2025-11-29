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
     * Procesa un archivo.
     *
     * @param archivo el archivo a procesar
     */
    private void procesaArchivo(File archivo, File archivoSalida, Opciones opciones) {
        System.out.println("Procesando archivo: " + archivo.getName());

        // TODO: Modificar para que el filtro sepa qué columnas seleccionar, para no pasar las opciones
        CriterioFiltro<RegistroCSV> filtro = null;
        if (opciones.getColumnas() != null) {
            System.out.println("Se seleccionaran columnas");
            filtro = CriterioFiltro.paraRegistroCSV();
        }

        LectorCSV lector = new LectorCSV(archivo, true);
        try {
            RegistroCSV registro;
            while ((registro = lector.siguienteRegistro()) != null) {
                // TODO: Aplicar filtros, seleccion de columnas y limpiar datos antes de escribir
                if (filtro != null) {
                    registro = filtro.seleccionarColumnas(registro, opciones.getColumnas());
                }
                EscritorCSV.escribeRegistro(registro, archivoSalida);
            }
        } catch (Exception e) {
            // TODO: Manejar con el Logger
            System.out.println("Error al procesar archivo: " + archivo.getName());
        }
    }

    private void procesaArchivos(File carpetaTemporal, Opciones opciones) {
        String rutaArchivoFinal = carpetaTemporal.getParent() + File.separator + "resultado.csv";
        File archivoSalida = new File(rutaArchivoFinal);

        File[] archivos = carpetaTemporal.listFiles();

        if (archivos == null || archivos.length == 0) return;

        // TODO: Escribir el encabezado del archivo de salida

        for (int i = 0; i < archivos.length; i++) {
            procesaArchivo(archivos[i], archivoSalida, opciones);
        }
    }

    /**
     * Hace el procesamiento secuencial.
     *
     * @param opciones Configuraciones para ejecutar el programa.
     */
    public void procesa(Opciones opciones) {
        DivisorArchivo divisor = new DivisorArchivo(opciones.getArchivo());

        try {
            divisor.divide();
        } catch (Exception e) {
            System.err.println("Error al dividir archivo: " + e.getMessage());
        }

        // TODO: Crear un CriterioFiltro a partir de las opciones y pasarlo en lugar de pasar las opciones directamente
        procesaArchivos(divisor.getCarpetaTemporal(), opciones);

        AdminArchivosTmp.eliminaCarpetaTemporal(divisor.getCarpetaTemporal());
    }
}
