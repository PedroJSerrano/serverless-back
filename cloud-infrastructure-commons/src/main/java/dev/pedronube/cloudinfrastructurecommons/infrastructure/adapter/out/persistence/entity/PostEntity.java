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
public class PostEntity extends BaseEntity {

    private String id;
    private String title;
    private String content;
    private String imageUrl;
    private String videoUrl;
    private String documentUrl;
    private String authorId;
    private boolean published;
    private String createdAt;
    private String entityType; // Para el GSI

    // Constructor para crear un nuevo post
    public PostEntity(String title, String content, String authorId) {
        this.id = UUID.randomUUID().toString();
        this.createdAt = Instant.now().toString();
        this.entityType = "POST";
        this.published = false; // Por defecto, se crea como borrador
        this.title = title;
        this.content = content;
        this.authorId = authorId;

        // Claves de la tabla principal
        setPk("POST#" + id);
        setSk("METADATA");

        // Claves para el GSI (listar todos los posts)
        setGsi1pk(this.entityType);
        setGsi1sk(this.createdAt);
    }
}