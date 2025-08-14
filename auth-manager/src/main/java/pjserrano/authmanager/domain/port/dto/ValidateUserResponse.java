package pjserrano.authmanager.domain.port.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
public class ValidateUserResponse {
    private String userId;
    private String jwtToken;
}
