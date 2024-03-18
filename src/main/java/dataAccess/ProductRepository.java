package dataAccess;

import dataAccess.DbFactory;
import jakarta.persistence.*;
import models.ExternalTransactionEntity;
import models.ProductEntity;
import utils.constants.ProductConstant;


import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ProductRepository {
    private final EntityManager entityManager;

    public ProductRepository() {
        this.entityManager = DbFactory.getFactory().createEntityManager();
    }


    public long countAllByUser(UUID userId) {
        try {
            return entityManager.createQuery("SELECT COUNT(e) FROM product e WHERE e.userId= :userId", Long.class)
                    .setParameter("userId",userId)
                    .getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
    public List<ProductEntity> searchProducts(String keyword, double minPrice, double maxPrice) {
        String queryString = "SELECT p FROM product p WHERE p.title LIKE :keyword " +
                "AND p.price BETWEEN :minPrice AND :maxPrice";
        TypedQuery<ProductEntity> query = entityManager.createQuery(queryString, ProductEntity.class);
        query.setParameter("keyword", "%" + keyword + "%");
        query.setParameter("minPrice", minPrice);
        query.setParameter("maxPrice", maxPrice);
        return query.getResultList();
    }

    public Optional<ProductEntity> addProduct(ProductEntity product) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            ProductEntity entity = (ProductEntity) entityManager.merge(product) ;
            entityManager.persist(entity);
            transaction.commit();

            return Optional.of(entity);
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
            }

            return Optional.empty();
        }


    public void updateProduct(UUID id,String title, BigInteger price, String description, String contact, String secret, String isPublic, LocalDateTime updateAt) {
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();

            entityManager.createQuery("UPDATE product p SET p.title = :title, p.price = :price, p.description = :description, p.contact = :contact, p.secret = :secret, p.isPublic = :isPublic, p.updateAt = :updateAt WHERE p.id = :id")

                    .setParameter("title", title)
                    .setParameter("price", price)
                    .setParameter("description", description)
                    .setParameter("contact", contact)
                    .setParameter("secret", secret)
                    .setParameter("isPublic",  Boolean.parseBoolean(isPublic))
                    .setParameter("updateAt", updateAt)

                    .setParameter("id", id)
                    .executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        }
    }


    public int getCategoryId() {
        TypedQuery<Integer> query = entityManager.createQuery(
                "SELECT c.id FROM category c", Integer.class);
        List<Integer> categoryIds = query.getResultList();
        if (!categoryIds.isEmpty()) {
            return categoryIds.get(0);
        } else {
            return 0;
        }

        }
        public List<ProductEntity> getUserProductWithPaging(int start, int end, UUID userId) {
            try {
                TypedQuery<ProductEntity> query = entityManager.createQuery(
                                "SELECT e FROM product e WHERE e.userId = :userId", ProductEntity.class)
                        .setParameter("userId", userId);

                query.setFirstResult(start);
                query.setMaxResults(end);
                return query.getResultList();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
    }
    public boolean isUserSeller(UUID userId) {
        try {
            Boolean isSeller = entityManager.createQuery("SELECT p.isSeller FROM product p WHERE p.userId = :userId", Boolean.class)
                    .setParameter("userId", userId)
                    .getSingleResult();


            return isSeller != null && isSeller;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public Optional<ProductEntity> getProductById(UUID productId) {
        try {
            TypedQuery<ProductEntity> query = entityManager.createQuery(
                            "SELECT p FROM product p WHERE p.id = :productId", ProductEntity.class)
                    .setParameter("productId", productId);
            ProductEntity product = query.getSingleResult();
            return Optional.of(product);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
    public List<ProductEntity> getSellerProducts(UUID sellerId) {
        try {
            TypedQuery<ProductEntity> query = entityManager.createQuery(
                            "SELECT e FROM product e WHERE e.userId = :sellerId", ProductEntity.class)
                    .setParameter("sellerId", sellerId);


            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            // Xử lý ngoại lệ nếu cần
            return null;
        }
    }
        public BigInteger getUserBalance(UUID userId) {
            try {
                BigInteger balance = entityManager.createQuery("SELECT u.balance FROM user u WHERE u.id = :userId",BigInteger.class)
                        .setParameter("userId", userId)
                        .getSingleResult();
                return balance != null ? balance : BigInteger.ZERO;
            } catch (Exception e) {
                e.printStackTrace();
                return BigInteger.ZERO;
            }
        }
    public void updateUserBalance(UUID userId, BigInteger newBalance) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.createQuery("UPDATE user u SET u.balance = :newBalance WHERE u.id = :userId")

            .setParameter("newBalance", newBalance)
            .setParameter("userId", userId)
            .executeUpdate();

            transaction.commit();
        } catch (RuntimeException e) {
            if(transaction.isActive()){
                transaction.rollback();

            }
            e.printStackTrace();
        }
    }
    public List<ProductEntity> getAllProducts(int start, int end) {
        try {
            TypedQuery<ProductEntity> query = entityManager.createQuery(
                            "SELECT e FROM product e WHERE e.status = :status", ProductEntity.class)
                    .setParameter("status", ProductConstant.PRODUCT_STATUS_READY);

            query.setFirstResult(start);
            query.setMaxResults(end);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

