package Models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity(name = "captcha")
@Table(name = "`captcha`")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CaptchaEntity {
    @Id
    @Basic
    @Column(name = "`captcha_id`", nullable = false, updatable = false)
    private String captchaId;

    @Basic
    @Column(name = "`data`", nullable = false, updatable = false)
    private String data;

    @Basic
    @Column(name = "`create_at`", updatable = false)
    @CreationTimestamp
    private LocalDateTime createAt;

    @Basic
    @Column(name = "`create_by`")
    private String createBy;
}
