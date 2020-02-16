package org.maquinavending.exceptions;

/**
 * Es el mensaje que se muestra cuando la máquina no dispone de productos.
 * @author Félix Hebles Jiménez
 * @version 1.0
 * Revisión: Iordache Mihai Laurentiu
 */

public class MaquinaVaciaException extends Exception {

	public MaquinaVaciaException(String mensaje) {
		super(mensaje);

	}

}
