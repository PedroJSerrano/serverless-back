package dev.pedronube.domaincommons.domain.model.user;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class User {
    private String sub;
    private String username;
    private String email;
    private String subscriptionLevel;
    private String createdAt;
}
