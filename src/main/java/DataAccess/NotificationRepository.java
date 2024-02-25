package DataAccess;
import Models.NotificationEntity;
import Models.ProductEntity;
import Models.UserEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import org.hibernate.Session;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
public class NotificationRepository {
    private final EntityManager entityManager;

    public NotificationEntity() {
        this.entityManager = DbFactory.getFactory().createEntityManager();
    }
    public Optional<NotificationEntity> getNotificationByUserId(UUID userId) {
        try {
            NotificationEntity entity = entityManager
                    .createQuery("FROM notification n where n.id = :userId", NotificationEntity.class)
                    .setParameter("userId", userId)
                    .getSingleResult();

            return Optional.ofNullable(entity);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }
    public Optional<NotificationEntity> addNotification(NotificationEntity notification) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            NotificationEntity entity = (NotificationEntity) entityManager.merge(notification);
            entityManager.persist(entity);

            transaction.commit();

            return Optional.of(entity);
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        }

        return Optional.empty();
    }
    public void updateStatus(NotificationEntity entity) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.createQuery("update notification n set n.status = :status")
                    .setParameter("status", entity.getStatus())
                    .executeUpdate();
            entityManager.merge(entity);

            transaction.commit();

        } catch (Exception e) {
            e.printStackTrace();
            transaction.rollback();
        }

    }
    public List<NotificationEntity> getUserNotificationWithPaging(int start, int end, UUID userId) {
        try {
            TypedQuery<NotificationEntity> query = entityManager.createQuery(
                            "SELECT n FROM notification n WHERE n.userId = :userId", NotificationEntity.class)
                    .setParameter("userId", userId);

            query.setFirstResult(start);
            query.setMaxResults(end);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public long countAllByUser(UUID userId) {
        try {
            return entityManager.createQuery("SELECT COUNT(n) FROM notification n WHERE n.userId = :userId", Long.class)
                    .setParameter("userId", userId)
                    .getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}

