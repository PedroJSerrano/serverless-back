package pjserrano.authmanager.domain.model;

import lombok.*;

import java.util.List;

/* Este Value Object encapsula toda la información de un usuario
que es relevante para la aplicación, incluyendo sus roles. Esto evita
que la lógica de negocio tenga que trabajar directamente con la entidad de la base de datos.*/
@Data
@ToString
@AllArgsConstructor
public class User {
    private String username;
    private String password;
    private String email;
    private List<String> roles;
}