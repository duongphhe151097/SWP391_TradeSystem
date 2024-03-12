package models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import models.common.BaseEntity;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.UUID;

@Entity(name = "order")
@Table(name = "`order`")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OrderEntity extends BaseEntity implements Serializable {
    @Id
    @Column(name = "`id`", columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "`product_id`", columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID productId;

    @Column(name = "`user_id`", columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID userId;

    @Column(name = "`status`", nullable = false)
    private short status;

    @Column(name = "`fee`", nullable = false)
    private BigInteger fee;

    @Column(name = "`amount`", nullable = false)
    private BigInteger amount;
}
