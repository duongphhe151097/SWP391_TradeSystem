package dataAccess;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NonUniqueResultException;
import jakarta.persistence.TypedQuery;
import models.CategoryEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CategoryRepository {

    private final EntityManager entityManager;

    public CategoryRepository() {
        this.entityManager = DbFactory.getFactory().createEntityManager();
    }

    public Optional<CategoryEntity> addCategory(CategoryEntity categoríes) {

        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            CategoryEntity entity = entityManager.merge(categoríes);
            entityManager.persist(entity);
            transaction.commit();

            return Optional.of(entity);
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        }

        return Optional.empty();
    }

    public List<CategoryEntity> getCategory() {
        try {
            entityManager.clear();
            List<CategoryEntity> entity = entityManager
                    .createQuery("select c from category c where c.isDelete = false", CategoryEntity.class)
                    .getResultList();

            return entity;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    public boolean updateCategoryStatus(int id, boolean isDelete) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.createQuery("UPDATE category c SET c.isDelete = :isDelete WHERE c.id = :id")
                    .setParameter("id", id)
                    .setParameter("isDelete", isDelete)
                    .executeUpdate();
            transaction.commit();

            return true;
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        }
        return false;
    }

    public long countAllCategory() {
        entityManager.clear();
        StringBuilder hql = new StringBuilder();
        hql.append("SELECT COUNT(*) FROM category c WHERE c.isDelete = false ");
        try {
            String queryString = hql.toString();
            TypedQuery<Long> query = entityManager.createQuery(queryString, Long.class);
            return query.getSingleResult();
        } catch (NonUniqueResultException e) {
            // Log the exception for debugging purposes
            e.printStackTrace();
            throw new NonUniqueResultException("Query returned multiple results.", e);
        } catch (Exception e) {
            // Log the exception for debugging purposes
            e.printStackTrace();
            throw e; // Rethrow the exception to indicate an error in the method
        }
    }

    public List<CategoryEntity> getCategoriesWithPaging(int start, int pageSize) {
        try {
            TypedQuery<CategoryEntity> query = entityManager.createQuery(
                    "SELECT c FROM category c WHERE c.isDelete = false ", CategoryEntity.class);
            query.setFirstResult(start);
            query.setMaxResults(pageSize);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}