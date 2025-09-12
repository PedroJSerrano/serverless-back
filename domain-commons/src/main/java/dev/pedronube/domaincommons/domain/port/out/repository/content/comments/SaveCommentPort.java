package dev.pedronube.domaincommons.domain.port.out.repository.content.comments;

import dev.pedronube.domaincommons.domain.model.content.Comment;

import java.util.function.Consumer;

public interface SaveCommentPort extends Consumer<Comment> {}
