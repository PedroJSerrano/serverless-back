package dev.pedronube.userprofile.domain.port.out;

import dev.pedronube.userprofile.domain.model.User;

import java.util.Optional;
import java.util.function.Function;

public interface UpdateUserRepositoryPort extends Function<Optional<User>, Optional<User>> {
}
