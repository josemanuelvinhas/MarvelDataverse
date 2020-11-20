package com.dm.marveldataverse.core;

/**
 * Esta clase representa una excepción validando datos
 */
public class ValidationException extends Exception {

    private int error;

    /**
     * Método constructor de la excepción
     * @param message Mensaje significativo
     * @param error Número del error (se corresponde con R.string.(String del error) )
     */
    public ValidationException(String message, int error) {
        super(message);
        this.error = error;
    }

    /**
     * Método que devuelve el número de error
     * @return El número del error (se corresponde con R.string.(String del error) )
     */
    public int getError() {
        return error;
    }
}
