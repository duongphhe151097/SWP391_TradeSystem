package DataAccess;

import Models.UserEntity;
import Utils.Validation.StringValidator;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UserRepository {
    private final EntityManager entityManager;

    public UserRepository() {
        this.entityManager = DbFactory.getFactory().createEntityManager();
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
        EntityTransaction transaction = entityManager.getTransaction();
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
        EntityTransaction transaction = entityManager.getTransaction();
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
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();

            UserEntity user = entityManager.find(UserEntity.class, userId);

            if (user == null) {
                System.out.println("User not found!");
                return;
            }

            user.setPassword(newPassword);
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

    public void updateUserProfile(UUID userId, String username, String fullname, String phone_number) {
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();

            entityManager.createQuery("UPDATE user u SET u.username = :username, u.fullName = :fullname, u.phoneNumber = :phone_number WHERE u.id = :id")
                    .setParameter("id", userId)
                    .setParameter("username", username)
                    .setParameter("fullname", fullname)
                    .setParameter("phone_number", phone_number)
                    .executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        }
    }

    public void updateUserBalance(UUID userId, BigDecimal balance) {
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();

            entityManager.createQuery("UPDATE user u SET u.balance = :balance WHERE u.id = :id")
                    .setParameter("id", userId)
                    .setParameter("balance", balance)
                    .executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        }
    }

    public long countAll(String search, String status, LocalDateTime startDate, LocalDateTime endDate) {
        StringBuilder hql = new StringBuilder();
        hql.append("SELECT COUNT(*) FROM user u ");

        if (StringValidator.isValidEmail(search)) {
            hql.append("WHERE u.email LIKE :email ");
        } else {
            hql.append("WHERE u.username LIKE :username ");
        }

        if (!status.equals("ALL")) {
            hql.append("AND u.status = :status ");
        }

        if (startDate != null) {
            hql.append("AND u.createAt >= :startDate ");
        }

        if (endDate != null) {
            hql.append("AND u.createAt <= :endDate");
        }

        try {
            String queryString = hql.toString();
            TypedQuery<Long> query = entityManager.createQuery(queryString, Long.class);
            if (queryString.contains(":email")) query.setParameter("email", "%" + search + "%");
            if (queryString.contains(":username")) query.setParameter("username", "%" + search + "%");
            if (queryString.contains(":status")) query.setParameter("status", status);
            if (queryString.contains(":startDate")) query.setParameter("startDate", startDate);
            if (queryString.contains(":endDate")) query.setParameter("endDate", endDate);

            return query.getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<UserEntity> getAllWithPaging(int start, int end, String search, String status, LocalDateTime startDate, LocalDateTime endDate) {
        StringBuilder hql = new StringBuilder();
        hql.append("SELECT u FROM user u ");

        if (StringValidator.isValidEmail(search)) {
            hql.append("WHERE u.email LIKE :email ");
        } else {
            hql.append("WHERE u.username LIKE :username ");
        }


        if (!status.equals("ALL")) {
            hql.append("AND u.status = :status ");
        }

        if (startDate != null) {
            hql.append("AND u.createAt >= :startDate ");
        }

        if (endDate != null) {
            hql.append("AND u.createAt <= :endDate");
        }

        try {
            String queryString = hql.toString();

            TypedQuery<UserEntity> query = entityManager.createQuery(queryString, UserEntity.class);
            if (queryString.contains(":email")) query.setParameter("email", "%" + search + "%");
            if (queryString.contains(":username")) query.setParameter("username", "%" + search + "%");
            if (queryString.contains(":status")) query.setParameter("status", status);
            if (queryString.contains(":startDate")) query.setParameter("startDate", startDate);
            if (queryString.contains(":endDate")) query.setParameter("endDate", endDate);

            query.setFirstResult(start);
            query.setMaxResults(end);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

}

