package dataAccess;

import models.NotificationEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import java.util.*;

public class NotificationRepository {
    private final EntityManager entityManager;

    public NotificationRepository() {
        this.entityManager = DbFactory.getFactory().createEntityManager();
    }

    public List<NotificationEntity> getNotificationByUser(UUID userToNotify) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();

            List<NotificationEntity> entity = entityManager
                    .createQuery("SELECT n FROM notification n " +
                            "WHERE n.userToNotify = :userToNotify"+
                            " order by n.createAt DESC ", NotificationEntity.class)
                    .setParameter("userToNotify", userToNotify)
                    .getResultList();

            transaction.commit();
            return entity;
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        }

        return Collections.emptyList();
    }


    public Optional<NotificationEntity> add(NotificationEntity entity) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.merge(entity); // Sử dụng merge thay vì persist
            transaction.commit();

            return Optional.of(entity);
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        }

        return Optional.empty();
    }

    public boolean updateNotificationStatus( UUID notificationId, boolean isSeen) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.createQuery("UPDATE notification n SET n.isSeen = :isSeen WHERE n.id = :id ")
                    .setParameter("id", notificationId)
                    .setParameter("isSeen", isSeen)
                    .executeUpdate();
            transaction.commit();

            return true;
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        }
        return false;
    }
}