package Models;

import Models.EntityKey.TokenActivationKey;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity(name = "tokenActive")
@Table(name = "`token_activation`")
@IdClass(TokenActivationKey.class)
@Builder
public class TokenActivationEntity {

    @Id
    @Column(name = "`token`", length = 50, nullable = false)
    private String token;

    @Basic
    @Column(name = "`user_id`", nullable = false)
    private UUID userId;

    @Basic
    @Column(name = "`type`", nullable = false)
    private short type;

    @Basic
    @Column(name = "`is_used`")
    private boolean isUsed;

    @Basic
    @Column(name = "`create_at`", updatable = false)
    @CreationTimestamp
    private LocalDateTime createAt;

    @Basic
    @Column(name = "`expried_at`", updatable = false)
    private LocalDateTime expriedAt;

    @ManyToOne(fetch = FetchType.EAGER)
    private UserEntity userEntity;
}
