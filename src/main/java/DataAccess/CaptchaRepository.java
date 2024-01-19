package DataAccess;

import Models.CaptchaEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.Optional;
import java.util.UUID;

public class CaptchaRepository {
    private final EntityManager entityManager;

    public CaptchaRepository() {
        entityManager = DbFactory.getFactory().createEntityManager();
    }

    public Optional<CaptchaEntity> addCaptcha(CaptchaEntity entity) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            CaptchaEntity captchaEntity = (CaptchaEntity) entityManager.merge(entity);
            entityManager.persist(captchaEntity);
            transaction.commit();

            return Optional.of(captchaEntity);
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        }

        return Optional.empty();
    }

    public Optional<CaptchaEntity> getCaptchaById(String captchaId, UUID id) {
        try {
            CaptchaEntity entity = entityManager
                    .createQuery("select c from captcha c " +
                            "where c.captchaId = :captchaId " +
                            "and c.id = :id", CaptchaEntity.class)
                    .setParameter("captchaId", captchaId)
                    .setParameter("id", id)
                    .getSingleResult();

            return Optional.ofNullable(entity);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public void deleteCaptcha(String captchaId, UUID id) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.createQuery("delete captcha c where c.id = :id and c.captchaId = :captchaId")
                    .setParameter("id", id)
                    .setParameter("captchaId", captchaId)
                    .executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        }
    }
}
