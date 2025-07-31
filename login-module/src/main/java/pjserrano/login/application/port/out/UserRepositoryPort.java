package pjserrano.login.application.port.out;

import pjserrano.login.domain.UserPrincipal;

import java.util.Optional;
import java.util.function.Function;

// Razón: Este es un "puerto de salida" para interactuar con la BBDD.
// Se define como una interfaz funcional porque la lógica de buscar por nombre
// de usuario se puede expresar como una función que recibe un String y devuelve un Optional.
public interface UserRepositoryPort extends Function<String, Optional<UserPrincipal>> {}