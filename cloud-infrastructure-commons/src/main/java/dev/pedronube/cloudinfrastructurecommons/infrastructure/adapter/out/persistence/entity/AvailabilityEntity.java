package dev.pedronube.cloudinfrastructurecommons.infrastructure.adapter.out.persistence.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@DynamoDbBean
public class AvailabilityEntity extends BaseEntity {

    private String classTitle;
    private int durationMinutes;
    private int capacity;
    private int spotsAvailable;
    private String startTime; // Formato ISO 8601 YYYY-MM-DDTHH:mm:ssZ

    // Constructor para un nuevo horario disponible
    public AvailabilityEntity(String classTitle, String startTime, int durationMinutes, int capacity) {
        this.classTitle = classTitle;
        this.startTime = startTime;
        this.durationMinutes = durationMinutes;
        this.capacity = capacity;
        this.spotsAvailable = capacity; // Inicialmente, todas las plazas están disponibles

        // Claves para agrupar todas las disponibilidades
        setPk("AVAILABILITY");
        setSk("SLOT#" + this.startTime);
    }
}