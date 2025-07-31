package pjserrano.login.application.port.out;

import pjserrano.login.domain.UserPrincipal;

import java.util.function.Function;

/* Otro "puerto de salida" para generar un token.
La lógica de generación del token se puede expresar como una función que recibe
los datos de usuario (username, password, roles) y devuelve el token como un String.*/
public interface TokenServicePort extends Function<UserPrincipal, String> {}
