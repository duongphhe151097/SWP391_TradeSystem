package dataAccess;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import models.UserEntity;
import models.UserReportEntity;
import utils.validation.StringValidator;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
                hql.append("AND u.createBy LIKE :username ");
            }

            if (!StringValidator.isNullOrBlank(title)) {
                hql.append("AND u.title = :title ");
            }

            if (status != 0) {
                hql.append("AND u.status = :status ");
            }

            if (startDate != null) {
                hql.append("AND u.createAt >= :startDate ");
            }

            if (endDate != null) {
                hql.append("AND u.createAt <= :endDate");
            }

            entityManager.clear();
            String queryString = hql.toString();
            TypedQuery<UserReportEntity> typedQuery = entityManager
                    .createQuery(queryString, UserReportEntity.class);

            if(queryString.contains(":username")) typedQuery.setParameter("username", "%" + userName + "%");
            if(queryString.contains(":title")) typedQuery.setParameter("title", "%" + title + "%");
            if(queryString.contains(":status")) typedQuery.setParameter("status", status);
            if(queryString.contains(":startDate")) typedQuery.setParameter("startDate", startDate);
            if(queryString.contains(":endDate")) typedQuery.setParameter("endDate", endDate);

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
                hql.append("AND u.createBy LIKE :username ");
            }

            if (!StringValidator.isNullOrBlank(title)) {
                hql.append("AND u.title LIKE :title ");
            }

            if (status != 0) {
                hql.append("AND u.status = :status ");
            }

            if (startDate != null) {
                hql.append("AND u.createAt >= :startDate ");
            }

            if (endDate != null) {
                hql.append("AND u.createAt <= :endDate");
            }

            entityManager.clear();
            String queryString = hql.toString();
            TypedQuery<Long> typedQuery = entityManager
                    .createQuery(queryString, Long.class);
            if(queryString.contains(":username")) typedQuery.setParameter("username", "%" + userName + "%");
            if(queryString.contains(":title")) typedQuery.setParameter("title", "%" + title + "%");
            if(queryString.contains(":status")) typedQuery.setParameter("status", status);
            if(queryString.contains(":startDate")) typedQuery.setParameter("startDate", startDate);
            if(queryString.contains(":endDate")) typedQuery.setParameter("endDate", endDate);

            return typedQuery.getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }
}
