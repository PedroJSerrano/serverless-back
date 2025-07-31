package pjserrano.login.infrastructure.adapter.out.persistence.model;


import lombok.Value;

import java.util.List;

@Value
public class UserDbEntity {
    String username;
    String password;
    List<String> roles; // Razón: Nueva propiedad para almacenar los roles
}
