package org.maquinavending.exceptions;

/**
 * Es el mensaje de error que sale cuando el importe para el comprar el producto no
 * sea suficiente.
 * @author Félix Hebles Jiménez
 * @version 1.0
 * Revisión: Iordache Mihai Laurentiu
 */

public class ImporteInsuficienteException extends Exception{

	public  ImporteInsuficienteException (String mensajerror) {
		super(mensajerror);
	}
}
