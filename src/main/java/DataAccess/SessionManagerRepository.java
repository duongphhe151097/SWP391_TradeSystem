package DataAccess;

import Models.SessionManagerEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.Optional;
import java.util.UUID;

public class SessionManagerRepository {
    private final EntityManager entityManager;

    public SessionManagerRepository() {
        entityManager = DbFactory.getFactory().createEntityManager();
    }

    public Optional<SessionManagerEntity> getSessionByUserId(UUID userId) {
        try {
            SessionManagerEntity entity = entityManager
                    .createQuery("SELECT s FROM session s " +
                            "WHERE s.userId = :userId ", SessionManagerEntity.class)
                    .setParameter("userId", userId)
                    .getSingleResult();

            return Optional.ofNullable(entity);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    public void addSession(SessionManagerEntity entity) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            SessionManagerEntity sessionManagerEntity = (SessionManagerEntity) entityManager.merge(entity);
            entityManager.persist(sessionManagerEntity);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            transaction.rollback();
        }
    }

    public void removeSession(String id, UUID userId) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();

            entityManager.createQuery("DELETE session s WHERE s.sessionId = :sessionId AND s.userId = :userId")
                    .setParameter("sessionId", id)
                    .setParameter("userId", userId)
                    .executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            transaction.rollback();
        }
    }

}
