package unam.pcic.dominio;

/**
 * - Debe ser implementado por los filtros
 * Posibles filtros:
 * - Filtro por valor constante
 * - Mayor que, menor que, mayor o igual que, menor o igual que.
 */
public interface CondicionFiltro<R> {
    /**
     * Indica si el registro cumple la condición de este filtro.
     *
     * @param registro Registro a evaluar
     * @return true si el registro cumple la condición, false en caso contrario.
     */
    boolean cumple(R registro);
}
