package Controllers;

import DataAccess.ProductRepository;
import Models.ProductEntity;
import Models.UserEntity;
import Utils.Validation.StringValidator;
import jakarta.persistence.EntityManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.UUID;

@WebServlet(name = "AddProductController", urlPatterns = "/addproduct")
public class AddProductController extends BaseController{
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/pages/addproduct.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String tile = req.getParameter("tile");
        String priceString = req.getParameter("price");
        String payer = req.getParameter("payer");
        String description = req.getParameter("description");
        String contact = req.getParameter("contact");
        String secret = req.getParameter("secret");
        String isPublic = req.getParameter("public");

        UserEntity u = new UserEntity();
        BigDecimal balance = u.getBalance();


//        if (balance.compareTo(price) >= 0) {
//
//            BigDecimal newBalance = balance.subtract(price);
//            u.setBalance(newBalance);
//
//            // Lưu thông tin giao dịch vào cơ sở dữ liệu
//            TransactionEntity transaction = new TransactionEntity();
//            transaction.setId(UUID.randomUUID());
//            transaction.setUserId(userEntity.getId());
//            transaction.setAmount(price.negate()); // Trừ tiền nên sẽ là số âm
//            transaction.setDescription("Mua sản phẩm: " + product.getTitle());
//            transactionRepository.addTransaction(transaction);
//
//            // Cập nhật thông tin tài khoản người dùng vào cơ sở dữ liệu
//            userRepository.updateUser(userEntity);
//        } else {
//            // Nếu số dư không đủ, thông báo cho người dùng
//            resp.getWriter().println("Số dư không đủ để thực hiện giao dịch!");
//        }

        try {
            // Validate input data
            if (!StringValidator.isValidDecimal(priceString)) {
                resp.getWriter().println("Invalid price format!");
                return;
            }

            // Convert price to decimal
            BigDecimal price = new BigDecimal(priceString);


            ProductEntity product = new ProductEntity();
            product.setId(UUID.randomUUID());
            product.setUser_id(UUID.randomUUID());
            product.setTitle(tile);
            product.setPrice(price);
            product.setDescription(description);
            product.setContact(contact);
            product.setSecret(secret);
            product.setPublic(isPublic != null && isPublic.equals("on"));

            // Save the product to the database
            ProductRepository productRepository =new ProductRepository();
            productRepository.addProduct(product);


            resp.sendRedirect(req.getContextPath() + "/success.jsp");
        } catch (Exception e) {
            e.printStackTrace();
            resp.getWriter().println("An error occurred while adding the product: " + e.getMessage());
        }
    }
}

