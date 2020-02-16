package org.maquinavending;

import org.maquinavending.exceptions.ImporteExactoException;
import org.maquinavending.exceptions.ImporteInsuficienteException;
import org.maquinavending.exceptions.MonedaNoValidaException;

import java.util.ArrayList;

/**
 * Interfaz utilizada por la máquina para indicar la compra de un producto.
 * @author Iordache Mihai Laurentiu
 * @version 1.0
 * Revisión: Iordache Mihai Laurentiu
 */
public interface Comprable {
    /**
     * Método público que no retorna nada. En él se establecerá la lógica de compra
     * @param producto El producto a comprar.
     * @param monedas Las monedas del comprador
     * @throws MonedaNoValidaException Lanzada en caso de que el comprador no cumpla con los requisitos
     * de monedas aceptadas.
     * @throws ImporteInsuficienteException Lanzada en caso de que el importe del comprador sea insuficiente.
     * @throws ImporteExactoException Lanzada cuando el artefacto/vendedor no disponga de cambio
     */
    void comprar(Producto producto, ArrayList<Moneda> monedas) throws MonedaNoValidaException, ImporteInsuficienteException, ImporteExactoException;
}
