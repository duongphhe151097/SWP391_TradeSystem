package DataAccess;

import Models.ExternalTransactionEntity;
import Models.UserEntity;
import Utils.Validation.StringValidator;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import javax.xml.validation.Validator;
import java.math.BigInteger;
import java.time.LocalDateTime;
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

    public List<ExternalTransactionEntity> getExternalTransactionsWithPaging(int start, int pageSize) {
        try {
            TypedQuery<ExternalTransactionEntity> query = entityManager.createQuery(
                    "SELECT e FROM externalTrans e", ExternalTransactionEntity.class);
            query.setFirstResult(start);
            query.setMaxResults(pageSize);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public long countAll() {
        try {
            return entityManager.createQuery("SELECT COUNT(e) FROM externalTrans e", Long.class)
                    .getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

}
