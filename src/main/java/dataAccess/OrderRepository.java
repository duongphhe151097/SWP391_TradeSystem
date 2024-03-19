package dataAccess;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import models.OrderEntity;
import models.UserRoleEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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

    public Optional<OrderEntity> getOrderByUserId(UUID userId, UUID productId) {
        try {
            entityManager.clear();
            OrderEntity orderEntity = entityManager
                    .createQuery("SELECT o FROM order o WHERE o.userId = :userId AND o.productId = :productId AND o.isDelete = false", OrderEntity.class)
                    .setParameter("userId", userId)
                    .setParameter("productId", productId)
                    .getSingleResult();

            return Optional.of(orderEntity);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public Optional<OrderEntity> getOrderById(UUID orderId) {
        try {
            entityManager.clear();
            OrderEntity orderEntity = entityManager
                    .createQuery("SELECT o FROM order o WHERE o.id = :orderId AND o.isDelete = false", OrderEntity.class)
                    .setParameter("orderId", orderId)
                    .getSingleResult();

            return Optional.of(orderEntity);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }


    public boolean add(OrderEntity entity) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.persist(entity);
            transaction.commit();

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            transaction.rollback();
        }

        return false;
    }

    public void update(OrderEntity entity) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.merge(entity);
            transaction.commit();

        } catch (Exception e) {
            e.printStackTrace();
            transaction.rollback();
        }
    }
}
