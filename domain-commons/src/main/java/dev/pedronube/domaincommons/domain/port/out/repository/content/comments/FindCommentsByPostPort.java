package dev.pedronube.domaincommons.domain.port.out.repository.content.comments;

import dev.pedronube.domaincommons.domain.model.content.Comment;

import java.util.List;
import java.util.function.Function;

public interface FindCommentsByPostPort extends Function<String, List<Comment>> {
}
