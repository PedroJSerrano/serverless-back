package dev.pedronube.cloudinfrastructurecommons.infrastructure.adapter.out.persistence.mapper.impl;

import dev.pedronube.cloudinfrastructurecommons.infrastructure.adapter.out.persistence.entity.UserEntity;
import dev.pedronube.cloudinfrastructurecommons.infrastructure.adapter.out.persistence.mapper.IUserMapper;
import dev.pedronube.domaincommons.domain.model.user.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.function.Function;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UserMapper {

    private static final IUserMapper MAPPER = IUserMapper.INSTANCE;

    public static final Function<UserEntity, User> toDomain = MAPPER::toDomain;

    public static final Function<User, UserEntity> toEntity = MAPPER::toEntity;
}

