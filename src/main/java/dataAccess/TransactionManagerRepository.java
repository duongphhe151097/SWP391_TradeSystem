package dataAccess;

import models.ExternalTransactionEntity;
import models.UserEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NonUniqueResultException;
import jakarta.persistence.TypedQuery;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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

    public List<UserEntity> getExternalTransactionsWithPaging(int start, int end, BigInteger amountFrom, BigInteger amountTo, UUID id, String user, LocalDateTime startDate, LocalDateTime endDate) {
        entityManager.clear();
        StringBuilder hql = new StringBuilder();
        hql.append("SELECT e FROM externalTrans e WHERE 1=1");



        if (startDate != null) {
            hql.append("AND e.createAt >= :startDate ");
        }

        if (endDate != null) {
            hql.append("AND e.createAt <= :endDate ");
        }

        if(amountFrom != null){
            hql.append("AND e.amount >= :amountFrom ");
        }

        if(amountTo != null){
            hql.append("AND e.amount <= :amountTo ");
        }

        if (id != null) {
            hql.append("AND e.id = :id ");
        }

        if (user != null) {
            hql.append("AND e.createBy LIKE :user ");
        }

        try {
            String queryString = hql.toString();

            TypedQuery<UserEntity> query = entityManager.createQuery(queryString, UserEntity.class);
            if (queryString.contains(":amountFrom")) query.setParameter("amountFrom", amountFrom);
            if (queryString.contains(":amountTo")) query.setParameter("amountTo", amountTo);
            if (queryString.contains(":startDate")) query.setParameter("startDate", startDate);
            if (queryString.contains(":endDate")) query.setParameter("endDate", endDate);
            if (queryString.contains(":id")) query.setParameter("id", id);
            if (queryString.contains(":user")) query.setParameter("user", "%" + user + "%");


            query.setFirstResult(start);
            query.setMaxResults(end);

            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    public List<ExternalTransactionEntity> getUserExternalTransactionsWithPaging(int start, int end, UUID userId) {
        try {
            TypedQuery<ExternalTransactionEntity> query = entityManager.createQuery(
                    "SELECT e FROM externalTrans e WHERE e.userId = :userId", ExternalTransactionEntity.class)
                    .setParameter("userId", userId);

            query.setFirstResult(start);
            query.setMaxResults(end);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

//    public long countAll() {
//        try {
//            return entityManager.createQuery("SELECT COUNT(e) FROM externalTrans e", Long.class)
//                    .getSingleResult();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return 0;
//    }

    public long countAll(LocalDateTime startDate, LocalDateTime endDate, BigInteger amountFrom, BigInteger amountTo, String user, UUID id) {
        try {
            entityManager.clear();
            StringBuilder hql = new StringBuilder();
            hql.append("SELECT COUNT(*) FROM externalTrans e WHERE 1=1 ");

            if (startDate != null) {
                hql.append("AND e.createAt >= :startDate ");
            }
            if (endDate != null) {
                hql.append("AND e.createAt <= :endDate ");
            }
            if (amountFrom != null) {
                hql.append("AND e.amount >= :amountFrom ");
            }
            if (amountTo != null) {
                hql.append("AND e.amount <= :amountTo ");
            }
            if (id != null) {
                hql.append("AND e.id = :id ");
            }
            if (user != null) {
                hql.append("AND e.createBy LIKE :user ");
            }


            String queryString = hql.toString();
            TypedQuery<Long> query = entityManager.createQuery(queryString, Long.class);

            if (startDate != null) {
                query.setParameter("startDate", startDate);
            }
            if (endDate != null) {
                query.setParameter("endDate", endDate);
            }
            if (amountFrom != null) {
                query.setParameter("amountFrom", amountFrom);
            }
            if (amountTo != null) {
                query.setParameter("amountTo", amountTo);
            }
            if (id != null) {
                query.setParameter("id", id);
            }
            if (user != null) {
                query.setParameter("user", "%" + user + "%");
            }

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
