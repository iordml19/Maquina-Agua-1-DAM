package org.maquinavending.exceptions;

/**
 * Es el mensaje de error que sale cuando la moneda introducida en la máquina no es válida.
 * @author Félix Hebles Jiménez
 * @version 1.0
 * Revisión: Iordache Mihai Laurentiu
 */
 
public class MonedaNoValidaException extends Exception {
	public  MonedaNoValidaException (String mensajerror) {
		super(mensajerror);
	}
}
	

