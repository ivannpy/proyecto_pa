package unam.pcic.io;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.locks.ReentrantLock;
import java.nio.file.Files;
import java.nio.file.Paths;


/**
 * - Registra líneas con errores de parseo.
 * - Es thread-safe para la escritura del log.
 * - Es Singleton (usar singleton pattern).
 * - Incluye detalles de los errores.
 * - Describe los errores en las validaciones
 * - Da los detalles de la ejecucion (los prints)
 */
public class Logger {

    /**
     * Instancia única del Logger
     */
    private static final Logger instancia = new Logger();

    /**
     * Candado para garantizar thread-safety en la escritura
     */
    private final ReentrantLock lock = new ReentrantLock();

    /**
     * Ruta del log
     */
    private final String rutaArchivo;

    /**
     * Formateador de fechas
     */
    private final DateTimeFormatter formateador;

    /**
     * Niveles del log
     */
    public enum Nivel {
        DEBUG, INFO, WARN, ERROR, FATAL
    }

    /**
     * Constructor privado para usar patrón Singleton.
     */
    private Logger() {
        this.rutaArchivo = "logs/aplicacion.log";
        this.formateador = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        crearDirectorioSiNoExiste();
    }

    /**
     * Crea el directorio de logs si no existe
     */
    private void crearDirectorioSiNoExiste() {
        try {
            Files.createDirectories(Paths.get("logs"));
        } catch (IOException e) {
            System.err.println("No se pudo crear el directorio de logs: " + e.getMessage());
        }
    }

    /**
     * Obtiene la instancia del Logger
     * Como se usa el patrón Singleton, solo se puede acceder al Log con este método.
     *
     * @return La instancia del Logger
     */
    public static Logger getInstancia() {
        return instancia;
    }

    /**
     * Registra un mensaje con el nivel especificado.
     *
     * @param nivel   Nivel del mensaje
     * @param mensaje Contenido del mensaje
     */
    public void log(Nivel nivel, String mensaje) {
        escribirLog(formatearMensaje(nivel, mensaje, null));
    }

    /**
     * Registra un mensaje con el Nivel especificado y la excepción arrojada.
     *
     * @param nivel     Nivel del mensaje
     * @param mensaje   Contenido del mensaje
     * @param arrojable Excepción arrojada
     */
    public void log(Nivel nivel, String mensaje, Throwable arrojable) {
        escribirLog(formatearMensaje(nivel, mensaje, arrojable));
    }

    /**
     * Registra un mensaje de nivel DEBUG
     *
     * @param mensaje Contenido del mensaje
     */
    public void debug(String mensaje) {
        log(Nivel.DEBUG, mensaje);
    }

    /**
     * Registra un mensaje de nivel INFO
     *
     * @param mensaje Contenido del mensaje
     */
    public void info(String mensaje) {
        log(Nivel.INFO, mensaje);
    }

    /**
     * Registra un mensaje de nivel WARN
     *
     * @param mensaje Contenido del mensaje
     */
    public void warn(String mensaje) {
        log(Nivel.WARN, mensaje);
    }

    /**
     * Registra un mensaje de nivel ERROR
     *
     * @param mensaje Contenido del mensaje
     */
    public void error(String mensaje) {
        log(Nivel.ERROR, mensaje);
    }

    /**
     * Registra un mensaje de nivel ERROR con detalles de la excepción
     *
     * @param mensaje   Contenido del mensaje
     * @param arrojable Excepción relacionada
     */
    public void error(String mensaje, Throwable arrojable) {
        log(Nivel.ERROR, mensaje, arrojable);
    }

    /**
     * Registra un mensaje de nivel FATAL
     *
     * @param mensaje Contenido del mensaje
     */
    public void fatal(String mensaje) {
        log(Nivel.FATAL, mensaje);
    }

    /**
     * Registra un error de parseo de una línea dada.
     *
     * @param numeroLinea    Número de la línea con error
     * @param contenidoLinea Contenido de la línea
     * @param mensaje        Mensaje de error
     * @param arrojable      Excepción relacionada (opcional)
     */
    public void errorParseo(int numeroLinea, String contenidoLinea, String mensaje, Throwable arrojable) {
        String mensajeCompleto = String.format(
                "Error de parseo en línea %d: '%s'. %s",
                numeroLinea,
                contenidoLinea,
                mensaje
        );
        log(Nivel.ERROR, mensajeCompleto, arrojable);
    }

    /**
     * Registra un error de validación
     *
     * @param campo   Campo que falló la validación
     * @param valor   Valor que no pasó la validación
     * @param mensaje Mensaje descriptivo del error
     */
    public void errorValidacion(String campo, String valor, String mensaje) {
        String mensajeCompleto = String.format(
                "Error de validación en campo '%s' con valor '%s': %s",
                campo,
                valor,
                mensaje
        );
        log(Nivel.ERROR, mensajeCompleto);
    }

    /**
     * Formatea el mensaje de log con la fecha, nivel y detalles de error
     *
     * @param nivel     Nivel del mensaje
     * @param mensaje   Contenido del mensaje
     * @param arrojable Excepción arrojada
     * @return El mensaje formateado
     */
    private String formatearMensaje(Nivel nivel, String mensaje, Throwable arrojable) {
        StringBuilder sb = new StringBuilder();

        sb.append(LocalDateTime.now().format(formateador))
                .append(" [")
                .append(nivel)
                .append("] ")
                .append(mensaje);

        sb.append(" (Thread: ")
                .append(Thread.currentThread().getName())
                .append(")");

        if (arrojable != null) {
            sb.append("\n  Causa: ").append(arrojable.getClass().getName()).append(": ").append(arrojable.getMessage());

            StackTraceElement[] stackTrace = arrojable.getStackTrace();

            int max = Math.min(5, stackTrace.length);

            for (int i = 0; i < max; i++) sb.append("\n    at ").append(stackTrace[i]);

            if (stackTrace.length > max) sb.append("\n    ... ").append(stackTrace.length - max).append(" más");
        }

        return sb.toString();
    }

    /**
     * Escribe el mensaje en el archivo de log de forma thread-safe
     *
     * @param mensaje Mensaje formateado para escribir
     */
    private void escribirLog(String mensaje) {
        lock.lock();
        try (PrintWriter escritor = new PrintWriter(new FileWriter(rutaArchivo, true))) {
            escritor.println(mensaje);

            System.out.println(mensaje);
        } catch (IOException e) {
            System.err.println("Error al escribir en el archivo de log: " + e.getMessage());
        } finally {
            lock.unlock();
        }
    }
}
