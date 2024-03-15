package DataAccess;

import Models.ProductEntity;
import Models.UserEntity;
import org.hibernate.*;
import org.hibernate.cfg.Configuration;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.UUID;

public class ProductRepository {
    private SessionFactory sessionFactory;

    public ProductRepository() {
        sessionFactory = new Configuration().configure("persistence.xml").buildSessionFactory();
    }

    public void purchaseOrder(UUID id, UUID userId) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();

            ProductEntity product = session.get(ProductEntity.class, id);

            if (product == null) {
                throw new IllegalArgumentException("Không tìm thấy đơn hàng.");
            }

            UserEntity user = session.get(UserEntity.class, userId);
            BigDecimal productPrice = product.getPrice();
            BigInteger productPriceBigInteger = productPrice.toBigInteger();
            if (user.getBalance().compareTo(productPriceBigInteger) < 0) {
                throw new IllegalArgumentException("Tài khoản không đủ tiền.");
            }

            BigInteger newBalance = user.getBalance().subtract(productPriceBigInteger);
            user.setBalance(newBalance);

            session.update(user);

            transaction.commit();

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            throw new IllegalArgumentException("Mua thất bại.");
        } finally {
            session.close();
        }
    }

    public ProductEntity getOrderDetails(UUID id) {
        Session session = sessionFactory.openSession();
        try {
            return session.get(ProductEntity.class, id);
        } finally {
            session.close();
        }
    }

}
