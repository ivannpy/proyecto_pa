package unam.pcic.io;

import unam.pcic.dominio.RegistroCSV;

import java.io.File;

/**
 * - Divide el archivo en N subarchivos temporales.
 * - Calcula N según la cantidad de procesadores disponibles.
 * - Distribuye registros equitativamente entre los subarchivos.
 * - Preserva el encabezado de cada subarchivo.
 */
public class DivisorArchivo {
    private final int cantidadProcesadores;

    public DivisorArchivo() {
        this.cantidadProcesadores = Runtime.getRuntime().availableProcessors();
    }

    public int getCantidadProcesadores() {
        return cantidadProcesadores;
    }

    public static void divide(File archivo){
        // Divide no hace ningun procesamiento.
        // Solo toma el archivo y lo divide en subarchivos.
        // Luego, cada subarchivo se procesa por separado.
        // Cuando se procesan los subarchivos, se procesan como RegistroCSV
        // Se aplican los filtros, etc y se escribe en un archivo nuevo.

        LectorCSV lector = new LectorCSV(archivo, true);
        try {
            RegistroCSV registro;

            while ((registro = lector.nextRegistro()) != null) {
                System.out.println("Registro en línea " + registro.getNumeroLinea());
                // Ejemplo: imprimir primera columna si existe
                System.out.println(registro);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
