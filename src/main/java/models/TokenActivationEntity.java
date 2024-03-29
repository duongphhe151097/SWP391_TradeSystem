package models;

import models.common.BaseEntity;
import models.entityKey.TokenActivationKey;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity(name = "tokenActive")
@Table(name = "`token_manager`")
@IdClass(TokenActivationKey.class)
@Builder
public class TokenActivationEntity extends BaseEntity {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.RANDOM)
    @Column(name = "`id`", columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;

    @Id
    @Column(name = "`token`", length = 50, nullable = false)
    private String token;

    @Id
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


//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "`user_id`", referencedColumnName = "`id`")
//    private UserEntity userEntity;


}
