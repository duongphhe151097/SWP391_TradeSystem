package DataAccess;

import Models.UserEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.servlet.http.HttpServletRequest;
import org.hibernate.Session;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UserRepository {
    private final EntityManager entityManager;
    private final EntityTransaction transaction;

    public UserRepository() {
        this.entityManager = DbFactory.getFactory().createEntityManager();
        this.transaction = entityManager.getTransaction();
    }

    public Optional<UserEntity> getUserById(UUID userId) {
        try {
            UserEntity entity = entityManager
                    .createQuery("FROM user u where u.id = :userId", UserEntity.class)
                    .setParameter("userId", userId)
                    .getSingleResult();

            return Optional.ofNullable(entity);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    public Optional<UserEntity> getUserByUsername(String username) {
        try {
            UserEntity entity = entityManager
                    .createQuery("FROM user u where u.username = :username", UserEntity.class)
                    .setParameter("username", username)
                    .getSingleResult();

            return Optional.ofNullable(entity);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    public Optional<UserEntity> getUserByEmail(String email) {
        try {
            UserEntity entity = entityManager
                    .createQuery("FROM user u where u.email = :email", UserEntity.class)
                    .setParameter("email", email)
                    .getSingleResult();

            return Optional.ofNullable(entity);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    public Optional<UserEntity> addUser(UserEntity user) {
        try {
            transaction.begin();
            UserEntity entity = (UserEntity) entityManager.merge(user);
            entityManager.persist(entity);

            transaction.commit();

            return Optional.of(entity);
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        }

        return Optional.empty();
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
    public void updateUser(UserEntity user) {
        try {
            transaction.begin();
            entityManager.merge(user);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        }
    }
    private UserEntity getUserFromSession(HttpServletRequest request) {
        return (UserEntity) request.getSession().getAttribute("user");
    }

}
