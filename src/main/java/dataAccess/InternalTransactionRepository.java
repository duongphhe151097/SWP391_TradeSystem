package dataAccess;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import models.InternalTransactionEntity;

public class InternalTransactionRepository {
    private final EntityManager entityManager;

    public InternalTransactionRepository() {
        this.entityManager = DbFactory.getFactory().createEntityManager();
    }

    public boolean add(InternalTransactionEntity entity){
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.merge(entity);
            transaction.commit();

            return true;
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        }

        return false;
    }
}
