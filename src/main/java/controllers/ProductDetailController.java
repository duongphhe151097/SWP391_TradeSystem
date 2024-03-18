package controllers;

import dataAccess.OrderRepository;
import dataAccess.ProductRepository;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.OrderEntity;
import models.ProductEntity;
import utils.annotations.Authorization;
import utils.constants.UserConstant;
import utils.validation.StringValidator;


import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@WebServlet(name = "ProductDetailController", urlPatterns = "/product/detail")
@Authorization(role = "USER", isPublic = false)
public class ProductDetailController extends BaseController {

    private ProductRepository productRepository;
    private OrderRepository orderRepository;

    @Override
    public void init() throws ServletException {
        this.productRepository = new ProductRepository();
        this.orderRepository = new OrderRepository();
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher dispatcher = req.getRequestDispatcher("/pages/product/product-detail.jsp");
        String productIdString = req.getParameter("id");

        try {
            if (StringValidator.isNullOrBlank(productIdString) || !StringValidator.isUUID(productIdString)) {
                req.setAttribute("ERROR_MESSAGE", "Tham số không hợp lệ!");
                dispatcher.forward(req, resp);
                return;
            }

            UUID productId = UUID.fromString(productIdString);
            Optional<ProductEntity> productOptional = productRepository.getProductById(productId);

            if (productOptional.isEmpty()) {
                req.setAttribute("ERROR_MESSAGE", "Không tìm thấy đơn trung gian!");
                dispatcher.forward(req, resp);
                return;
            }

            ProductEntity product = productOptional.get();
            req.setAttribute("product", product);
            HttpSession session = req.getSession(false);
            UUID userId = (UUID) session.getAttribute(UserConstant.SESSION_USERID);
            Optional<OrderEntity> orderEntity = orderRepository
                    .getOrderByUserId(userId, productId);
            boolean canViewSecret = orderEntity.isPresent() || product.getUserId().equals(userId);

            req.setAttribute("VAR_CANVIEWSECRET", canViewSecret);
            String secretLink = req.getRequestURL().toString() + "?id=" + productId.toString();
            req.setAttribute("VAR_SECRETURL", secretLink);

        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("ERROR_MESSAGE", "Xảy ra lỗi khi tải trang!");
        }
        dispatcher.forward(req, resp);
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher dispatcher = req.getRequestDispatcher("/pages/product/product-sale.jsp");
        try {
            String productIdString = req.getParameter("id");
            String title = req.getParameter("title");
            String priceString = req.getParameter("price");
            String description = req.getParameter("description");
            String contact = req.getParameter("contact");
            String secret = req.getParameter("secret");
            String isPublic = req.getParameter("public");
            LocalDateTime updateAt = LocalDateTime.now();
            BigInteger price = new BigInteger(priceString);

            HttpSession session = req.getSession(false);
            UUID userId = (UUID) session.getAttribute(UserConstant.SESSION_USERID);

            // Tạo đối tượng ProductEntity để cập nhật
            ProductEntity productEntity = new ProductEntity();
            productEntity.setUserId(userId);
            productEntity.setTitle(title);
            productEntity.setDescription(description);
            productEntity.setPrice(price);
            productEntity.setContact(contact);
            productEntity.setSecret(secret);
            productEntity.setPublic(isPublic != null && isPublic.equals("on"));

            // Cập nhật thông tin sản phẩm trong cơ sở dữ liệu
            UUID id = UUID.fromString(productIdString);
            productRepository.updateProduct(id, title, price, description, contact, secret, isPublic, updateAt);
            resp.sendRedirect(req.getContextPath() + "/market");

        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("resultMessage", "Đã xảy ra lỗi khi cập nhật thông tin sản phẩm!");
            dispatcher.forward(req, resp);
        }

    }
}