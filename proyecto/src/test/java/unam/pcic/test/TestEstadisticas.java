package unam.pcic.test;

import org.junit.Test;
import unam.pcic.ControladorAplicacion;
import unam.pcic.analisis.AnalizadorRendimiento;
import unam.pcic.analisis.Estadisticas;
import unam.pcic.utilidades.Configuracion;
import unam.pcic.utilidades.Opciones;


public class TestEstadisticas {

    public TestEstadisticas() {
    }

    @Test
    public void testNubeDePalabras() {
        String[] juegos = new String[]{"PUBG: BATTLEGROUNDS"};

        // "Halo: The Master Chief Collection",
        //                "PUBG: BATTLEGROUNDS",
        //                "Grand Theft Auto V"

        String idioma = "english";

        String archivoParcial = ".\\data\\sample.csv";
        String archivoCompleto = "C:\\Users\\jivan\\Descargas\\Steam reviews\\all_reviews\\all_reviews.csv";

        String[] args = new String[]{archivoCompleto,
                "-c", "2,10,11",
                //"-f", "c1=spanish",
                "-f", "c0=" + "PUBG: BATTLEGROUNDS",
                "-l", "10",
                "-m", "conc"};

        Opciones opciones = Configuracion.parsea(args);

        ControladorAplicacion.ejecutar(opciones);

        boolean quitarStopWords = true;

        AnalizadorRendimiento analizadorRendimiento = new AnalizadorRendimiento(opciones);
        analizadorRendimiento.iniciar();

        Estadisticas.generaNubesDePalabras(opciones, juegos, idioma, quitarStopWords);

        analizadorRendimiento.finalizar();
        System.out.println("Tiempo en generar n-gramas: " + analizadorRendimiento.getTiempoTranscurrido() + " s.");
    }
}
