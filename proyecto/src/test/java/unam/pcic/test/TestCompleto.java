package unam.pcic.test;

import org.junit.Test;
import unam.pcic.ControladorAplicacion;
import unam.pcic.utilidades.Configuracion;
import unam.pcic.utilidades.Opciones;


public class TestCompleto {
    public TestCompleto() {

    }

    @Test
    public void testCompletoSeleccionFiltro() {
        String[] args = new String[]{".\\data\\sample.csv",
                "-c", "2,10,11",
                "-f", "c1=spanish",
                "-l", "10",
                "-m", "both"};

        Opciones opciones = Configuracion.parsea(args);

        ControladorAplicacion.ejecutar(opciones);
    }

    @Test
    public void testCompletoSeleccion() {
        String[] args = new String[]{".\\data\\sample.csv",
                "-c", "2,10,11",
                "-l", "10",
                "-m", "both"};

        Opciones opciones = Configuracion.parsea(args);

        ControladorAplicacion.ejecutar(opciones);
    }

    @Test
    public void testCompletoFIltro() {
        String[] args = new String[]{".\\data\\sample.csv",
                "-f", "c1=spanish",
                "-l", "10",
                "-m", "both"};

        Opciones opciones = Configuracion.parsea(args);

        ControladorAplicacion.ejecutar(opciones);
    }

    @Test
    public void testCompletoEstrella() {
        String[] args = new String[]{".\\data\\sample.csv",
                "-c", "*",
                "-l", "10",
                "-m", "both"};

        Opciones opciones = Configuracion.parsea(args);

        ControladorAplicacion.ejecutar(opciones);
    }

    @Test
    public void testSecuencial() {
        String[] args = new String[]{".\\data\\sample.csv",
                "-l", "10",
                "-m", "sec"};

        Opciones opciones = Configuracion.parsea(args);

        ControladorAplicacion.ejecutar(opciones);
    }

    @Test
    public void testConcurrente() {
        String[] args = new String[]{".\\data\\sample.csv",
                "-l", "10",
                "-m", "conc"};

        Opciones opciones = Configuracion.parsea(args);

        ControladorAplicacion.ejecutar(opciones);
    }
}
