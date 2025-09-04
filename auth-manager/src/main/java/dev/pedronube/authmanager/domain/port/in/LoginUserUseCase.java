package dev.pedronube.authmanager.domain.port.in;

import dev.pedronube.authmanager.domain.port.dto.ValidateUserCommand;
import dev.pedronube.authmanager.domain.port.dto.ValidateUserResponse;

import java.util.function.Function;

/* Este es el "puerto de entrada". Se define como una interfaz funcional
para que la implementación pueda ser una simple lambda. Es la firma del method
que queremos que la aplicación exponga. El mundo exterior solo debe conocer
esta interfaz, no la implementación.*/
public interface LoginUserUseCase extends Function<ValidateUserCommand, ValidateUserResponse> {}
