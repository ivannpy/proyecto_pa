package unam.pcic.analisis;

import unam.pcic.dominio.*;
import unam.pcic.io.LectorCSV;
import unam.pcic.utilidades.Opciones;

import java.io.File;
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


    private static boolean esStopWord(String palabra, String idioma) {
        if (palabra == null || palabra.isEmpty()) return true;
        String p = palabra.toLowerCase(Locale.ROOT);
        return switch (idioma.toLowerCase(Locale.ROOT)) {
            case "spanish", "es" -> STOPWORDS_ES.contains(p);
            case "english", "en" -> STOPWORDS_EN.contains(p);
            default -> false;
        };
    }

    public static void generaNubesDePalabras(Opciones opciones, String[] juegos, String idioma) {
        File archivo = opciones.getArchivoDeSalida();

        System.out.println("Archivo de salida: " + archivo.getAbsolutePath());

        LectorCSV lector = new LectorCSV(archivo, true);

        try {
            AlmacenRenglones tabla = lector.leerTodo();
            for (String juego : juegos) {
                generaNubeDePalabras(tabla, juego, idioma);
            }
        } catch (Exception e) {
            System.err.println("Error al leer archivo: " + e.getMessage());
        }
    }

    private static void generaNubeDePalabras(AlmacenRenglones tabla, String juego, String idioma) {
        String[] columnas = tabla.getEncabezado().getValores();
        System.out.println("Columnas del archivo: " + Arrays.toString(columnas));

        int indiceGame = -1;
        int indiceReview = -1;
        int indiceLanguage = -1;
        for (int i = 1; i < columnas.length; i++) {
            if (columnas[i].equals("game")) indiceGame = i;
            if (columnas[i].equals("review")) indiceReview = i;
            if (columnas[i].equals("language")) indiceLanguage = i;
        }

        //assert indiceGame != -1;
        //assert indiceReview != -1;
        //assert indiceLanguage != -1;

        CondicionFiltro<RegistroCSV> condicionJuego = new CondicionIgualdad(indiceGame, juego);
        CondicionFiltro<RegistroCSV> condicionIdioma = new CondicionIgualdad(indiceLanguage, idioma);

        Map<String, Integer> unigramas = new HashMap<>();
        Map<String, Integer> bigramas = new HashMap<>();


        for (RegistroCSV registro : tabla.getRegistros()) {
            if (!condicionJuego.cumple(registro) || !condicionIdioma.cumple(registro)) continue;

            idioma = idioma.toLowerCase(Locale.ROOT);

            String comentario = registro.getValor(indiceReview);
            if (comentario == null || comentario.isBlank()) continue;

            // Tokenización básica: separar por cualquier carácter que NO sea letra
            String[] tokensCrudos = comentario.toLowerCase(Locale.ROOT).split("\\P{L}+");

            List<String> tokensLimpios = new ArrayList<>();

            for (String token : tokensCrudos) {
                if (token == null || token.isBlank()) continue;
                if (esStopWord(token, idioma)) continue;
                tokensLimpios.add(token);
            }

            if (tokensLimpios.isEmpty()) continue;

            // Contar unigramas
            for (String token : tokensLimpios) {
                unigramas.merge(token, 1, Integer::sum);
            }

            // Contar bigramas
            String prev = null;
            for (String token : tokensLimpios) {
                if (prev != null) {
                    String bigrama = prev + " " + token;
                    bigramas.merge(bigrama, 1, Integer::sum);
                }
                prev = token;
            }

            System.out.println("=== Nube de palabras para juego: " + juego + "===");
            imprimirTopFrecuencias(unigramas, "Unigramas", 20);
            imprimirTopFrecuencias(bigramas, "Bigramas", 20);
        }

    }

    private static void imprimirTopFrecuencias(Map<String, Integer> mapa,
                                               String titulo,
                                               int topN) {
        if (mapa.isEmpty()) {
            System.out.println(titulo + ": (sin datos)");
            return;
        }

        System.out.println(">> " + titulo);
        mapa.entrySet().stream()
                .sorted((e1, e2) -> Integer.compare(e2.getValue(), e1.getValue()))
                .limit(topN)
                .forEach(e -> System.out.println(e.getKey() + " -> " + e.getValue()));
        System.out.println();
    }

}
