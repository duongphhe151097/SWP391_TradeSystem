package DataAccess;

import Models.ProductEntity;
import Models.UserEntity;
import org.hibernate.Cache;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.UUID;

public class ProductRepository {

    public void purchaseOrder(UUID orderId, UUID userId, Cache HibernateUtil) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();

            ProductEntity product = session.get(ProductEntity.class, orderId);

            if (product == null) {
                throw new IllegalArgumentException("Không tìm thấy đơn hàng.");
            }

            UserEntity user = session.get(UserEntity.class, userId);
            BigDecimal productPrice = product.getPrice();
            BigInteger productPriceBigInteger = productPrice.toBigInteger(); // Chuyển đổi giá tiền sang kiểu BigInteger
            if (user.getBalance().compareTo(productPriceBigInteger) < 0) {
                throw new IllegalArgumentException("Tài khoản không đủ.");
            }

            BigInteger newBalance = user.getBalance().subtract(productPriceBigInteger);
            user.setBalance(newBalance);

            session.update(user);

            transaction.commit();


        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            // Xử lý lỗi
            throw new IllegalArgumentException("Mua thất bại.");
        } finally {
            session.close();
        }
    }
}
