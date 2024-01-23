package DataAccess;

import Models.UserEntity;
import Utils.Generators.StringGenerator;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.servlet.http.HttpServletRequest;

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
    public void updateUserPassword(UUID userId, String newPassword, String salt) {
        try {
            transaction.begin();

            UserEntity user = entityManager.find(UserEntity.class, userId);

            if (user == null) {
                System.out.println("User not found!");
                return;
            }

            String hashedPassword = StringGenerator.hashingPassword(newPassword, salt);

            user.setPassword(hashedPassword);
            user.setSalt(salt);
            entityManager.merge(user);

            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }


    private UserEntity getUserFromSession(HttpServletRequest request) {
        return (UserEntity) request.getSession().getAttribute("user");
    }

}
