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
        hql.append("SELECT e FROM externalTrans e WHERE 1=1 ");



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

    public List<UserEntity> getUserExternalTransactionsWithPaging(UUID userId,int start, int end, BigInteger amountFrom, BigInteger amountTo, UUID id, LocalDateTime startDate, LocalDateTime endDate) {
        entityManager.clear();
        StringBuilder hql = new StringBuilder();
        hql.append("SELECT e FROM externalTrans e WHERE e.userId  = :userId ");


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



        try {
            String queryString = hql.toString();

            TypedQuery<UserEntity> query = entityManager.createQuery(queryString, UserEntity.class);
            if (queryString.contains(":userId")) query.setParameter("userId", userId);
            if (queryString.contains(":amountFrom")) query.setParameter("amountFrom", amountFrom);
            if (queryString.contains(":amountTo")) query.setParameter("amountTo", amountTo);
            if (queryString.contains(":startDate")) query.setParameter("startDate", startDate);
            if (queryString.contains(":endDate")) query.setParameter("endDate", endDate);
            if (queryString.contains(":id")) query.setParameter("id", id);


            query.setFirstResult(start);
            query.setMaxResults(end);

            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

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
        public long countAllByUser(UUID userId,LocalDateTime startDate, LocalDateTime endDate, BigInteger amountFrom, BigInteger amountTo, UUID id) {
            try {
                entityManager.clear();
                StringBuilder hql = new StringBuilder();
                hql.append("SELECT COUNT(*) FROM externalTrans e WHERE e.userId = :userId ");

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


                String queryString = hql.toString();
                TypedQuery<Long> query = entityManager.createQuery(queryString, Long.class);
                if (userId != null) {
                    query.setParameter("userId", userId);
                }

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
}
