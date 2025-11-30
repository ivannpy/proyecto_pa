package unam.pcic.dominio;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;


/**
 * - Encapsula condiciones de filtrado.
 * - Usa Patrón Builder para construir
 * - Valida y evalúa condiciones contra un RegistroCSV.
 */
public class CriterioFiltro<R> {
    /**
     * La forma de seleccionar columnas en un almacen de registros
     */
    private final Seleccion<R> seleccion;

    /**
     * Columnas que se van a seleccionar
     */
    private final int[] columnasSeleccionadas;

    /**
     * Lista de filtros que se van a aplicar
     */
    private final List<CondicionFiltro<R>> filtros;

    /**
     * Proveedor de almacen de registros
     */
    private final Supplier<Almacen<R>> proveedorAlmacen;

    /**
     * Constructor.
     * ¿Por qué se necesita el proveedor de almacen?
     *
     * @param seleccion        La forma de seleccionar columnas en un almacen de registros.
     * @param proveedorAlmacen El proveedor de almacen de registros.
     */
    public CriterioFiltro(Seleccion<R> seleccion,
                          int[] columnas,
                          List<CondicionFiltro<R>> filtros,
                          Supplier<Almacen<R>> proveedorAlmacen) {
        this.seleccion = seleccion;
        this.columnasSeleccionadas = columnas;
        this.filtros = filtros;
        this.proveedorAlmacen = proveedorAlmacen;
    }

    /**
     * Fábrica de filtros para registros row-oriented (RegistroCSV).
     * Usa SeleccionRenglon y AlmacenRenglones.
     *
     * @return un filtro y selección para registros row-oriented (RegistroCSV).
     */
    public static CriterioFiltro<RegistroCSV> paraRegistroCSV(int[] columnas,
                                                              List<CondicionFiltro<RegistroCSV>> filtros) {
        return new CriterioFiltro<>(
                new SeleccionRenglon(),
                columnas,
                filtros,
                AlmacenRenglones::new
        );
    }

    /**
     * Selecciona columnas dadas de un almacen de registros.
     *
     * @param almacen El almacen de registros.
     * @return Un nuevo almacen de registros con las columnas seleccionadas.
     */
    public Almacen<R> seleccionarColumnas(Almacen<R> almacen) {
        Almacen<R> seleccionAlmacen = proveedorAlmacen.get();

        for (R registro : almacen) {
            R reducido = seleccionarColumnas(registro);
            seleccionAlmacen.agregar(reducido);
        }

        return seleccionAlmacen;
    }

    /**
     * Seleccionar las columnas dadas de un registro.
     *
     * @param registro El registro.
     * @return Un nuevo registro con las columnas seleccionadas.
     */
    public R seleccionarColumnas(R registro) {
        return this.seleccion.seleccionar(registro, columnasSeleccionadas);
    }

    /**
     * Aplica un filtro a un registro.
     *
     * @param registro El registro a filtrar.
     * @param filtro El filtro a aplicar.
     * @return true si el registro cumple la condición del filtro, false en caso contrario.
     */
    public boolean aplicarFiltro(R registro, CondicionFiltro<R> filtro) {
        return filtro.cumple(registro);
    }

    /**
     * Aplica todos los filtros a un registro.
     *
     * @param registro El registro a filtrar.
     * @return true si el registro cumple todas las condiciones de los filtros, false en caso contrario.
     */
    public boolean aplicarFiltros(R registro) {
        if (filtros.isEmpty()) return true;

        boolean noCumpleAlguna = false;
        for (CondicionFiltro<R> condicion : filtros) {
            if (!aplicarFiltro(registro, condicion)) {
                noCumpleAlguna = true;
                break;
            }
        }
        return !noCumpleAlguna;
    }

    @Override
    public String toString() {
        return "CriterioFiltro(" + ", columnasSeleccionadas={"
                + Arrays.toString(columnasSeleccionadas) + "}, filtros={" + filtros.toString() + "})";
    }
}
