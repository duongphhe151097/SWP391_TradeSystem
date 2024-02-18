package Models;

import Models.Common.BaseEntity;
import Models.EntityKey.ExternalTransactionKey;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

@Entity(name = "externalTrans")
@Table(name = "`external_transaction`")
@IdClass(ExternalTransactionKey.class)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ExternalTransactionEntity extends BaseEntity implements Serializable {
    @Id
    @Column(name = "`id`", columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;

    @Id
    @Column(name = "`type`", updatable = false, nullable = false, length = 20)
    private String type;

    @Column(name = "`command`", updatable = false, nullable = false)
    private short command;

    @Column(name = "`user_id`", updatable = false, nullable = false)
    private UUID userId;

    @Column(name = "`amount`", updatable = false, nullable = false)
    private BigDecimal amount;

    @Column(name = "`status`", updatable = false, nullable = false)
    private short status;
}
