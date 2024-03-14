package models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import models.common.BaseEntity;

import java.io.Serializable;
import java.util.UUID;

@Entity(name = "user_rp")
@Table(name = "`user_report`")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserReportEntity extends BaseEntity implements Serializable {
    @Id
    @Column(name = "`id`", columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "`user_id`", columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID userId;

    @Column(name = "`product_target`", columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID productTarget;

    @Column(name = "`type`", nullable = false)
    private short type;

    @Column(name = "`title`", length = 500, nullable = false)
    private String title;

    @Column(name = "`description`", nullable = false)
    private String description;

    @Column(name = "`admin_response`")
    private String adminResponse;

    @Column(name = "`status`", nullable = false)
    private short status;
}
