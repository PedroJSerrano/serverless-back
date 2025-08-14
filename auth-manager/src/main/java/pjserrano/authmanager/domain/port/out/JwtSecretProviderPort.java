package pjserrano.authmanager.domain.port.out;

import java.util.function.Supplier;

public interface JwtSecretProviderPort extends Supplier<String> { }