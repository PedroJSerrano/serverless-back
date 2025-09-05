package dev.pedronube.usermanagementservice.domain.port.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CreateUserCommand {
    private String username;
    private String password;
    private String email;
    private List<String> roles;
}
