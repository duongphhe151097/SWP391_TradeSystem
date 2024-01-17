package Models.Common;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
@EntityListeners(AuditListener.class)
public abstract class BaseEntity {
    @Basic
    @Column(name = "`is_delete`")
    private boolean isDelete;

    @Basic
    @Column(name = "`create_at`", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createAt;

    @Basic
    @Column(name = "`create_by`", nullable = false, updatable = false)
    private String createBy;

    @Basic
    @Column(name = "`update_at`")
    @UpdateTimestamp
    private LocalDateTime updateAt;

    @Basic
    @Column(name = "`update_by`")
    private String updateBy;
}
