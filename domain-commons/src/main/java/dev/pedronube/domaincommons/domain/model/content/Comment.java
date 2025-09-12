package dev.pedronube.domaincommons.domain.model.content;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Comment {
    private String id;
    private String content;
    private String authorId;
    private String createdAt;
}
