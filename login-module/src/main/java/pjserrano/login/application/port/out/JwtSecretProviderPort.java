package pjserrano.login.application.port.out;

import java.util.function.Supplier;

public interface JwtSecretProviderPort extends Supplier<String> { }