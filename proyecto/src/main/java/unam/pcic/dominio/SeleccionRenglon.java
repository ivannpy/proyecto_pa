package unam.pcic.dominio;


/**
 * Implementa la selección de columnas de un almacen de registros (RegistroCSV).
 */
public class SeleccionRenglon implements Seleccion<RegistroCSV> {

    /**
     * Selecciona columnas dadas de un RegistroCSV.
     *
     * @param registro El registro de entrada.
     * @param columnas Las columnas a seleccionar.
     * @return Un nuevo registro solo con las columnas seleccionadas.
     */
    @Override
    public RegistroCSV seleccionar(RegistroCSV registro, int[] columnas) {
        String[] valoresSeleccionados = new String[columnas.length];

        for (int i = 0; i < columnas.length; i++) {
            valoresSeleccionados[i] = registro.getValor(columnas[i]);
        }

        return new RegistroCSV(valoresSeleccionados, registro.getNumeroLinea());
    }
}
