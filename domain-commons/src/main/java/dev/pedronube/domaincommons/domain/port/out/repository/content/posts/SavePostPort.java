package dev.pedronube.domaincommons.domain.port.out.repository.content.posts;

import dev.pedronube.domaincommons.domain.model.content.Post;

import java.util.function.Consumer;

public interface SavePostPort extends Consumer<Post> {}
