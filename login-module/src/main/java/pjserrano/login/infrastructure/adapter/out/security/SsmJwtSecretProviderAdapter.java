package pjserrano.login.infrastructure.adapter.out.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pjserrano.login.application.port.out.JwtSecretProviderPort;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ssm.SsmClient;
import software.amazon.awssdk.services.ssm.model.GetParameterRequest;
import software.amazon.awssdk.services.ssm.model.GetParameterResponse;

@Configuration
public class SsmJwtSecretProviderAdapter {

    @Value("${MY_AWS_REGION}")
    private String awsRegion;

    @Bean
    public JwtSecretProviderPort jwtSecretProvider() {
        SsmClient ssmClient = SsmClient.builder()
                .region(Region.of(awsRegion))
                .build();
        return () -> {
            try {
                GetParameterRequest parameterRequest = GetParameterRequest.builder()
                        .name("/login/jwt/secret")
                        .withDecryption(true) // Crucial para SecureString
                        .build();

                GetParameterResponse parameterResponse = ssmClient.getParameter(parameterRequest);
                return parameterResponse.parameter().value();
            } catch (Exception e) {
                System.err.println("Error al obtener el secreto JWT desde SSM: " + e.getMessage());
                throw new RuntimeException("No se pudo obtener el secreto JWT", e);
            }
        };
    }
}