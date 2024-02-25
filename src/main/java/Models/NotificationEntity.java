package Models;

import Models.Common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.io.Serializable;
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

    @Column(name = "`user_id`", updatable = false)
    private String userId;

    @Column(name = "`title`", nullable = false)
    private String title;

    @Column(name = "`description`", nullable = false)
    private String description;

    @Column(name = "`status`", nullable = false)
    private boolean status;

    @Column(name = "`create_date`", updatable = false, nullable = false, length = 14)
    private String createDate;

}

