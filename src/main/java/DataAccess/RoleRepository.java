package DataAccess;

import Models.RoleEntity;
import Models.UserEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class RoleRepository {
    private final EntityManager entityManager;
    private final EntityTransaction transaction;

    public RoleRepository() {
        entityManager = DbFactory.getFactory().createEntityManager();
        transaction = entityManager.getTransaction();
    }

    public Optional<Set<RoleEntity>> getRoleByUserId(UUID userId) {
        try {
            entityManager.clear();
            Set<RoleEntity> roles = entityManager
                    .getReference(UserEntity.class, userId)
                    .getRoleEntities();

            return Optional.of(roles);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    public static void main(String[] args) {
        RoleRepository roleRepository = new RoleRepository();
        Optional<Set<RoleEntity>> roleEntities = roleRepository
                .getRoleByUserId(UUID.fromString("349f0237-eef9-4c14-9f4a-a3d356b74a5e"));
        for (RoleEntity role : roleEntities.get()){
            System.out.println(role.getRoleName());
        }
    }
}
