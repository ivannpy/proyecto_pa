package unam.pcic;

import unam.pcic.analisis.AnalizadorRendimiento;
import unam.pcic.dominio.*;
import unam.pcic.io.AdminArchivosTmp;
import unam.pcic.io.DivisorArchivo;
import unam.pcic.io.Logger;
import unam.pcic.procesamiento.ProcesadorCSV;
import unam.pcic.utilidades.Opciones;


/**
 * - Orquesta el flujo completo
 * - Ejecuta la versión secuencial y/o la concurrente según Opciones
 * - Invoca el análisis de rendimiento.
 * - Coordina la generación de outputs.
 */
public class ControladorAplicacion {

    /**
     * Ejecuta la aplicación según las configuraciones.
     * Respeta el modo elegido por el usuario:
     * - SECUENCIAL
     * - CONCURRENTE
     * - AMBOS (primero secuencial y luego concurrente)
     *
     * @param opciones Opciones para ejecutar el programa.
     */
    public static void ejecutar(Opciones opciones) {
        Logger logger = Logger.getInstancia();

        // Dividir archivo en subarchivos
        AnalizadorRendimiento analizador = new AnalizadorRendimiento(opciones);
        DivisorArchivo divisor = new DivisorArchivo(opciones);
        analizador.iniciar();
        try {
            logger.info("Dividiendo archivo en " + opciones.getCantidadSubarchivos() + " subarchivos...");
            divisor.divide();
        } catch (Exception e) {
            logger.error("Error al dividir archivo: " + e.getMessage());
            System.exit(1);
        }

        analizador.finalizar();
        logger.info("Tiempo que tomo dividir archivo: " + analizador.getTiempoTranscurrido() + " s.");

        if (opciones.isEjecutarAmbos()) {
            logger.debug("Modo seleccionado: AMBOS (SECUENCIAL y CONCURRENTE)");

            logger.debug("Inicia la ejecución SECUENCIAL");
            ControladorAplicacion.ejecutar(opciones, Procesamiento.SECUENCIAL);

            logger.debug("Inicia la ejecución CONCURRENTE");
            ControladorAplicacion.ejecutar(opciones, Procesamiento.CONCURRENTE);

        } else if (opciones.getModoProcesamiento() == Procesamiento.CONCURRENTE) {
            logger.debug("Modo seleccionado: CONCURRENTE");
            logger.debug("Inicia la ejecución CONCURRENTE");
            ControladorAplicacion.ejecutar(opciones, Procesamiento.CONCURRENTE);
        } else {
            logger.debug("Modo seleccionado: SECUENCIAL");
            logger.debug("Inicia la ejecución SECUENCIAL");
            ControladorAplicacion.ejecutar(opciones, Procesamiento.SECUENCIAL);
        }

        AdminArchivosTmp.eliminaCarpetaTemporal(opciones.getCarpetaTemporal());
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
