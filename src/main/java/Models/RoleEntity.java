package Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "role")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RoleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id", nullable = false, unique = true)
    private int roleId;

    @Column(name = "role_name", length = 50, nullable = false)
    private String roleName;

    @Column(name = "role_description")
    private String roleDescription;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "roleEntities")
    private Set<UserEntity> userEntities;
}
