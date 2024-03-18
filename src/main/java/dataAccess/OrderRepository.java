package dataAccess;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import models.OrderEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class OrderRepository {
    private final EntityManager entityManager;

    public OrderRepository() {
        entityManager = DbFactory.getFactory().createEntityManager();
    }

    public List<OrderEntity> getOrderByUserIdWithFilter(
            UUID uid,
            int startPage,
            int endPage,
            LocalDateTime startDate,
            LocalDateTime endDate,
            short status
    ) {
        try {
            StringBuilder hql = new StringBuilder();
            hql.append("SELECT o FROM order o WHERE o.userId = :userId ");

            if (status != 0) {
                hql.append("AND o.status = :status ");
            }

            if (startDate != null) {
                hql.append("AND o.createAt >= :startDate ");
            }

            if (endDate != null) {
                hql.append("AND o.createAt <= :endDate");
            }

            entityManager.clear();
            String queryString = hql.toString();

            TypedQuery<OrderEntity> typedQuery = entityManager
                    .createQuery(queryString, OrderEntity.class);
            if (queryString.contains(":userId")) typedQuery.setParameter("userId", uid);
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

    public long countOrderByUserId(
            UUID uid,
            LocalDateTime startDate,
            LocalDateTime endDate,
            short status
    ) {
        try {
            StringBuilder hql = new StringBuilder();
            hql.append("SELECT COUNT(*) FROM order o WHERE o.userId = :userId ");

            if (status != 0) {
                hql.append("AND o.status = :status ");
            }

            if (startDate != null) {
                hql.append("AND o.createAt >= :startDate ");
            }

            if (endDate != null) {
                hql.append("AND o.createAt <= :endDate");
            }

            entityManager.clear();
            String queryString = hql.toString();
            TypedQuery<Long> typedQuery = entityManager
                    .createQuery(queryString, Long.class);
            if (queryString.contains(":userId")) typedQuery.setParameter("userId", uid);
            if (queryString.contains(":status")) typedQuery.setParameter("status", status);
            if (queryString.contains(":startDate")) typedQuery.setParameter("startDate", startDate);
            if (queryString.contains(":endDate")) typedQuery.setParameter("endDate", endDate);

            return typedQuery.getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }
}
