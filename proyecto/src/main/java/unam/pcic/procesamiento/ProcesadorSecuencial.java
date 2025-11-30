package unam.pcic.procesamiento;

import unam.pcic.dominio.CondicionFiltro;
import unam.pcic.dominio.CriterioFiltro;
import unam.pcic.dominio.RegistroCSV;
import unam.pcic.io.AdminArchivosTmp;
import unam.pcic.io.DivisorArchivo;
import unam.pcic.io.EscritorCSV;
import unam.pcic.io.LectorCSV;
import unam.pcic.utilidades.Opciones;
import java.io.File;
import java.util.List;


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
     * @param opciones Las opciones para el procesamiento (se cambiará por un CriterioFiltro)
     */
    private void procesaArchivo(File archivo, EscritorCSV escritor, Opciones opciones) {
        System.out.println("Procesando archivo: " + archivo.getName());

        // TODO: Modificar para que el filtro sepa qué columnas seleccionar, para no pasar las opciones
        // Seleccionar columnas
        CriterioFiltro<RegistroCSV> filtro = null;
        if (opciones.getColumnas() != null) {
            System.out.println("Se seleccionaran columnas");
            filtro = CriterioFiltro.paraRegistroCSV();
        }

        // Aplicar filtros
        List<CondicionFiltro<RegistroCSV>> condiciones = opciones.getFiltros();

        LectorCSV lector = new LectorCSV(archivo, true);
        try {
            RegistroCSV registro;
            while ((registro = lector.siguienteRegistro()) != null) {
                // Aplicar seleccion de columnas
                if (filtro != null) {
                    registro = filtro.seleccionarColumnas(registro, opciones.getColumnas());
                }

                // TODO: Aplicar filtros y limpiar datos
                boolean noCumpleAlguna = false;
                if (condiciones != null) {
                    for (CondicionFiltro<RegistroCSV> cond : condiciones) {
                        if (!cond.cumple(registro)) {
                            noCumpleAlguna = true;
                            break;
                        }
                    }
                }

                if (noCumpleAlguna) continue;

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
     * @param opciones        Las opciones para el procesamiento.
     */
    private void procesaArchivos(File carpetaTemporal, Opciones opciones) {
        String rutaArchivoFinal = carpetaTemporal.getParent() + File.separator + "resultado.csv";
        File archivoSalida = new File(rutaArchivoFinal);

        File[] archivos = carpetaTemporal.listFiles();

        if (archivos == null || archivos.length == 0) return;

        // TODO: Escribir el encabezado del archivo de salida

        try (EscritorCSV escritorSalida = new EscritorCSV(archivoSalida, true)) {
            // TODO: Pasar un CriterioFiltro en lugar de las opciones
            for (int i = 0; i < archivos.length; i++) {
                procesaArchivo(archivos[i], escritorSalida, opciones);
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

        // TODO: Crear un CriterioFiltro a partir de las opciones y pasarlo en lugar de pasar las opciones directamente
        procesaArchivos(divisor.getCarpetaTemporal(), opciones);

        AdminArchivosTmp.eliminaCarpetaTemporal(divisor.getCarpetaTemporal());
    }
}
