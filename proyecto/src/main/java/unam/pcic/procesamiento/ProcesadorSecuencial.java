package unam.pcic.procesamiento;

import unam.pcic.io.AdminArchivosTmp;
import unam.pcic.io.DivisorArchivo;
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
     * Hace el procesamiento secuencial.
     *
     * @param opciones Configuraciones para ejecutar el programa.
     */
    public void procesa(Opciones opciones) {
        try {
            File inputFile = new File(opciones.getArchivo());
            DivisorArchivo divisor = new DivisorArchivo();
            divisor.divide(inputFile);
        } catch (Exception e) {
            System.err.println("Error al dividir archivo: " + e.getMessage());
        }
        // Una vez que se divide el archivo, se puede procesar de forma secuencial.


        // Se puede mejorar
        //File carpetaTemporal = new File(new File(opciones.getArchivo()).getAbsoluteFile() + "tmp");
        //AdminArchivosTmp.eliminaCarpetaTemporal(carpetaTemporal);
    }
}
