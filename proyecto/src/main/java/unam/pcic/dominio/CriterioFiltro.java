package unam.pcic.dominio;

import java.util.function.Supplier;


/**
 * - Encapsula condiciones de filtrado.
 * - Usa Patrón Builder para construir
 * - Valida y evalúa condiciones contra un RegistroCSV.
 */
public class CriterioFiltro<R> {
    /** La forma de seleccionar columnas en un almacen de registros */
    private final Seleccion<R> seleccion;

    /** Proveedor de almacen de registros */
    private final Supplier<Almacen<R>> proveedorAlmacen;

    /**
     * Constructor.
     * ¿Por qué se necesita el proveedor de almacen?
     *
     * @param seleccion La forma de seleccionar columnas en un almacen de registros.
     * @param proveedorAlmacen El proveedor de almacen de registros.
     */
    public CriterioFiltro(Seleccion<R> seleccion, Supplier<Almacen<R>> proveedorAlmacen) {
        this.seleccion = seleccion;
        this.proveedorAlmacen = proveedorAlmacen;
    }

    /**
     * Fábrica de filtros para registros row-oriented (RegistroCSV).
     *  Usa SeleccionRenglon y AlmacenRenglones.
     *
     * @return un filtro y selección para registros row-oriented (RegistroCSV).
     */
    public static CriterioFiltro<RegistroCSV> paraRegistroCSV() {
        return new CriterioFiltro<>(
                new SeleccionRenglon(),
                AlmacenRenglones::new
        );
    }

    /**
     * Selecciona columnas dadas de un almacen de registros.
     *
     * @param almacen El almacen de registros.
     * @param columnas Las columnas a seleccionar.
     * @return Un nuevo almacen de registros con las columnas seleccionadas.
     */
    public Almacen<R> seleccionarColumnas(Almacen<R> almacen, int[] columnas) {
        Almacen<R> seleccionAlmacen = proveedorAlmacen.get();

        for (R registro : almacen) {
            R reducido = this.seleccion.seleccionar(registro, columnas);
            seleccionAlmacen.agregar(reducido);
        }

        return seleccionAlmacen;
    }

    // Aquí se deben aplicar los filtros

}
