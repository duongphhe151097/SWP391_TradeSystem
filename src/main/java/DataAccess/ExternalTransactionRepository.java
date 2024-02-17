package DataAccess;

import Models.ExternalTransactionEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.Optional;
import java.util.UUID;

public class ExternalTransactionRepository {
    private final EntityManager entityManager;

    public ExternalTransactionRepository() {
        this.entityManager = DbFactory.getFactory().createEntityManager();
    }

    public Optional<ExternalTransactionEntity> getExternalTransactionByIdType(UUID txnId, String type){
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

    public Optional<ExternalTransactionEntity> add(ExternalTransactionEntity entity){
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            ExternalTransactionEntity externalTransactionEntity = entityManager.merge(entity);
            entityManager.persist(externalTransactionEntity);
            transaction.commit();

            return Optional.of(externalTransactionEntity);
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        }

        return Optional.empty();
    }

    public Optional<ExternalTransactionEntity> update(ExternalTransactionEntity entity){
        EntityTransaction transaction = entityManager.getTransaction();
        try{
            transaction.begin();
            entityManager.merge(entity);
            transaction.commit();

            return Optional.of(entity);
        }catch (Exception e) {
            transaction.rollback();
        }

        return Optional.empty();
    }
}
