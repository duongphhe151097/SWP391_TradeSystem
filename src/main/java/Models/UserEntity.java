package Models;

import Models.Common.BaseEntity;
import jakarta.persistence.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor

@Getter
@Setter
@Entity(name = "user")
@Table(name = "`user`")
@Builder
public class UserEntity extends BaseEntity implements Serializable {
    @Id
    @UuidGenerator(style = UuidGenerator.Style.RANDOM)
    @Column(name = "`id`", columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "`username`", length = 25, nullable = false, updatable = false)
    private String username;

    @Column(name = "`password`", nullable = false)
    private String password;

    @Column(name = "`salt`", length = 50, nullable = false)
    private String salt;

    @Column(name = "`email`", length = 350, nullable = false)
    private String email;

    @Column(name = "`fullname`", length = 500, nullable = false)
    private String fullName;

    @Column(name = "`phone_number`", length = 18, nullable = false)
    private String phoneNumber;

    @Column(name = "`address`", length = 1000)
    private String address;

    @Column(name = "`avatar`", length = 1000)
    private String avatar;

    @Column(name = "`status`", nullable = false)
    private short status;

    @Column(name = "`attemp`", nullable = false)
    private short attemp;

    @Column(name = "`rating`")
    private float rating;

    @Column(name = "`balance`")
    private BigDecimal balance;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "user_role",
            joinColumns = {@JoinColumn(name = "user_id", nullable = false)},
            inverseJoinColumns = {@JoinColumn(name = "role_id", nullable = false)}
    )
    private Set<RoleEntity> roleEntities;

//    @OneToMany(fetch = FetchType.LAZY, mappedBy = "userEntity")
//    private List<TokenActivationEntity> tokenActivationEntities;
//
//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "`id`",referencedColumnName = "`user_id`", insertable = false, updatable = false)
//    private SessionManagerEntity sessionManager;
}
