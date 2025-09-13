package dev.pedronube.cloudinfrastructurecommons.infrastructure.adapter.out.persistence.entity;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*;

@Setter
@EqualsAndHashCode
@NoArgsConstructor
@DynamoDbBean
public abstract class BaseEntity {
    private String pk;
    private String sk;
    private String gsi1pk;
    private String gsi1sk;

    @DynamoDbPartitionKey
    public String getPk() { return pk; }
    @DynamoDbSortKey
    public String getSk() { return sk; }
    @DynamoDbSecondaryPartitionKey(indexNames = "GSI1")
    public String getGsi1pk() { return gsi1pk; }
    @DynamoDbSecondarySortKey(indexNames = "GSI1")
    public String getGsi1sk() { return gsi1sk; }
}
