package dev.pedronube.cloudinfrastructurecommons.infrastructure.adapter.out.persistence.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@DynamoDbBean
public class ReservationEntity extends BaseEntity {

    private String classTitle;
    private String slotId; // Clave para el GSI
    private String status;
    private String createdAt;

    // Constructor para una nueva reserva
    public ReservationEntity(String userId, String slotId, String classTitle) {
        String userPk = "USER#" + userId;
        this.createdAt = Instant.now().toString();
        this.status = "CONFIRMED";
        this.slotId = slotId;
        this.classTitle = classTitle;

        // Claves de la tabla principal (para ver las reservas de un usuario)
        setPk(userPk);
        setSk("RESERVATION#" + this.slotId);

        // Claves para el GSI (para ver los inscritos a una clase)
        setGsi1pk(this.slotId);
        setGsi1sk(userPk);
    }
}