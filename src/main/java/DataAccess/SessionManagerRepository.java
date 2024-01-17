package DataAccess;

import Models.SessionManagerEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.Optional;
import java.util.UUID;

public class SessionManagerRepository {
    private final EntityManager entityManager;
    private final EntityTransaction transaction;

    public SessionManagerRepository() {
        entityManager = DbFactory.getFactory().createEntityManager();
        transaction = entityManager.getTransaction();
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

    }

}
