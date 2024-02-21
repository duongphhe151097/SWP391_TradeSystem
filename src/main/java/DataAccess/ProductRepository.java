package DataAccess;

import Models.ProductEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;


import java.util.List;

public class ProductRepository {
    private final EntityManager entityManager;

    public ProductRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
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
}
