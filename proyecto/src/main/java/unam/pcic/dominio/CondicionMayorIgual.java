package unam.pcic.dominio;

/**
 * Implementa la condición de 'mayor o igual' de registro según el valor de una columna.
 */
public class CondicionMayorIgual implements CondicionFiltro<RegistroCSV> {
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
    public CondicionMayorIgual(int columna, String valor) {
        this.columna = columna;
        this.valor = valor;
    }

    /**
     * Regresa true si el valor del registro dado es mayor o igual al valor buscado.
     *
     * @param registro El registro a comparar.
     * @return true si el valor del registro dado es mayor o igual al valor buscado, false en caso contrario.
     */
    public boolean cumple(RegistroCSV registro) {
        if (!registro.tieneValor(columna)) {
            return false;
        }

        String valorRegistro = registro.getValor(columna);

        // Intentar comparación primero
        try {
            double numRegistro = Double.parseDouble(valorRegistro);
            double numFiltro = Double.parseDouble(valor);
            return numRegistro >= numFiltro;
        } catch (NumberFormatException e) {
            // Si no son numéricos, comparar como cadenas
            return valorRegistro.compareTo(valor) >= 0;
        }
    }
}
