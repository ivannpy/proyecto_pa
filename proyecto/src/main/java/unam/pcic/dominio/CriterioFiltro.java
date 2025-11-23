package unam.pcic.dominio;

import java.util.function.Supplier;


/**
 * - Encapsula condiciones de filtrado.
 * - Usa Patrón Builder para construir
 * - Valida y evalúa condiciones contra un RegistroCSV.
 */
public class CriterioFiltro<R> {
    /**  */
    private final Seleccion<R> seleccion;
    private final Supplier<Almacen<R>> proveedorAlmacen;


    public CriterioFiltro(Seleccion<R> seleccion, Supplier<Almacen<R>> proveedorAlmacen) {
        this.seleccion = seleccion;
        this.proveedorAlmacen = proveedorAlmacen;
    }

    /**
     * Fábrica para registros row-oriented (RegistroCSV).
     * Usa SeleccionRenglon y AlmacenRenglones.
     */
    public static CriterioFiltro<RegistroCSV> paraRegistroCSV() {
        return new CriterioFiltro<>(
                new SeleccionRenglon(),
                AlmacenRenglones::new
        );
    }

    public Almacen<R> seleccionarColumnas(Almacen<R> almacen, int[] columnas) {
        Almacen<R> seleccionAlmacen = proveedorAlmacen.get();

        for (R registro : almacen) {
            R reducido = this.seleccion.seleccionar(registro, columnas);
            seleccionAlmacen.agregar(reducido);
        }

        return seleccionAlmacen;
    }


}
