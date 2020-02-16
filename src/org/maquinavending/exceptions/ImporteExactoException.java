package org.maquinavending.exceptions;

/**
 * Es el mensaje que sale cuando la máquina no tenga cambio para devolver.
 * @author Félix Hebles Jiménez
 * @version 1.0
 * Revisión: Iordache Mihai Laurentiu
 */

public class ImporteExactoException extends Exception{

	public ImporteExactoException (String mensaje) {
		super(mensaje);
	}
}


