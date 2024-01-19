package DataAccess;

import Models.TokenActivationEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

public class TokenActivationRepository {
    private final EntityManager entityManager;

    public TokenActivationRepository() {
        entityManager = DbFactory.getFactory().createEntityManager();
    }

    public void addToken(TokenActivationEntity entity){
        EntityTransaction transaction = entityManager.getTransaction();
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
