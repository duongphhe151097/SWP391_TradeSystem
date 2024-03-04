package Controllers;

import DataAccess.ProductRepository;
import Models.ProductEntity;
import Utils.Annotations.Authorization;
import Utils.Constants.UserConstant;
import Utils.Validation.StringValidator;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@WebServlet(name = "AddProductController", urlPatterns = "/addproduct")
@Authorization(role = "", isPublic = true)
public class AddProductController extends BaseController {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/pages/addproduct.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String title = req.getParameter("title");
            String priceString = req.getParameter("price");
            String description = req.getParameter("description");
            String contact = req.getParameter("contact");
            String secret = req.getParameter("secret");
            String isPublic = req.getParameter("public");


            if (!StringValidator.isValidDecimal(priceString)) {
                sendErrorMessage(req, resp, "Định dạng giá không hợp lệ!");
                return;
            }

            BigDecimal price = new BigDecimal(priceString);
            HttpSession session = req.getSession();
            UUID userId = (UUID) session.getAttribute(UserConstant.SESSION_USERID);

            if (userId == null) {
                sendErrorMessage(req, resp, "Phiên người dùng không hợp lệ!");
                return;
            }


            ProductRepository productRepository = new ProductRepository();
            ProductEntity productEntity = ProductEntity.builder()
                    .id(UUID.randomUUID())
                    .userId(userId)
                    .title(title)
                    .categoryId(productRepository.getCategoryId())
                    .description(description)
                    .secret(secret)
                    .price(price)
                    .contact(contact)
                    .isPublic(isPublic != null && isPublic.equals("on"))
                    .updatable(false)
                    .quantity(0)
                    .status((short) 6)
                    .build();


            int categoryId = productRepository.getCategoryId();




            Optional<ProductEntity> addedProduct = productRepository.addProduct(productEntity);
            if (addedProduct.isPresent()) {
                resp.sendRedirect(req.getContextPath() + "/pages/salesorders.jsp");
            } else {
                sendErrorMessage(req, resp, "Lỗi khi thêm sản phẩm vào cơ sở dữ liệu!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendErrorMessage(req, resp, "Đã xảy ra lỗi khi thêm sản phẩm: " + e.getMessage());
        }
    }

    private void sendErrorMessage(HttpServletRequest req, HttpServletResponse resp, String message) throws ServletException, IOException {
        req.setAttribute("errorMessage", message);
        req.getRequestDispatcher("/pages/addproduct.jsp").forward(req, resp);
    }
}
