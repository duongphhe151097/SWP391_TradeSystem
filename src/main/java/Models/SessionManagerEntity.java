package Models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "`session_manager`")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SessionManagerEntity {
    @Id
    @Column(name = "`session_id`", length = 32, nullable = false)
    private String sessionId;

    @Basic
    @Column(name = "`user_id`", nullable = false)
    private UUID userId;

    @Basic
    @Column(name = "`create_at`", updatable = false)
    @CreationTimestamp
    private LocalDateTime createAt;

    @Basic
    @Column(name = "`last_active`", updatable = false)
    private LocalDateTime lastActive;

    @Basic
    @Column(name = "`create_by`")
    private String createBy;

}
