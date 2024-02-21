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

//public class TransactionManagerRepository {
//    private final EntityManager entityManager;
//
//    public TransactionManagerRepository() {
//        this.entityManager = DbFactory.getFactory().createEntityManager();
//    }
//
//    public List<UserEntity> getExternalTransaction() {
//        try {
//            List<UserEntity> entity = entityManager
//                    .createQuery("select et from externalTrans et", UserEntity.class)
//                    .getResultList();
//
//            return entity;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return new ArrayList<>();
//    }
//
//    public List<UserEntity> getExternalTransactionByUser(UUID UserId) {
//        try {
//
//            List<UserEntity> entity = entityManager
//                    .createQuery("from externalTrans et WHERE et.userId = :UserId", UserEntity.class)
//                    .setParameter("UserId", UserId)
//                    .getResultList();
//
//            return entity;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return new ArrayList<>();
//    }
//
//    public List<ExternalTransactionEntity> getUserExternalTransactionsWithPaging(int start, int pageSize, UUID UserId) {
//        try {
//            TypedQuery<ExternalTransactionEntity> query = entityManager.createQuery(
//                    "SELECT e FROM externalTrans e where e.userId = :UserId", ExternalTransactionEntity.class)
//                    .setParameter("UserId", UserId);
//            query.setFirstResult(start);
//            query.setMaxResults(pageSize);
//            return query.getResultList();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    public long countAllByUser(UUID userId) {
//        try {
//            return entityManager.createQuery("SELECT COUNT(e) FROM externalTrans e WHERE e.userId = :userId", Long.class)
//                    .setParameter("userId", userId)
//                    .getSingleResult();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return 0;
//    }
//
//    public List<ExternalTransactionEntity> getExternalTransactionsWithPaging(int start, int pageSize) {
//        try {
//            TypedQuery<ExternalTransactionEntity> query = entityManager.createQuery(
//                    "SELECT e FROM externalTrans e", ExternalTransactionEntity.class);
//            query.setFirstResult(start);
//            query.setMaxResults(pageSize);
//            return query.getResultList();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    public long countAll() {
//        try {
//            return entityManager.createQuery("SELECT COUNT(e) FROM externalTrans e", Long.class)
//                    .getSingleResult();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return 0;
//    }
//
//    public long countAllWithSearch(UUID id, String type, BigInteger amount) {
//        entityManager.clear();
//        StringBuilder hql = new StringBuilder();
//        hql.append("SELECT COUNT(*) FROM externalTrans e ");
//
//        if (id != null) {
//            hql.append("AND e.id >= :id ");
//        }
//        if (type != null) {
//            hql.append("AND e.type >= :type ");
//        }
//        if (userId != null) {
//            hql.append("AND e.userId >= :userId ");
//        }
//        if (amount != null) {
//            hql.append("AND e.amount >= :amount ");
//        }
//        if (createAt != null) {
//            hql.append("AND e.createAt = :createAt ");
//        }
//
//        if (createBy != null) {
//            hql.append("AND e.createBy <= :createBy");
//        }
//        if (updateAt != null) {
//            hql.append("AND e.updateAt <= :updateAt");
//        }
//        if (updateBy != null) {
//            hql.append("AND e.updateBy <= :updateBy");
//        }
//
//
//
//
//        try {
//            String queryString = hql.toString();
//            TypedQuery<Long> query = entityManager.createQuery(queryString, Long.class);
//
//            if (queryString.contains(":id")) query.setParameter("id", id);
//            if (queryString.contains(":type")) query.setParameter("type", type);
//            if (queryString.contains(":userId")) query.setParameter("userId", userId);
//            if (queryString.contains(":amount")) query.setParameter("amount", amount);
//            if (queryString.contains(":startDate")) query.setParameter("createAt", createAt);
//            if (queryString.contains(":endDate")) query.setParameter("createBy", createBy);
//            if (queryString.contains(":startDate")) query.setParameter("updateAt", updateAt);
//            if (queryString.contains(":endDate")) query.setParameter("updateBy", updateBy);
//
//
//            return query.getSingleResult();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return 0;
//    }

//}


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

    public List<UserEntity> getExternalTransactionByUser(UUID UserId) {
        try {

            List<UserEntity> entity = entityManager
                    .createQuery("from externalTrans et WHERE et.userId = :UserId", UserEntity.class)
                    .setParameter("UserId", UserId)
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

    public List<ExternalTransactionEntity> getUserExternalTransactionsWithPaging(int start, int pageSize, UUID userId) {
        try {
            TypedQuery<ExternalTransactionEntity> query = entityManager.createQuery(
                    "SELECT e FROM externalTrans e WHERE e.userId = :userId", ExternalTransactionEntity.class)
                    .setParameter("userId", userId);

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

        public long countAllByUser(UUID userId) {
        try {
            return entityManager.createQuery("SELECT COUNT(e) FROM externalTrans e WHERE e.userId = :userId", Long.class)
                    .setParameter("userId", userId)
                    .getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
