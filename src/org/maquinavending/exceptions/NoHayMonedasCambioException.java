package org.maquinavending.exceptions;

/**
 * Excepción lanzada en caso de no poder llegar al cambio solicitado por falta de monedas de
 * menor valor a pesar de existir cambio en la máquina
 * @author Iordache Mihai Laurentiu
 * @version 1.0
 */
public class NoHayMonedasCambioException extends Exception {

    public NoHayMonedasCambioException(String msg){
        super(msg);
    }
}
