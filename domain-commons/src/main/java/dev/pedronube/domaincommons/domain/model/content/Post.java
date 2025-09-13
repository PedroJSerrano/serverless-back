package dev.pedronube.domaincommons.domain.model.content;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Post {
    private String id;
    private String title;
    private String content;
    private String videoUrl;
    private String imageUrl;
    private String documentUrl;
    private String authorId;
    private String createdAt;
}
