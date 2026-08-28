package com.sena.database_connection.mesadeayuda.exception;

public class RefreshTokenInvalidoException extends RuntimeException {
    public RefreshTokenInvalidoException(String mensaje) {
        super(mensaje);
    }
}
