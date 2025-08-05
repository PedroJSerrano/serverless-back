package pjserrano.authmanager.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
public class UserCredentials {
    private String username;
    private String password;
}
