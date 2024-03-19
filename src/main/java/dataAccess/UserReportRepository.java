package dataAccess;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import models.UserReportEntity;
import utils.validation.StringValidator;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UserReportRepository {
    private final EntityManager entityManager;

    public UserReportRepository() {
        this.entityManager = DbFactory.getFactory().createEntityManager();
    }

    public List<UserReportEntity> getReportWithPaging(
            int startPage,
            int endPage,
            String title,
            String userName,
            LocalDateTime startDate,
            LocalDateTime endDate,
            short status
    ) {
        try {
            StringBuilder hql = new StringBuilder();
            hql.append("SELECT ur FROM user_rp ur WHERE 1=1 ");

            if (!StringValidator.isNullOrBlank(userName)) {
                hql.append("AND ur.createBy LIKE :username ");
            }

            if (!StringValidator.isNullOrBlank(title)) {
                hql.append("AND ur.title LIKE :title ");
            }

            if (status != 0) {
                hql.append("AND ur.status = :status ");
            }

            if (startDate != null) {
                hql.append("AND ur.createAt >= :startDate ");
            }

            if (endDate != null) {
                hql.append("AND ur.createAt <= :endDate");
            }

            entityManager.clear();
            String queryString = hql.toString();
            TypedQuery<UserReportEntity> typedQuery = entityManager
                    .createQuery(queryString, UserReportEntity.class);

            if (queryString.contains(":username")) typedQuery.setParameter("username", "%" + userName + "%");
            if (queryString.contains(":title")) typedQuery.setParameter("title", "%" + title + "%");
            if (queryString.contains(":status")) typedQuery.setParameter("status", status);
            if (queryString.contains(":startDate")) typedQuery.setParameter("startDate", startDate);
            if (queryString.contains(":endDate")) typedQuery.setParameter("endDate", endDate);

            typedQuery.setFirstResult(startPage);
            typedQuery.setMaxResults(endPage);

            return typedQuery.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    public long countReportWithPaging(
            String title,
            String userName,
            LocalDateTime startDate,
            LocalDateTime endDate,
            short status
    ) {
        try {
            StringBuilder hql = new StringBuilder();
            hql.append("SELECT COUNT(*) FROM user_rp ur WHERE 1=1 ");

            if (!StringValidator.isNullOrBlank(userName)) {
                hql.append("AND ur.createBy LIKE :username ");
            }

            if (!StringValidator.isNullOrBlank(title)) {
                hql.append("AND ur.title LIKE :title ");
            }

            if (status != 0) {
                hql.append("AND ur.status = :status ");
            }

            if (startDate != null) {
                hql.append("AND ur.createAt >= :startDate ");
            }

            if (endDate != null) {
                hql.append("AND ur.createAt <= :endDate");
            }

            entityManager.clear();
            String queryString = hql.toString();
            TypedQuery<Long> typedQuery = entityManager
                    .createQuery(queryString, Long.class);
            if (queryString.contains(":username")) typedQuery.setParameter("username", "%" + userName + "%");
            if (queryString.contains(":title")) typedQuery.setParameter("title", "%" + title + "%");
            if (queryString.contains(":status")) typedQuery.setParameter("status", status);
            if (queryString.contains(":startDate")) typedQuery.setParameter("startDate", startDate);
            if (queryString.contains(":endDate")) typedQuery.setParameter("endDate", endDate);

            return typedQuery.getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    public Optional<UserReportEntity> getReportById(UUID id) {
        entityManager.clear();
        try {
            UserReportEntity userReport = entityManager
                    .createQuery("SELECT ur FROM user_rp ur WHERE ur.id = :id AND ur.isDelete = false", UserReportEntity.class)
                    .setParameter("id", id)
                    .getSingleResult();

            return Optional.ofNullable(userReport);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    public Optional<UserReportEntity> getReportByProductId(UUID pid) {
        entityManager.clear();
        try {
            UserReportEntity userReport = entityManager
                    .createQuery("SELECT ur FROM user_rp ur WHERE ur.productTarget = :pid AND ur.isDelete = false", UserReportEntity.class)
                    .setParameter("pid", pid)
                    .getSingleResult();

            return Optional.ofNullable(userReport);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    public boolean add(UserReportEntity entity) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.persist(entity);
            transaction.commit();

            return true;
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        }

        return false;
    }

    public boolean update(UserReportEntity entity) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.merge(entity);
            transaction.commit();

            return true;
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        }

        return false;
    }

    public long countReportByUserIdWithPaging(
            UUID userId,
            String title,
            LocalDateTime startDate,
            LocalDateTime endDate,
            short status
    ) {
        try {
            StringBuilder hql = new StringBuilder();
            hql.append("SELECT COUNT(*) FROM user_rp ur WHERE ur.userId = :userId OR ur.userTarget = :userId ");

            if (!StringValidator.isNullOrBlank(title)) {
                hql.append("AND ur.title LIKE :title ");
            }

            if (status != 0) {
                hql.append("AND ur.status = :status ");
            }

            if (startDate != null) {
                hql.append("AND ur.createAt >= :startDate ");
            }

            if (endDate != null) {
                hql.append("AND ur.createAt <= :endDate");
            }

            entityManager.clear();
            String queryString = hql.toString();
            TypedQuery<Long> typedQuery = entityManager
                    .createQuery(queryString, Long.class);
            if (queryString.contains(":userId")) typedQuery.setParameter("userId", userId);
            if (queryString.contains(":title")) typedQuery.setParameter("title", "%" + title + "%");
            if (queryString.contains(":status")) typedQuery.setParameter("status", status);
            if (queryString.contains(":startDate")) typedQuery.setParameter("startDate", startDate);
            if (queryString.contains(":endDate")) typedQuery.setParameter("endDate", endDate);

            return typedQuery.getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    public List<UserReportEntity> getReportByUserIdWithPaging(
            UUID userId,
            int startPage,
            int endPage,
            String title,
            LocalDateTime startDate,
            LocalDateTime endDate,
            short status
    ) {
        try {
            StringBuilder hql = new StringBuilder();
            hql.append("SELECT ur FROM user_rp ur WHERE ur.userId = :userId OR ur.userTarget = :userId ");

            if (!StringValidator.isNullOrBlank(title)) {
                hql.append("AND ur.title LIKE :title ");
            }

            if (status != 0) {
                hql.append("AND ur.status = :status ");
            }

            if (startDate != null) {
                hql.append("AND ur.createAt >= :startDate ");
            }

            if (endDate != null) {
                hql.append("AND ur.createAt <= :endDate");
            }

            entityManager.clear();
            String queryString = hql.toString();
            TypedQuery<UserReportEntity> typedQuery = entityManager
                    .createQuery(queryString, UserReportEntity.class);

            if (queryString.contains(":userId")) typedQuery.setParameter("userId", userId);
            if (queryString.contains(":title")) typedQuery.setParameter("title", "%" + title + "%");
            if (queryString.contains(":status")) typedQuery.setParameter("status", status);
            if (queryString.contains(":startDate")) typedQuery.setParameter("startDate", startDate);
            if (queryString.contains(":endDate")) typedQuery.setParameter("endDate", endDate);

            typedQuery.setFirstResult(startPage);
            typedQuery.setMaxResults(endPage);

            return typedQuery.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }
}
