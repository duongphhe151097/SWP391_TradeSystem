package Models;

import Models.Common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity(name = "role")
@Table(name = "`role`")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class RoleEntity extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`role_id`", nullable = false, unique = true)
    private int roleId;

    @Column(name = "`role_name`", length = 50, nullable = false)
    private String roleName;

    @Column(name = "`role_description`")
    private String roleDescription;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "roleEntities")
    private Set<UserEntity> userEntities;
}
