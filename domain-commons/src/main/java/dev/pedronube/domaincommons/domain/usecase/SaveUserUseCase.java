package dev.pedronube.domaincommons.domain.usecase;

import dev.pedronube.domaincommons.domain.model.user.User;

import java.util.function.Consumer;

public interface SaveUserUseCase extends Consumer<User> {}
