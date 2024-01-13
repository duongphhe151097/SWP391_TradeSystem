package DataAccess;

import Models.UserEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;

import java.util.Optional;
import java.util.UUID;

public class UserRepository {
    private final EntityManager entityManager;
    private final EntityTransaction transaction;

    public UserRepository() {
        EntityManagerFactory factory = DbFactory.getFactory();
        this.entityManager = factory.createEntityManager();
        this.transaction = entityManager.getTransaction();
    }

    public Optional<UserEntity> getUserById(UUID userId) {
        UserEntity entity = entityManager
                .createQuery("FROM user u where u.id = :id", UserEntity.class)
                .setParameter("id", userId)
                .getSingleResult();

        return Optional.ofNullable(entity);
    }

    public Optional<UserEntity> getUserByUsername(String username) {
        UserEntity entity = entityManager
                .createQuery("FROM user u where u.username = :username", UserEntity.class)
                .setParameter("username", username)
                .getSingleResult();

        return Optional.ofNullable(entity);
    }

    public Optional<UserEntity> getUserByEmail(String email) {
        UserEntity entity = entityManager
                .createQuery("FROM user u where u.username = :email", UserEntity.class)
                .setParameter("email", email)
                .getSingleResult();

        return Optional.ofNullable(entity);
    }

    public boolean addUser(UserEntity user) {
        try {
            transaction.begin();
            entityManager.persist(user);
            transaction.commit();

            return true;
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        }

        return false;
    }

    public boolean updateUserStatus(UUID userId, short status) {
        try {
            transaction.begin();
            entityManager.createQuery("UPDATE user u SET u.status = :status WHERE u.id = :id")
                    .setParameter("id", userId)
                    .setParameter("status", status)
                    .executeUpdate();
            transaction.commit();
            return true;
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        }
        return false;
    }
}
