package Models;

import Models.Common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "captcha")
@Table(name = "`captcha`")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CaptchaEntity extends BaseEntity {
    @Id
    @UuidGenerator(style = UuidGenerator.Style.RANDOM)
    @Column(name = "`id`", columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;

    @Basic
    @Column(name = "`captcha_id`", nullable = false, updatable = false)
    private String captchaId;

    @Basic
    @Column(name = "`data`", nullable = false, updatable = false)
    private String data;

    @Basic
    @Column(name = "`expried_at`", updatable = false)
    private LocalDateTime expriedAt;
}
