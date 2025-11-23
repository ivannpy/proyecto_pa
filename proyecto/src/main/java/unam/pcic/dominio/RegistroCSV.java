package unam.pcic.dominio;

/**
 * - Representa una fila o renglón del CSV.
 * - Sus atributos son los campos del CSV, y son dinámicos.
 * - Provee métodos para acceder a valores por índice o nombre de columna.
 * - Solo debe usarlo el procesador secuencial.
 */
public class RegistroCSV {
    /** Valores del registro */
    private final String[] valores;

    /** Posición del registro en el archivo */
    private final Long numeroLinea;

    /**
     * Construye un RegistroCSV a partir de una arreglo de valores (Strings) y su posición en el archivo.
     *
     * @param valores Arreglo de Strings con los valores del registro.
     * @param numeroLinea Posición del registro en el archivo.
     */
    public RegistroCSV(String[] valores, Long numeroLinea){
        if (valores == null || valores.length == 0) {
            throw new IllegalArgumentException("Los valores no pueden ser null o vacíos");
        }
        this.valores = valores;
        this.numeroLinea = numeroLinea;
    }

    /**
     * Regresa un arreglo de Strings con los valores del registro.
     *
     * @return Un arreglo de Strings con los valores del registro.
     */
    public String[] getValores(){
        return valores;
    }

    /**
     * Regresa la posición del registro en el archivo.
     *
     * @return La posición del registro en el archivo.
     */
    public Long getNumeroLinea(){
        return numeroLinea;
    }

    /**
     * Regresa el valor de una columna del registro.
     *
     * @param indiceColumna El indice de la columna.
     * @return El valor del registro para esa columna.
     */
    public String getValor(int indiceColumna){
        if (indiceColumna < 0 || indiceColumna >= valores.length) {
            throw new IndexOutOfBoundsException("Índice de columna inválido: " + indiceColumna);
        }
        return valores[indiceColumna];
    }

    /**
     * Regresa la cantidad de columnas del registro.
     *
     * @return la cantidad de columnas del registro.
     */
    public int getNumeroColumnas(){
        return valores.length;
    }

    /**
     * Regresa true si el registro tiene un valor para la columna indicada.
     *
     * @param indiceColumna la columna indicada.
     * @return true si el registro tiene un valor para la columna indicada, false en caso contrario.
     */
    public boolean tieneValor(int indiceColumna){
        String valor = getValor(indiceColumna);
        return valor != null && !valor.trim().isEmpty();
    }

    /**
     * Representación en cadena del registro.
     *
     * @return una representación en cadena del registro.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("RegistroCSV(Linea=");
        sb.append(getNumeroLinea());
        sb.append(", ColumnasTotales=");
        sb.append(getNumeroColumnas());
        sb.append(", Valores={");

        for(String campo: valores) {
            sb.append(campo);
            sb.append(",");
        }
        sb.append("})");

        return sb.toString();
    }

    /**
     * Genera una cadena con los valores del registro para escribirlo en un archivo CSV.
     *
     * @return una cadena con los valores del registro.
     */
    public String serializa(){
        StringBuilder sb = new StringBuilder();
        for(String campo: valores) {
            sb.append(campo);
            sb.append(",");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

}
