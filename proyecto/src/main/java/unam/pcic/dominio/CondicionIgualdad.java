package unam.pcic.dominio;

/**
 * Implementa la condición de igualdad de registro según el valor de una columna.
 */
public class CondicionIgualdad implements CondicionFiltro<RegistroCSV> {
    /**
     * Columna del registro a comparar
     */
    private final int columna;

    /**
     * Valor buscado
     */
    private final String valor;

    /**
     * Constructor.
     * Representa una condición de filtrado.
     *
     * @param columna La columna del registro a comparar.
     * @param valor   El valor buscado.
     */
    public CondicionIgualdad(int columna, String valor) {
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
        if (!registro.tieneValor(columna)) return false;

        String valorRegistro = registro.getValor(columna);

        try {
            double numRegistro = Double.parseDouble(valorRegistro);
            double numFiltro = Double.parseDouble(valor);
            return numRegistro == numFiltro;
        } catch (NumberFormatException e) {
            return valorRegistro.compareTo(valor) == 0;
        }
    }

    @Override
    public String toString() {
        return "Columna " + columna + " = " + valor;
    }
}
