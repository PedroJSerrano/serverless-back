package pjserrano.authmanager.domain.port.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
public class ValidateUserCommand {
    private String username;
    private String password;
}
