package org.maquinavending;

/**
 * Clase que representa una Botella de Agua
 * @author Jesús
 * @version 1.0
 */
public class BotellaAgua extends Producto {

    /**
     * Construye una botella
     * @param producto El tipo de la botella
     * @param precio El precio de la botella
     * @param peso El peso del de la botella
     */
    public BotellaAgua(TipoProducto producto, double precio, double peso) {
        super(producto, precio, peso);
    }

}
