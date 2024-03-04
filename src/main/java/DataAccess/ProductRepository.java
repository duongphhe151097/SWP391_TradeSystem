package DataAccess;

import Models.CategoryEntity;
import Models.ProductEntity;
import Models.UserEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ProductRepository {
    private final EntityManager entityManager;

    public ProductRepository() {
        this.entityManager = DbFactory.getFactory().createEntityManager();
    }

    public List<ProductEntity> getAllProducts() {
        TypedQuery<ProductEntity> query = entityManager.createQuery(
                "SELECT p FROM product p", ProductEntity.class);
        return query.getResultList();
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


    public void updateProduct(ProductEntity product) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.merge(product);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
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
}