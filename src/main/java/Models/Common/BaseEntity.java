package Models.Common;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
public abstract class BaseEntity {
    @Basic
    @Column(name = "`is_delete`")
    private boolean isDelete;

    @Basic
    @Column(name = "`create_at`", updatable = false)
    @CreationTimestamp
    private LocalDateTime createAt;

    @Basic
    @Column(name = "`create_by`")
    private String createBy;

    @Basic
    @Column(name = "`update_at`")
    @UpdateTimestamp
    private LocalDateTime updateAt;

    @Basic
    @Column(name = "`update_by`")
    private String updateBy;
}
