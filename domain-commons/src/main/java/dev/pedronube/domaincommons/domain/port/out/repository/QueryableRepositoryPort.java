package dev.pedronube.domaincommons.domain.port.out.repository;

import java.util.List;

public interface QueryableRepositoryPort<T> extends GenericRepositoryPort<T, String> {
    List<T> findByAttribute(String attributeName, String value);
    List<T> findWithPagination(String lastKey, int limit);
}
