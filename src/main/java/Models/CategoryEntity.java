package Models;

import Models.Common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.io.Serializable;

@Entity(name = "category")
@Table(name = "category")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CategoryEntity extends BaseEntity implements Serializable {
    @Id
    @Column(name = "`id`", updatable = false, nullable = false)
    private int id;

    @Column(name = "`parent_id`")
    private int parentId;

    @Column(name = "`title`", nullable = false, length = 100)
    private String title;
}
