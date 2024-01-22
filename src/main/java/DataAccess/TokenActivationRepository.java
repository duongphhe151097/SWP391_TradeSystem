package DataAccess;

import Models.TokenActivationEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.time.LocalDateTime;

public class TokenActivationRepository {
    private final EntityManager entityManager;
    private final EntityTransaction transaction;

    public TokenActivationRepository() {
        entityManager = DbFactory.getFactory().createEntityManager();
        transaction = entityManager.getTransaction();
    }
    public void addToken(TokenActivationEntity entity){
        try{
            transaction.begin();
            TokenActivationEntity tokenActivation = (TokenActivationEntity) entityManager.merge(entity);
            entityManager.persist(tokenActivation);
            transaction.commit();
        }catch (Exception e){
            transaction.rollback();
        }
    }
    public void markTokenAsUsed(String token) {
        try {
            transaction.begin();
            TokenActivationEntity tokenEntity = entityManager.find(TokenActivationEntity.class, token);

            if (tokenEntity != null) {
                tokenEntity.setUsed(true);

                 tokenEntity.setExpriedAt(LocalDateTime.now().plusDays(7));
                entityManager.merge(tokenEntity);
            }

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        }
    }
}
