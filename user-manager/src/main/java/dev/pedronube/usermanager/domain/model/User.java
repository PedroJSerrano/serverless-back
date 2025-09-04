package dev.pedronube.usermanager.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
@AllArgsConstructor
public class User {
    private String username;
    private String password;
    private String email;
    private List<String> roles;
}
