package unam.pcic.io;

import unam.pcic.dominio.RegistroCSV;
import unam.pcic.utilidades.Opciones;

import java.io.File;
import java.util.List;


/**
 * - Divide el archivo en N subarchivos temporales.
 * - Calcula N según la cantidad de procesadores disponibles.
 * - Distribuye registros equitativamente entre los subarchivos.
 * - Preserva el encabezado de cada subarchivo.
 */
public class DivisorArchivo {
    /**
     * Cantidad de subarchivos a crear
     */
    private final int cantidadSubarchivos;

    /**
     * El archivo de entrada
     */
    private final File archivoDeEntrada;

    /**
     * Constructor por defecto.
     * El DivisorArchivo sabe cuantos procesadores hay en el sistema.
     */
    public DivisorArchivo(Opciones opciones) {
        this.cantidadSubarchivos = opciones.getCantidadSubarchivos();
        this.archivoDeEntrada = opciones.getArchivoDeEntrada();
    }

    /**
     * Divide el archivo de entrada en tantos subarchivos como procesadores hay en el sistema.
     *
     */
    public void divide() {
        Logger logger = Logger.getInstancia();
        LectorCSV lector = new LectorCSV(archivoDeEntrada, true);

        List<File> archivosTemporales = AdminArchivosTmp.creaArchivosTemporales(archivoDeEntrada, cantidadSubarchivos);

        int i = 0;

        try (EscritorCSVMultiple escritor = new EscritorCSVMultiple(archivosTemporales)) {
            String[] valoresEncabezado = lector.leerEncabezado();
            if (valoresEncabezado != null) {
                RegistroCSV registroEncabezado = new RegistroCSV(valoresEncabezado, 0L);
                escritor.escribeEnTodos(registroEncabezado.serializa());
            }

            String linea;
            while ((linea = lector.siguienteLinea()) != null) {
                int indiceArchivoTmp = i % cantidadSubarchivos;
                escritor.escribeLinea(indiceArchivoTmp, linea);
                i++;
                if (i % 1_000_000 == 0) logger.debug("Registros procesados (DivisorArchivo): " + i);
            }
            escritor.flush();
            lector.cerrarLectorSecuencial();
        } catch (Exception e) {
            logger.error("Error al dividir archivo: " + archivoDeEntrada.getName() + " - " + e.getMessage());
            System.exit(1);
        }
    }
}
