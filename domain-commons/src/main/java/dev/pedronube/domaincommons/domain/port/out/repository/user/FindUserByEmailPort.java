package dev.pedronube.domaincommons.domain.port.out.repository.user;

import dev.pedronube.domaincommons.domain.model.user.User;

import java.util.Optional;
import java.util.function.Function;

public interface FindUserByEmailPort extends Function<String, Optional<User>> {}
