package pjserrano.login.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
public class UserSession {
    private String userId;
    private String jwtToken;
}
