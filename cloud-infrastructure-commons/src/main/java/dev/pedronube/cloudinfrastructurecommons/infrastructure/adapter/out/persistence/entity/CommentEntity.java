package dev.pedronube.cloudinfrastructurecommons.infrastructure.adapter.out.persistence.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@DynamoDbBean
public class CommentEntity extends BaseEntity {

    private String id;
    private String content;
    private String authorId;
    private String authorName;
    private String createdAt;

    // Constructor para un nuevo comentario
    public CommentEntity(String content, String authorId, String authorName) {
        this.id = UUID.randomUUID().toString();
        this.createdAt = Instant.now().toString();
        this.content = content;
        this.authorId = authorId;
        this.authorName = authorName;

        // Claves para anidar el comentario bajo su post
        setPk("POST#" + id);
        setSk("COMMENT#" + this.createdAt);
    }
}