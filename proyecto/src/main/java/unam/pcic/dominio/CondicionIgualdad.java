package unam.pcic.dominio;

public class CondicionIgualdad implements CondicionFiltro {
    private final int columna;
    private final String valor;

    public CondicionIgualdad(int columna, String valor) {
        this.columna = columna;
        this.valor = valor;
    }

    public boolean cumple(RegistroCSV registro) {
        return registro.tieneValor(columna) && registro.getValor(columna).equals(valor);
    }
}
