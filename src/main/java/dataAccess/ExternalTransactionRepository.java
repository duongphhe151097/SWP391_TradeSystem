package dataAccess;

import models.ExternalTransactionEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.Optional;
import java.util.UUID;

public class ExternalTransactionRepository {
    private final EntityManager entityManager;

    public ExternalTransactionRepository() {
        this.entityManager = DbFactory.getFactory().createEntityManager();
    }

    public Optional<ExternalTransactionEntity> getExternalTransactionByIdType(UUID txnId, String type) {
        try {
            ExternalTransactionEntity entity = entityManager
                    .createQuery("select ext from externalTrans ext " +
                            "where ext.id = :txnId and ext.type = :type and ext.isDelete = false", ExternalTransactionEntity.class)
                    .setParameter("txnId", txnId)
                    .setParameter("type", type)
                    .getSingleResult();

            return Optional.ofNullable(entity);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public Optional<ExternalTransactionEntity> getExternalTransactionByUid(UUID uId) {
        try {
            ExternalTransactionEntity entity = entityManager
                    .createQuery("select ext from externalTrans ext " +
                            "where ext.userId = :uid and ext.isDelete = false", ExternalTransactionEntity.class)
                    .setParameter("uid", uId)
                    .getSingleResult();

            return Optional.ofNullable(entity);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public Optional<ExternalTransactionEntity> add(ExternalTransactionEntity entity) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.persist(entity);
            transaction.commit();

            return Optional.of(entity);
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        }

        return Optional.empty();
    }

    public void updateStatus(ExternalTransactionEntity entity) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.createQuery("update externalTrans ext set ext.status = :status where ext.userId = :uid and ext.id = :txnId")
                    .setParameter("status", entity.getStatus())
                    .setParameter("uid", entity.getUserId())
                    .setParameter("txnId", entity.getId())
                    .executeUpdate();
            entityManager.merge(entity);

            transaction.commit();

        } catch (Exception e) {
            e.printStackTrace();
            transaction.rollback();
        }

    }
}
