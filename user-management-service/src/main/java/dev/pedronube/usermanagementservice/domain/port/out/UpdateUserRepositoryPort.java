package dev.pedronube.usermanagementservice.domain.port.out;

import dev.pedronube.usermanagementservice.domain.model.User;

import java.util.Optional;
import java.util.function.Function;

public interface UpdateUserRepositoryPort extends Function<Optional<User>, Optional<User>> {
}
