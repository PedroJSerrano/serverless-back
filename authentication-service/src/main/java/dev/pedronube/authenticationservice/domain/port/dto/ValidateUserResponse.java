package dev.pedronube.authenticationservice.domain.port.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
public class ValidateUserResponse {
    private String username;
    private String jwtToken;
}
