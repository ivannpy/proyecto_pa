package unam.pcic.test;

import org.junit.Test;
import unam.pcic.ControladorAplicacion;
import unam.pcic.utilidades.Configuracion;
import unam.pcic.utilidades.Opciones;

public class TestCantidadSubarchivos {
    public TestCantidadSubarchivos() {

    }

    @Test
    public void testRendimientoConcurrente() {
        // Las pruebas se hacen sobre el archivo de 39.5 GB
        String archivoParcial = ".\\data\\sample.csv";
        String archivoCompleto = "C:\\Users\\jivan\\Descargas\\Steam reviews\\all_reviews\\all_reviews.csv";
        String[] args = new String[]{archivoParcial,
                "-c", "2,10,11",
                "-f", "c1=spanish",
                "-l", "10",
                "-m", "conc"};

        Opciones opciones = Configuracion.parsea(args);
        opciones.setCantidadSubarchivos(1);
        ControladorAplicacion.ejecutar(opciones);

        opciones.setCantidadSubarchivos(2);
        ControladorAplicacion.ejecutar(opciones);

        opciones.setCantidadSubarchivos(4);
        ControladorAplicacion.ejecutar(opciones);

        opciones.setCantidadSubarchivos(8);
        ControladorAplicacion.ejecutar(opciones);
    }

    @Test
    public void testRendimientoSecuencial() {
        // Las pruebas finales se hacen sobre el archivo de 39.5 GB
        String archivoParcial = ".\\data\\sample.csv";
        String archivoCompleto = "C:\\Users\\jivan\\Descargas\\Steam reviews\\all_reviews\\all_reviews.csv";
        String[] args = new String[]{archivoParcial,
                "-c", "2,10,11",
                "-f", "c1=spanish",
                "-l", "10",
                "-m", "sec"};

        Opciones opciones = Configuracion.parsea(args);
        opciones.setCantidadSubarchivos(1);
        ControladorAplicacion.ejecutar(opciones);

        opciones.setCantidadSubarchivos(2);
        ControladorAplicacion.ejecutar(opciones);

        opciones.setCantidadSubarchivos(4);
        ControladorAplicacion.ejecutar(opciones);

        opciones.setCantidadSubarchivos(8);
        ControladorAplicacion.ejecutar(opciones);
    }
}
