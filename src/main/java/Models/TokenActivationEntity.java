package Models;

import Models.EntityKey.TokenActivationKey;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
public class TokenActivationEntity {

    @Id
    @Column(name = "`token`", length = 50, nullable = false)
    private String token;

    @Basic
    @Column(name = "`user_id`")
    private UUID user_id;

    @Basic
    @Column(name = "`create_at`", updatable = false)
    @CreationTimestamp
    private LocalDateTime createAt;

    @Basic
    @Column(name = "`expried_at`", updatable = false)
    private LocalDateTime expriedAt;
}
