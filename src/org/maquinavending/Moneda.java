package org.maquinavending;

/**
 * Clase que representa a una Moneda
 * @author Jesús Reyes Carrillo
 * @version 1.0
 */
public final class Moneda{
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
