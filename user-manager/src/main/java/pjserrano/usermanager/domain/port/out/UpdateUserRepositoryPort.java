package pjserrano.usermanager.domain.port.out;

import pjserrano.usermanager.domain.model.User;

import java.util.Optional;
import java.util.function.Function;

public interface UpdateUserRepositoryPort extends Function<Optional<User>, Optional<User>> {
}
