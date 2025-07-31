package pjserrano.login.application.port.in;

import pjserrano.login.domain.UserCredentials;
import pjserrano.login.domain.UserSession;

import java.util.function.Function;

// Razón: Este es el "puerto de entrada". Se define como una interfaz funcional
// para que la implementación pueda ser una simple lambda. Es la firma del method
// que queremos que la aplicación exponga. El mundo exterior solo debe conocer
// esta interfaz, no la implementación.
public interface LoginUserUseCase extends Function<UserCredentials, UserSession> {}
