package dev.pedronube.usermanager.domain.port.out;

import dev.pedronube.usermanager.domain.model.User;

import java.util.Optional;
import java.util.function.Function;

public interface UpdateUserRepositoryPort extends Function<Optional<User>, Optional<User>> {
}
