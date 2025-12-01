package unam.pcic;

/**
 * Tipos de procesamiento
 */
public enum Procesamiento {
    /**
     * Para el procesamiento secuencial
     */
    SECUENCIAL {
        /**
         * Regresa el nombre del tipo de procesamiento
         *
         * @return el nombre del tipo de procesamiento
         */
        public String toString() {
            return "Secuencial";
        }
    },

    /**
     * Para el procesamiento concurrente
     */
    CONCURRENTE {
        /**
         * Regresa el nombre del tipo de procesamiento
         *
         * @return el nombre del tipo de procesamiento
         */
        public String toString() {
            return "Concurrente";
        }
    }
}
