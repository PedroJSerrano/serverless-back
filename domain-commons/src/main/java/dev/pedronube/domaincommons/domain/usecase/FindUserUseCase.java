package dev.pedronube.domaincommons.domain.usecase;

import dev.pedronube.domaincommons.domain.model.user.User;

import java.util.Optional;
import java.util.function.Function;

public interface FindUserUseCase extends Function<String, Optional<User>> {}
