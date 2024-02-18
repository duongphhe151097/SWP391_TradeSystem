package DataAccess;

import Models.VnPayTransactionEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;

import java.util.Optional;
import java.util.UUID;

public class VnPayTransactionRepository {
    private final EntityManager entityManager;

    public VnPayTransactionRepository() {
        this.entityManager = DbFactory.getFactory().createEntityManager();
    }

    public Optional<VnPayTransactionEntity> getByTransactionId(UUID transactionId){
        try {
            VnPayTransactionEntity entity = entityManager
                    .createQuery("select ext from vnPayTrans ext " +
                            "where ext.transactionId = :txnId and ext.isDelete = false", VnPayTransactionEntity.class)
                    .setParameter("txnId", transactionId)
                    .getSingleResult();

            return Optional.ofNullable(entity);
        } catch (Exception e) {
            if(!(e instanceof NoResultException)){
                e.printStackTrace();
            }
        }
        return Optional.empty();
    }

    public Optional<VnPayTransactionEntity> add(VnPayTransactionEntity entity){
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

    public Optional<VnPayTransactionEntity> update(VnPayTransactionEntity entity){
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
