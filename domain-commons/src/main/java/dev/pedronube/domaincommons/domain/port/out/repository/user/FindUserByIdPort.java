package dev.pedronube.domaincommons.domain.port.out.repository.user;

import dev.pedronube.domaincommons.domain.model.user.User;

import java.util.Optional;
import java.util.function.Function;

public interface FindUserByIdPort extends Function<String, Optional<User>> {}
