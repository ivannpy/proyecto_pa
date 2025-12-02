package unam.pcic.dominio;

/**
 * Interfaz para seleccionar columnas de un almacen.
 *
 * @param <R> El tipo de registros que guarda el almacen (RegistroCSV o ColumnaCSV).
 */
public interface Seleccion<R> {
    /**
     * Devuelve los valores de las columnas seleccionadas del registro dado.
     *
     * @param registro Registro de entrada
     * @param columnas Las columnas seleccionadas
     * @return Arreglo de Strings con solo las columnas seleccionadas, en el orden definido
     * por la implementación.
     */
    R seleccionar(R registro, int[] columnas);
}
