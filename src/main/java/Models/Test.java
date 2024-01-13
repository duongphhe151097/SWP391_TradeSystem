package Models;

import DataAccess.DbFactory;
import Utils.Constants.CommonConstants;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class Test {
    public static void main(String[] args) {
        EntityManagerFactory factory = DbFactory.getFactory();
        EntityManager entityManager = factory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
//        transaction.begin();
//
//        UserEntity userEntity = new UserEntity();
//        userEntity.setUsername("aaaa");
//        userEntity.setPassword("a");
//        userEntity.setSalt("a");
//        userEntity.setEmail("testaaa");
//        userEntity.setFullName("test");
//        userEntity.setPhoneNumber("0123");
//        userEntity.setAddress("test");
//        userEntity.setAvatar("a");
//        userEntity.setStatus((short)1);
//        userEntity.setRating(0);
//        userEntity.setDelete(true);
//        userEntity.setCreateBy(CommonConstants.DEFAULT_USER);
//
//
//        entityManager.persist(userEntity);
        UserEntity entities = entityManager
                .createQuery("SELECT u from user u where u.id = :id", UserEntity.class)
                .setParameter("id", UUID.fromString("5884f182-472c-4cbc-a167-7e55ce4c3369"))
                .getSingleResult();
//
//
        entities.getRoleEntities()
                .forEach(roleEntity -> System.out.println(roleEntity.getRoleName()));
//
////        RoleEntity role = new RoleEntity();
////        role.setRoleName("TEST");
////
////        entityManager.persist(role);
//        transaction.commit();

//        transaction.begin();
//        UserRoleEntity userRole = new UserRoleEntity();
//        userRole.setUserId(UUID.fromString("5884f182-472c-4cbc-a167-7e55ce4c3369"));
//        userRole.setRoleId(1);
//        entityManager.persist(userRole);
//        transaction.commit();
//        entities.forEach(entity -> entity.setFullName("ABC"));
//        Optional<UserEntity> entity = entities.stream().findFirst();
//        entity.ifPresent(entityManager::remove);
    }
}
