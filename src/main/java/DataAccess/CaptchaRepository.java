package DataAccess;

import Models.CaptchaEntity;
import Models.UserEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.Optional;

public class CaptchaRepository {
    private final EntityManager entityManager;
    private final EntityTransaction transaction;

    public CaptchaRepository() {
        entityManager = DbFactory.getFactory().createEntityManager();
        transaction = entityManager.getTransaction();
    }

    public void addCaptcha(CaptchaEntity entity) {
        try {
            transaction.begin();
            CaptchaEntity userEntity = (CaptchaEntity) entityManager.merge(entity);
            entityManager.persist(userEntity);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        }
    }

    public Optional<CaptchaEntity> getCaptchaById(String id) {
        try {
            CaptchaEntity entity = entityManager
                    .createQuery("select c from captcha c where c.id = :id", CaptchaEntity.class)
                    .setParameter("id", id)
                    .getSingleResult();

            return Optional.ofNullable(entity);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public void deleteCaptcha(String id) {
        try {
            transaction.begin();
            entityManager.createQuery("delete captcha c where c.id = :id")
                    .setParameter("id", id)
                    .executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        }
    }
}
