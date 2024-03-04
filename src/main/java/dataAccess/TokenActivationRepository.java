package dataAccess;

import models.TokenActivationEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
        } catch (Exception e) {
            transaction.rollback();
        }
    }

    public void markTokenAsUsed(String token) {
        EntityTransaction transaction = entityManager.getTransaction();

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

    public Optional<TokenActivationEntity> getTokenByToken(String token) {
        try {
            TypedQuery<TokenActivationEntity> query = entityManager.createQuery(
                    "SELECT t FROM tokenActive t WHERE t.token = :token",
                    TokenActivationEntity.class
            );

            query.setParameter("token", token);

            List<TokenActivationEntity> tokens = query.getResultList();
            if (tokens.isEmpty()) {
                return Optional.empty();
            } else {
                return Optional.of(tokens.get(0));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }
    public void updateToken(TokenActivationEntity tokenEntity) {
        try {
            entityManager.getTransaction().begin();
            entityManager.merge(tokenEntity);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            e.printStackTrace();
        }
    }
}



//    public static void main(String[] args) {
//        String token = "gLb1bw4JNq20R5DCNuU757NkTTeHgB5FvFi6KaQ3sSzPlPoIV6";
//        TokenActivationRepository tr = new TokenActivationRepository();
//        System.out.println(tr.getUserByToken(token));
//        UserRepository userRepository = new UserRepository();
//        Optional<UserEntity> userOptional = userRepository.getUserByEmail("duchthe163251@fpt.edu.vn");
//
//        if (userOptional.isPresent()) {
//            UserEntity user = userOptional.get();
//
//            // Tạo reset token
//            String resetToken = StringGenerator.generateRandomString(50);
//
//            // Lưu reset token vào DB
//            TokenActivationRepository tokenRepository = new TokenActivationRepository();
//            TokenActivationEntity tokenEntity = TokenActivationEntity.builder()
//                    .token(resetToken)
//                    .userId(user.getId())
//                    .type((short) 2)
//                    .isUsed(false)
//                    .createAt(LocalDateTime.now())
//                    .expriedAt(LocalDateTime.now().plusDays(1))
//                    .build();
//
//            tokenRepository.addToken(tokenEntity);



