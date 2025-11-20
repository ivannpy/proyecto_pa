package unam.pcic;

import unam.pcic.dominio.RegistroCSV;
import unam.pcic.io.LectorCSV;
import java.io.*;
import java.net.URL;
import java.util.List;


public class Main {
    public static void main(String[] args) {
        prueba();
    }

    private static void prueba() {
        try {
            ClassLoader classLoader = Main.class.getClassLoader();
            URL resource = classLoader.getResource("sample.csv");

            if (resource == null) {
                // Meterlo a Logger
                System.err.println("Error: No se encontró el archivo en resources/");
                return;
            }

            File inputFile = new File(resource.getFile());
            LectorCSV lector = new LectorCSV(inputFile, true);

            String[] encabezados = lector.leerEncabezado();
            System.out.println("Columnas: " + encabezados.length);
            for (int i = 0; i < encabezados.length; i++) {
                System.out.println("  [" + i + "] " + encabezados[i]);
            }

            List<RegistroCSV> registros = lector.leer();
            System.out.println("Total de registros leídos: " + registros.size());

            for (RegistroCSV registro : registros) {
                if (registro.getNumeroColumnas() != 24)
                    System.out.println("No hay 24 columnas en cada registro");
            }

            int n = 30;
            System.out.println("\nPrimeros " + n + " registros:");
            for (int i = 0; i < Math.min(n, registros.size()); i++) {
                RegistroCSV registro = registros.get(i);
                System.out.println("Línea " + registro.getNumeroLinea() + ": " + registro);
            }

        } catch (Exception e) {
            System.err.println("Error al leer archivo: " + e.getMessage());
        }
    }
}