package unam.pcic.dominio;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * Almacen de renglones de un archivo CSV (RegistroCSV).
 */
public class AlmacenRenglones implements Almacen<RegistroCSV> {
    /** Lista de registros almacenados. */
    private List<RegistroCSV> registros;

    /** Cuántos registros hay en el almacen. */
    private int longitud;

    /**
     * Constructor por defecto (almacen vacío).
     */
    public AlmacenRenglones() {
        this(new ArrayList<>());
    }

    /**
     * Constructor para una lista de registros (RegistroCSV) dada.
     *
     * @param registros La lista de registros que conforman al almacen.
     */
    public AlmacenRenglones(List<RegistroCSV> registros) {
        this.registros = registros;
        this.longitud = registros.size();
    }

    /**
     * Regresa la lista de registros almacenados.
     *
     * @return la lista de registros almacenados.
     */
    public List<RegistroCSV> getRegistros() {
        return registros;
    }

    /**
     * Modifica la lista de registros almacenados.
     *
     * @param registros La nueva lista de registros.
     */
    public void setRegistros(List<RegistroCSV> registros) {
        this.registros = registros;
    }

    /**
     * Regresa la longitud del almacen (i.e. la cantidad de registros almacenados).
     *
     * @return La longitud del almacen.
     */
    @Override
    public int getLongitud() {
        return longitud;
    }

    /**
     * Regresa el registro en la posición dada.
     *
     * @param indice el indice del registro.
     * @return El registro en la posición dada por el índice
     */
    @Override
    public RegistroCSV get(int indice){
        return registros.get(indice);
    }

    /**
     * Agrega un registro al almacen.
     *
     * @param registro el registro a agregar.
     */
    @Override
    public void agregar(RegistroCSV registro) {
        registros.add(registro);
        longitud++;
    }

    /**
     * Regresa un iterador para iterar sobre los registros del almacen.
     *
     * @return un iterador para iterar sobre los registros del almacen.
     */
    @Override
    public Iterator<RegistroCSV> iterator() {
        return registros.iterator();
    }


}
