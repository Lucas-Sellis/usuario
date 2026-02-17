package com.example.usuario.infrastructure.exceptions;


import org.springframework.security.core.AuthenticationException;

public class UnauthorizedExeception extends AuthenticationException {
    public UnauthorizedExeception(String message) {
        super(message);
    }
}
