package Models;

import jakarta.persistence.*;
import lombok.*;
import Models.Common.BaseEntity;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "notification")
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
    @Basic
    @Column(name = "`create_at`", updatable = false,nullable = false)
    @CreationTimestamp
    private LocalDateTime createAt;
}

