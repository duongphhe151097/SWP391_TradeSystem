package Models;

import Models.Common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;
import java.util.UUID;

@Entity(name = "userRole")
@Table(name = "user_role")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserRoleEntity extends BaseEntity {
    @Id
    @Column(name = "role_id", nullable = false)
    private int roleId;

    @Id
    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserRoleEntity userRole = (UserRoleEntity) o;
        return roleId == userRole.roleId && Objects.equals(userId, userRole.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roleId, userId);
    }

    //    @ManyToOne(fetch = FetchType.EAGER)
//    @JoinColumn(
//            name = "user_id",
//            nullable = false,
//            foreignKey = @ForeignKey(name = "fk_user_user-role")
//    )
//    private UserEntity userEntity;
//
//    @ManyToOne(fetch = FetchType.EAGER)
//    @JoinColumn(
//            name = "role_id",
//            nullable = false,
//            foreignKey = @ForeignKey(name = "fk_role_user-role")
//    )
//    private RoleEntity roleEntity;
}
