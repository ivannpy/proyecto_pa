package unam.pcic.dominio;


/**
 * Almacen para almacenar registros.
 *  Representa un archivo CSV completo.
 *
 * @param <R> Tipo genérico para RegistroCSV o ColumnaCSV.
 */
public interface Almacen<R> extends Iterable<R> {
    /**
     * Obtiene cuántos registros tiene el almacén
     *
     * @return cantidad de registros en el almacén.
     */
    int getLongitud();

    /**
     * Obtiene el registro en la posición dada.
     *
     * @param indice el indice del registro.
     */
    R get(int indice);

    /**
     * Agrega un registro al almacén.
     *
     * @param registro el registro a agregar.
     */
    void agregar(R registro);

}
