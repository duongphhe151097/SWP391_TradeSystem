package models;

import jakarta.persistence.*;
import lombok.*;
import models.common.BaseEntity;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.UUID;

@Entity(name = "internalTrans")
@Table(name = "`internal_transaction`")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class InternalTransactionEntity extends BaseEntity implements Serializable {
    @Id
    @Column(name = "`id`", columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "`from`", columnDefinition = "uuid", updatable = false)
    private UUID from;

    @Column(name = "`to`", columnDefinition = "uuid", updatable = false)
    private UUID to;

    @Column(name = "`amount`", updatable = false, nullable = false)
    private BigInteger amount;

    @Column(name = "`description`", nullable = false)
    private String description;

    @Column(name = "`status`", nullable = false)
    private short status;
}
