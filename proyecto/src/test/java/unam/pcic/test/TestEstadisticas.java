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
        String[] juegos = new String[]{"Among Us"};

        String archivoParcial = ".\\data\\sample.csv";
        String archivoCompleto = "C:\\Users\\jivan\\Descargas\\Steam reviews\\all_reviews\\all_reviews.csv";
        String[] args = new String[]{archivoCompleto,
                                    "-c", "2,10,11",
                                    "-f", "c1=spanish",
                                    "-l", "10",
                                    "-m", "conc"};

        Opciones opciones = Configuracion.parsea(args);

        ControladorAplicacion.ejecutar(opciones);

        boolean quitarStopWords = true;
        Estadisticas.generaNubesDePalabras(opciones, juegos,"spanish", quitarStopWords);

    }
}
