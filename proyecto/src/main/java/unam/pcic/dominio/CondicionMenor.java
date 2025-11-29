package unam.pcic.dominio;

/**
 * Implementa la condición de 'mayor que' de registro según el valor de una columna.
 */
public class CondicionMenor implements CondicionFiltro<RegistroCSV> {
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
    public CondicionMenor(int columna, String valor) {
        this.columna = columna;
        this.valor = valor;
    }

    /**
     * Regresa true si el registro dado tiene el valor buscado en la columna indicada.
     *
     * @param registro El registro a comparar.
     * @return true si el registro dado tiene el valor buscado en la columna indicada, false en caso contrario.
     */
    public boolean cumple(RegistroCSV registro) {
        return registro.tieneValor(columna) && registro.getValor(columna).compareTo(valor) < 0;
    }
}
