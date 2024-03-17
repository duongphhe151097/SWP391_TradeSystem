package dataAccess;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import models.SettingEntity;
import models.UserEntity;
import models.VnPayTransactionEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class settingRepository {
    private final EntityManager entityManager;

    public settingRepository() {
        this.entityManager = DbFactory.getFactory().createEntityManager();
    }

    public List<UserEntity> getSetting() {
        try {
            List<UserEntity> entity = entityManager
                    .createQuery("select s from setting s", UserEntity.class)
                    .getResultList();

            return entity;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    public Optional<SettingEntity> add(SettingEntity entity){
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.persist(entity);
            transaction.commit();

            return Optional.of(entity);
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        }

        return Optional.empty();
    }

}
