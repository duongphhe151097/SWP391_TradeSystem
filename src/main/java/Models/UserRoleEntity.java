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
