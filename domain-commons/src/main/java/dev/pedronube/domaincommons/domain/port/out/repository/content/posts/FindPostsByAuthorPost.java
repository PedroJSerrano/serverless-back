package dev.pedronube.domaincommons.domain.port.out.repository.content.posts;

import dev.pedronube.domaincommons.domain.model.content.Post;

import java.util.List;
import java.util.function.Function;

public interface FindPostsByAuthorPost extends Function<String, List<Post>> {}
