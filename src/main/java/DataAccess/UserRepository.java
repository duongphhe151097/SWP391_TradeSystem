package DataAccess;

import Models.UserEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UserRepository {
    private final EntityManager entityManager;
    private final EntityTransaction transaction;

    public UserRepository() {
        EntityManagerFactory factory = DbFactory.getFactory();
        this.entityManager = factory.createEntityManager();
        this.transaction = entityManager.getTransaction();
    }

    public Optional<UserEntity> getUserById(UUID userId) {
        try {
            UserEntity entity = entityManager
                    .createQuery("FROM user u where u.id = :userId", UserEntity.class)
                    .setParameter("userId", userId)
                    .getSingleResult();

            return Optional.ofNullable(entity);
        }catch (Exception e){
            e.printStackTrace();
        }

        return Optional.empty();
    }

    public static void main(String[] args) {
        UserRepository userRepository = new UserRepository();
        EntityManagerFactory factory = DbFactory.getFactory();
        EntityManager manager = factory.createEntityManager();
        EntityTransaction entityTransaction = manager.getTransaction();

        entityTransaction.begin();
        manager.createQuery("UPDATE user u SET u.password = :password WHERE u.username = :username")
                .setParameter("password", "22222")
                .setParameter("username", "duongph")
                .executeUpdate();

//        UserEntity entity = manager.createQuery("FROM user u WHERE u.username = :username", UserEntity.class)
//                .setParameter("username", "duongph")
//                .getSingleResult();
//        entity.setPassword("00000");
        entityTransaction.commit();


//        UserEntity user = userRepository.getUserById(UUID.fromString("5884f182-472c-4cbc-a167-7e55ce4c3369")).get();
//        System.out.println(user.getUsername());


//        UserEntity userEntity = new UserEntity();
//        userEntity.setUsername("duongph");
//        userEntity.setPassword("12345");
//        userEntity.setSalt("a");
//        userEntity.setEmail("duongph@gmail.com");
//        userEntity.setFullName("test");
//        userEntity.setPhoneNumber("0123");
//        userEntity.setAddress("test");
//        userEntity.setAvatar("a");
//        userEntity.setStatus((short)1);
//        userEntity.setRating(0);
//        userEntity.setDelete(true);
//        userEntity.setCreateBy("duongph");
//        userRepository.addUser(userEntity);
    }

    public Optional<UserEntity> getUserByUsername(String username) {
        try {
            UserEntity entity = entityManager
                    .createQuery("FROM user u where u.username = :username", UserEntity.class)
                    .setParameter("username", username)
                    .getSingleResult();

            return Optional.ofNullable(entity);
        }catch (Exception e){
            e.printStackTrace();
        }

        return Optional.empty();
    }

    public Optional<UserEntity> getUserByEmail(String email) {
        try {
            UserEntity entity = entityManager
                    .createQuery("FROM user u where u.email = :email", UserEntity.class)
                    .setParameter("email", email)
                    .getSingleResult();

            return Optional.ofNullable(entity);
        }catch (Exception e){
            e.printStackTrace();
        }

        return Optional.empty();
    }

    public Optional<UserEntity> addUser(UserEntity user) {
        try {
            transaction.begin();
            UserEntity entity = (UserEntity) entityManager.merge(user);
            entityManager.persist(entity);

            transaction.commit();

            return Optional.of(entity);
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        }

        return Optional.empty();
    }

    public boolean updateUserStatus(UUID userId, short status) {
        try {
            transaction.begin();
            entityManager.createQuery("UPDATE user u SET u.status = :status WHERE u.id = :id")
                    .setParameter("id", userId)
                    .setParameter("status", status)
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
