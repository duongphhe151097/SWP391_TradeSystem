package dataAccess;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NonUniqueResultException;
import jakarta.persistence.TypedQuery;
import models.SettingEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SettingRepository {

    private final EntityManager entityManager;

    public SettingRepository() {
        this.entityManager = DbFactory.getFactory().createEntityManager();
    }

    public Optional<SettingEntity> addSetting(SettingEntity settings) {

        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            SettingEntity entity = entityManager.merge(settings);
            entityManager.persist(entity);
            transaction.commit();

            return Optional.of(entity);
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        }

        return Optional.empty();
    }

    public List<SettingEntity> getSetting() {
        try {
            entityManager.clear();
            List<SettingEntity> entity = entityManager
                    .createQuery("select s from setting s where s.isDelete = false", SettingEntity.class)
                    .getResultList();

            return entity;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    public boolean updateSettingStatus(int id, boolean isDelete) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.createQuery("UPDATE setting s SET s.isDelete = :isDelete WHERE s.id = :id")
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

    public long countAllSetting() {
        entityManager.clear();
        StringBuilder hql = new StringBuilder();
        hql.append("SELECT COUNT(*) FROM setting s WHERE s.isDelete = false ");
        try {
            String queryString = hql.toString();
            TypedQuery<Long> query = entityManager.createQuery(queryString, Long.class);
            return query.getSingleResult();
        }catch (NonUniqueResultException e) {
            // Log the exception for debugging purposes
            e.printStackTrace();
            throw new NonUniqueResultException("Query returned multiple results.", e);
        } catch (Exception e) {
            // Log the exception for debugging purposes
            e.printStackTrace();
            throw e; // Rethrow the exception to indicate an error in the method
        }
    }

        public List<SettingEntity> getSettingsWithPaging(int start, int pageSize) {
        try {
            TypedQuery<SettingEntity> query = entityManager.createQuery(
                    "SELECT s FROM setting s WHERE s.isDelete = false ", SettingEntity.class);
            query.setFirstResult(start);
            query.setMaxResults(pageSize);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
