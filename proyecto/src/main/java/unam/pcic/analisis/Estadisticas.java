package unam.pcic.analisis;

import unam.pcic.dominio.*;
import unam.pcic.io.LectorCSV;
import unam.pcic.utilidades.Opciones;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;


/**
 * - Recibe el archivo filtrado y procesado.
 * - Calcula las estadísticas
 */
public class Estadisticas {
    private static final Set<String> STOPWORDS_ES = Set.of(
            "a", "acá", "ahí", "al", "algo", "algunas", "algunos", "allá", "alli", "allí",
            "ante", "antes", "como", "con", "contra", "cual", "cuales", "cuando", "de",
            "del", "desde", "donde", "dos", "el", "él", "ella", "ellas", "ellos", "en",
            "era", "es", "esa", "ese", "eso", "esta", "este", "estos", "estas", "está",
            "están", "fue", "ha", "han", "hay", "la", "las", "los", "lo", "más", "me",
            "mi", "mis", "mucho", "muy", "no", "nos", "nosotros", "o", "otra", "otro",
            "para", "pero", "poco", "por", "porque", "que", "qué", "se", "si", "sí",
            "sin", "sobre", "su", "sus", "te", "tu", "tus", "un", "una", "unas", "unos",
            "ya", "y"
    );

    private static final Set<String> STOPWORDS_EN = Set.of(
            "a", "an", "the", "and", "or", "but", "of", "for", "on", "in", "at", "to",
            "from", "by", "with", "about", "into", "through", "during", "before", "after",
            "above", "below", "up", "down", "out", "over", "under", "again", "further",
            "then", "once", "here", "there", "all", "any", "both", "each", "few", "more",
            "most", "other", "some", "such", "no", "nor", "not", "only", "own", "same",
            "so", "than", "too", "very", "can", "will", "just", "don't", "should",
            "should've", "now", "is", "am", "are", "was", "were", "be", "been", "being",
            "have", "has", "had", "having", "do", "does", "did", "doing", "i", "you",
            "he", "she", "it", "we", "they", "me", "him", "her", "us", "them", "my",
            "your", "his", "their"
    );

    private static void calculaPromedios(AlmacenRenglones tabla,
                                         String columna,
                                         String[] juegos) {
        // Dada una columna numérica, calcula el promedio de los valores de cada juego.
    }


    // STOP WORDS para nubes de palabras

    private static boolean esStopWord(String palabra, String idioma) {
        if (palabra == null || palabra.isEmpty()) return true;
        String p = palabra.toLowerCase(Locale.ROOT);
        return switch (idioma.toLowerCase(Locale.ROOT)) {
            case "spanish", "es" -> STOPWORDS_ES.contains(p);
            case "english", "en" -> STOPWORDS_EN.contains(p);
            default -> false;
        };
    }

    public static void generaNubesDePalabras(Opciones opciones,
                                             String[] juegos,
                                             String idioma,
                                             boolean quitarStopWords) {

        File archivo = opciones.getArchivoDeSalida();

        LectorCSV lector = new LectorCSV(archivo, true);

        String carpeta = archivo.getParent();
        String stopWords = quitarStopWords ? "ssw" : "csw";

        try {
            AlmacenRenglones tabla = lector.leerTodo();
            for (String juego : juegos) {
                String nombreSeguro = juego.replaceAll("\\W+", "_");
                File archivoSalida = new File(carpeta, nombreSeguro + "_n_gramas_" + stopWords + ".txt");
                generaNubeDePalabras(tabla, juego, idioma, quitarStopWords, archivoSalida);
            }
            lector.cerrarLectorSecuencial();
        } catch (Exception e) {
            System.err.println("Error al leer archivo: " + e.getMessage());
        }
    }

    private static void generaNubeDePalabras(AlmacenRenglones tabla,
                                             String juego,
                                             String idioma,
                                             boolean quitarStopWords,
                                             File archivoSalida) {

        String[] columnas = tabla.getEncabezado().getValores();

        int indiceGame = -1;
        int indiceReview = -1;
        int indiceLanguage = -1;
        for (int i = 0; i < columnas.length; i++) {
            if (columnas[i].equals("game")) indiceGame = i;
            if (columnas[i].equals("review")) indiceReview = i;
            if (columnas[i].equals("language")) indiceLanguage = i;
        }

        assert indiceGame >= 0;
        assert indiceReview >= 0;
        assert indiceLanguage >= 0;

        CondicionFiltro<RegistroCSV> condicionJuego = new CondicionIgualdad(indiceGame, juego);
        CondicionFiltro<RegistroCSV> condicionIdioma = new CondicionIgualdad(indiceLanguage, idioma);

        Map<String, Integer> unigramas = new HashMap<>();
        Map<String, Integer> bigramas = new HashMap<>();
        Map<String, Integer> trigramas = new HashMap<>();
        Map<String, Integer> cuatrogramas = new HashMap<>();

        System.out.println("Procesando juego: " + juego);
        for (RegistroCSV registro : tabla.getRegistros()) {
            if (!condicionJuego.cumple(registro) || !condicionIdioma.cumple(registro)) continue;

            idioma = idioma.toLowerCase(Locale.ROOT);

            String comentario = registro.getValor(indiceReview);

            if (comentario == null || comentario.isBlank()) continue;

            String[] tokensCrudos = comentario.toLowerCase(Locale.ROOT).split("\\P{L}+");

            List<String> tokensLimpios = new ArrayList<>();

            for (String token : tokensCrudos) {
                if (token == null || token.isBlank()) continue;
                if (esStopWord(token, idioma) && quitarStopWords) continue;
                tokensLimpios.add(token);
            }

            if (tokensLimpios.isEmpty()) continue;

            // Unigramas
            for (String token : tokensLimpios) {
                unigramas.merge(token, 1, Integer::sum);
            }

            // Bigramas
            String prev = null;
            for (String token : tokensLimpios) {
                if (prev != null) {
                    String bigrama = prev + " " + token;
                    bigramas.merge(bigrama, 1, Integer::sum);
                }
                prev = token;
            }

            // Trigramas
            if (tokensLimpios.size() >= 3) {
                for (int i = 0; i <= tokensLimpios.size() - 3; i++) {
                    String trig = tokensLimpios.get(i) + " " +
                            tokensLimpios.get(i + 1) + " " +
                            tokensLimpios.get(i + 2);
                    trigramas.merge(trig, 1, Integer::sum);
                }
            }

            // 4-gramas
            if (tokensLimpios.size() >= 4) {
                for (int i = 0; i <= tokensLimpios.size() - 4; i++) {
                    String cuatro = tokensLimpios.get(i) + " " +
                            tokensLimpios.get(i + 1) + " " +
                            tokensLimpios.get(i + 2) + " " +
                            tokensLimpios.get(i + 3);
                    cuatrogramas.merge(cuatro, 1, Integer::sum);
                }
            }

        }
        try {
            escribirNGramasEnArchivo(archivoSalida, juego, idioma,
                    unigramas, bigramas, trigramas, cuatrogramas, 20);
        } catch (Exception e) {
            System.err.println("Error al escribir n-gramas para " + juego + ": " + e.getMessage());
        }
    }

    private static void escribirNGramasEnArchivo(File archivoSalida,
                                                 String juego,
                                                 String idioma,
                                                 Map<String, Integer> unigramas,
                                                 Map<String, Integer> bigramas,
                                                 Map<String, Integer> trigramas,
                                                 Map<String, Integer> cuatrogramas,
                                                 int topN) throws IOException {

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivoSalida, false))) {
            // Metadatos
            writer.write("Juego: " + juego);
            writer.newLine();
            writer.write("Idioma: " + idioma);
            writer.newLine();
            writer.write("Con stop words: No");
            writer.newLine();
            writer.newLine();

            // Secciones de n-gramas
            escribirTopFrecuenciasEnWriter(unigramas, "1-gramas", topN, writer);
            writer.newLine();
            escribirTopFrecuenciasEnWriter(bigramas, "2-gramas", topN, writer);
            writer.newLine();
            escribirTopFrecuenciasEnWriter(trigramas, "3-gramas", topN, writer);
            writer.newLine();
            escribirTopFrecuenciasEnWriter(cuatrogramas, "4-gramas", topN, writer);
        }
    }

    private static void escribirTopFrecuenciasEnWriter(Map<String, Integer> mapa,
                                                       String titulo,
                                                       int topN,
                                                       BufferedWriter writer) throws IOException {
        writer.write(titulo + ":");
        writer.newLine();

        if (mapa.isEmpty()) {
            writer.write("(sin datos)");
            writer.newLine();
            return;
        }

        List<Map.Entry<String, Integer>> entradas = new ArrayList<>(mapa.entrySet());
        entradas.sort((e1, e2) -> Integer.compare(e2.getValue(), e1.getValue()));

        int contador = 0;
        for (Map.Entry<String, Integer> e : entradas) {
            if (contador >= topN) break;
            writer.write(e.getKey() + " -> " + e.getValue());
            writer.newLine();
            contador++;
        }
    }

}