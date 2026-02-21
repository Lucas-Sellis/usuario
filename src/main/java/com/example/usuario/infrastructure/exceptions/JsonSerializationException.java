package com.example.usuario.infrastructure.exceptions;

public class JsonSerializationException extends RuntimeException {

    public JsonSerializationException(String message, Throwable cause) {
        super(message, cause);
    }

}
