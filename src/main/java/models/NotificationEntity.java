package models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import models.common.BaseEntity;
import org.hibernate.annotations.UuidGenerator;

import java.io.Serializable;
import java.util.UUID;

@Entity(name = "notifi")
@Table(name = "notification")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class NotificationEntity extends BaseEntity implements Serializable {
    @Id
    @UuidGenerator(style = UuidGenerator.Style.RANDOM)
    @Column(name = "`id`", columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "`user_fired_notify`", nullable = false)
    private UUID userFriedNotify;

    @Column(name = "`user_to_notify`")
    private UUID userToNotify;

    @Column(name = "`type`", nullable = false)
    private short type;

    @Column(name = "`message`", nullable = false, length = 500)
    private String message;

    @Column(name = "`is_seen`", nullable = false)
    private boolean isSeen;
}
