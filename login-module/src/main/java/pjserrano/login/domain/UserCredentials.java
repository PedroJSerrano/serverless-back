package pjserrano.login.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
//@Value sin ninguna de las anteriores?
public class UserCredentials {
    private String username;
    private String password;
}
