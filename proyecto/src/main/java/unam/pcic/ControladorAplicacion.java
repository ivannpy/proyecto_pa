package unam.pcic;

import unam.pcic.analisis.AnalizadorRendimiento;
import unam.pcic.dominio.*;
import unam.pcic.io.LectorCSV;
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
        System.out.println("Ejecutando app...");

        ControladorAplicacion.ejecutar(opciones, Procesamiento.SECUENCIAL);
        ControladorAplicacion.ejecutar(opciones, Procesamiento.CONCURRENTE);
    }

    /**
     * Ejecuta el flujo completo para un modo de procesamiento.
     *
     * @param opciones Las opciones para ejecutar el programa.
     * @param modo     El modo de procesamiento (Secuencial o Concurrente)
     */
    private static void ejecutar(Opciones opciones, Procesamiento modo) {
        AnalizadorRendimiento analizador = new AnalizadorRendimiento(opciones);
        analizador.iniciar();

        ProcesadorCSV procesador = FabricaProcesador.crearProcesador(modo);
        if (procesador == null) {
            System.err.println("Error al crear ProcesadorCSV");
            return;
        }

        procesador.procesa(opciones);
        analizador.finalizar();

        String modoStr = (modo == Procesamiento.SECUENCIAL) ? "Secuencial" : "Concurrente";
        System.out.println("Tiempo que tomó el procesamiento " + modoStr + ": " + analizador.getTiempoTranscurrido());
    }

}
