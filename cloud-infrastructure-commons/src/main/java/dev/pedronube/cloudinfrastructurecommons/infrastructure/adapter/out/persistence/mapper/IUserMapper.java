package dev.pedronube.cloudinfrastructurecommons.infrastructure.adapter.out.persistence.mapper;

import dev.pedronube.cloudinfrastructurecommons.infrastructure.adapter.out.persistence.entity.UserEntity;
import dev.pedronube.domaincommons.domain.model.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface IUserMapper {
    IUserMapper INSTANCE = Mappers.getMapper(IUserMapper.class);
//todo Revisar Gemini para los patrones
    UserEntity toEntity(User user);
    User toDomain(UserEntity entity);
}
