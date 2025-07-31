package pjserrano.login.domain;

import lombok.Value;
import java.util.List;

// Razón: Este Value Object encapsula toda la información de un usuario
// que es relevante para la aplicación, incluyendo sus roles. Esto evita
// que la lógica de negocio tenga que trabajar directamente con la entidad de la base de datos.
@Value
public class UserPrincipal {
    String username;
    String password; // La contraseña es necesaria para la validación del login
    List<String> roles;
}