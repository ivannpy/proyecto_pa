package unam.pcic.test;

import org.junit.Test;
import unam.pcic.ControladorAplicacion;
import unam.pcic.analisis.Estadisticas;
import unam.pcic.utilidades.Configuracion;
import unam.pcic.utilidades.Opciones;


public class TestEstadisticas {

    public TestEstadisticas() {

    }

    @Test
    public void testNubeDePalabras() {
        String[] juegos = new String[]{"Counter-Strike","Halo: The Master Chief Collection"};

        String[] args = new String[]{".\\data\\sample.csv", "-c", "2,10,11", "-f", "c1=spanish", "-l", "10",};

        Opciones opciones = Configuracion.parsea(args);

        System.out.println(opciones);

        ControladorAplicacion.ejecutar(opciones);

        Estadisticas.generaNubesDePalabras(opciones, juegos,"spanish");

    }
}
