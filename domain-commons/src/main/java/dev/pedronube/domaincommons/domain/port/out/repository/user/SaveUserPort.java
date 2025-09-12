package dev.pedronube.domaincommons.domain.port.out.repository.user;

import dev.pedronube.domaincommons.domain.model.user.User;

import java.util.function.Consumer;

public interface SaveUserPort extends Consumer<User> { }