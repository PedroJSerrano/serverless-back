package dev.pedronube.authenticationservice.domain.port.out;

import java.util.function.Supplier;

public interface JwtSecretProviderPort extends Supplier<String> { }