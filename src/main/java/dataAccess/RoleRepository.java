package dataAccess;

import models.RoleEntity;
import models.UserEntity;
import models.UserRoleEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class RoleRepository {
    private final EntityManager entityManager;

    public RoleRepository() {
        entityManager = DbFactory.getFactory().createEntityManager();
    }

    public Optional<List<RoleEntity>> getAllRole() {
        try {
            entityManager.clear();
            List<RoleEntity> roles = entityManager
                    .createQuery("SELECT r FROM role r WHERE r.isDelete = false", RoleEntity.class)
                    .getResultList();

            return Optional.of(roles);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Optional.empty();
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

    public Optional<RoleEntity> getRoleByName(String rolename) {
        try {
            entityManager.clear();
            RoleEntity role = entityManager
                    .createQuery("SELECT r FROM role r WHERE r.roleName = :rolename AND r.isDelete = false", RoleEntity.class)
                    .setParameter("rolename", rolename)
                    .getSingleResult();

            return Optional.of(role);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    public boolean addUserRole(UUID userId, int roleId) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            entityManager.clear();
            transaction.begin();
            UserRoleEntity entity = UserRoleEntity.builder()
                    .roleId(roleId)
                    .userId(userId)
                    .build();
            entityManager.persist(entity);
            transaction.commit();

            return true;
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        } finally {
            if (transaction.isActive()) {
                entityManager.flush();
            }
        }

        return false;
    }

    public boolean removeUserRole(UUID userId, int roleId) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.createQuery("DELETE userRole ur WHERE ur.userId = :userId AND ur.roleId = :roleId")
                    .setParameter("userId", userId)
                    .setParameter("roleId", roleId)
                    .executeUpdate();

            transaction.commit();

            return true;
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        } finally {
            if (transaction.isActive()) {
                entityManager.flush();
            }
        }

        return false;
    }
}
