package pjserrano.authmanager.application.port.out;

import pjserrano.authmanager.domain.UserPrincipal;

import java.util.Optional;
import java.util.function.Function;

/* Este es un "puerto de salida" para interactuar con la BBDD.
Se define como una interfaz funcional porque la lógica de buscar por nombre
de usuario se puede expresar como una función que recibe un String y devuelve un Optional.
Define unicamente la interfaz funcional, la implementacion puede ser JDBC, R2DBC, MongoDB, DynamoDB... */
public interface UserRepositoryPort extends Function<String, Optional<UserPrincipal>> {}