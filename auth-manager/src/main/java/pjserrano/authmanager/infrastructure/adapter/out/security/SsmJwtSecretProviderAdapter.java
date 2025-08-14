package pjserrano.authmanager.infrastructure.adapter.out.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pjserrano.authmanager.domain.port.out.JwtSecretProviderPort;
import software.amazon.awssdk.services.ssm.SsmClient;
import software.amazon.awssdk.services.ssm.model.GetParameterRequest;
import software.amazon.awssdk.services.ssm.model.GetParameterResponse;
import software.amazon.awssdk.services.ssm.model.Parameter;

import java.util.Optional;

@Configuration
@RequiredArgsConstructor
public class SsmJwtSecretProviderAdapter {

    private final SsmClient ssmClient; // Inyectado automáticamente por Spring Cloud AWS

    @Bean
    public JwtSecretProviderPort jwtSecretProvider() {
        GetParameterRequest request = GetParameterRequest.builder()
                .name("/login/jwt/secret")
                .withDecryption(true) // Crucial para SecureString
                .build();
                
        return () -> Optional.ofNullable(ssmClient.getParameter(
                    request
                ))
                            .map(GetParameterResponse::parameter)
                            .map(Parameter::value)
                            .orElseThrow(() -> new RuntimeException("No se pudo obtener el secreto JWT"));
    }
}