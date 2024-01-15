package DataAccess;

import Models.TokenActivationEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

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
}
