package unam.pcic.dominio;

/**
 * Implementa la condición de 'menor o igual' de registro según el valor de una columna.
 */
public class CondicionMenorIgual implements CondicionFiltro<RegistroCSV> {
    /** Columna del registro a comparar */
    private final int columna;

    /** Valor buscado */
    private final String valor;

    /**
     * Constructor.
     *      Representa una condición de filtrado.
     *
     * @param columna La columna del registro a comparar.
     * @param valor El valor buscado.
     */
    public CondicionMenorIgual(int columna, String valor) {
        this.columna = columna;
        this.valor = valor;
    }

    /**
     * Regresa true si el valor del registro dado es menor o igual al valor buscado.
     *
     * @param registro El registro a comparar.
     * @return true si el valor del registro dado es menor o igual al valor buscado, false en caso contrario.
     */
    public boolean cumple(RegistroCSV registro) {
        return registro.tieneValor(columna) && registro.getValor(columna).compareTo(valor) <= 0;
    }
}
