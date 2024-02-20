package DataAccess;

import Models.UserEntity;
import jakarta.persistence.EntityManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class TransactionManagerRepository {
    private final EntityManager entityManager;

    public TransactionManagerRepository() {
        this.entityManager = DbFactory.getFactory().createEntityManager();
    }

    public List<UserEntity> getExternalTransaction() {
        try {
            List<UserEntity> entity = entityManager
                    .createQuery("select et from externalTrans et", UserEntity.class)
                    .getResultList();

            return entity;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }



}
