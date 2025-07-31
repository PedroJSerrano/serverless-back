package pjserrano.login.domain;

import lombok.Value;
import java.util.List;

/* Este Value Object encapsula toda la información de un usuario
que es relevante para la aplicación, incluyendo sus roles. Esto evita
que la lógica de negocio tenga que trabajar directamente con la entidad de la base de datos.*/
@Value
public class UserPrincipal {
    private String username;
    private String password;
    private List<String> roles;
}