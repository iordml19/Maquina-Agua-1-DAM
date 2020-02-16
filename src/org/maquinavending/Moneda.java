package org.maquinavending;

/**
 * Clase que representa a una Moneda
 * @author Jesús
 * @version 1.0
 */
public class Moneda {
    private double valor;

    public Moneda(double valor){
        this.valor = valor;
    }

    public double getValor() {
        return valor;
    }

    @Override
    public String toString() {
        return "Moneda{" +
                "valor=" + valor +
                '}';
    }
}
