package dev.pedronube.cognitointegration.application.service;

import dev.pedronube.domaincommons.domain.port.out.repository.user.SaveUserPort;
import dev.pedronube.domaincommons.domain.usecase.SaveUserUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SaveUserFromCognitoService {

    @Bean
    public SaveUserUseCase saveUser(SaveUserPort saveUserPort) {
        return saveUserPort::accept;
    }
}
