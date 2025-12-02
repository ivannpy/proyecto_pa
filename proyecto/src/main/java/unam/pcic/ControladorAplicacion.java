package unam.pcic;

import unam.pcic.analisis.AnalizadorRendimiento;
import unam.pcic.dominio.*;
import unam.pcic.io.AdminArchivosTmp;
import unam.pcic.io.DivisorArchivo;
import unam.pcic.io.LectorCSV;
import unam.pcic.io.Logger;
import unam.pcic.procesamiento.ProcesadorCSV;
import unam.pcic.utilidades.Opciones;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * - Orquesta el flujo completo
 * - Ejecuta la versión secuencial y la concurrente
 * - Invoca el analisis de rendimiento.
 * - Coordina la generación de outputs.
 */
public class ControladorAplicacion {

    /**
     * Ejecuta la aplicación según las configuraciones.
     * Ejecuta ambos modos de procesamiento.
     *
     * @param opciones Opciones para ejecutar el programa.
     */
    public static void ejecutar(Opciones opciones) {
        Logger logger = Logger.getInstancia();

        // Dividir archivo en subarchivos
        DivisorArchivo divisor = new DivisorArchivo(opciones);

        try {
            logger.info("Dividiendo archivo en subarchivos...");
            divisor.divide();
        } catch (Exception e) {
            logger.error("Error al dividir archivo: " + e.getMessage());
            System.exit(1);
        }

        // TODO: Que la versión secuencial no tome los subarchivos
        logger.debug("Inicia la ejecución secuencial");
        ControladorAplicacion.ejecutar(opciones, Procesamiento.SECUENCIAL);

        // TODO: Que solo la versión concurrente tome los subarchivos
        logger.debug("Inicia la ejecución secuencial");
        ControladorAplicacion.ejecutar(opciones, Procesamiento.CONCURRENTE);

        //AdminArchivosTmp.eliminaCarpetaTemporal(opciones.getCarpetaTemporal());
    }

    /**
     * Ejecuta el flujo completo para un modo de procesamiento.
     *
     * @param opciones Las opciones para ejecutar el programa.
     * @param modo     El modo de procesamiento (Secuencial o Concurrente)
     */
    private static void ejecutar(Opciones opciones, Procesamiento modo) {
        Logger logger = Logger.getInstancia();

        AnalizadorRendimiento analizador = new AnalizadorRendimiento(opciones);
        analizador.iniciar();

        ProcesadorCSV procesador = FabricaProcesador.crearProcesador(modo);
        if (procesador == null) {
            logger.error("No se pudo crear el procesador para el modo " + modo);
            return;
        }

        procesador.procesa(opciones);
        analizador.finalizar();

        logger.info("Tiempo con procesamiento " + modo + ": " + analizador.getTiempoTranscurrido() + " s.");
    }

}
