package dev.pedronube.domaincommons.domain.port.out.repository.content.posts;

import dev.pedronube.domaincommons.domain.model.content.Post;

import java.util.Optional;
import java.util.function.Function;

public interface FindPostByIdPort extends Function<String, Optional<Post>> {}
