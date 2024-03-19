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
import utils.constants.OrderConstant;
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
            HttpSession session = req.getSession(false);
            UUID userId = (UUID) session.getAttribute(UserConstant.SESSION_USERID);
            Optional<OrderEntity> orderEntity = orderRepository
                    .getOrderByUserId(userId, productId);

            boolean canViewSecret = orderEntity.isPresent() || product.getUserId().equals(userId);

//            boolean canReport = false;
//            if(orderEntity.isPresent()){
//                canReport = orderEntity.get().getStatus() == OrderConstant.ORDER_CHECKING;
//            }

            req.setAttribute("product", product);
            req.setAttribute("VAR_CANVIEWSECRET", canViewSecret);
//            req.setAttribute("VAR_CANREPORT", canReport);
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

        String productIdString = req.getParameter("id");
        String title = req.getParameter("title");
        String priceString = req.getParameter("price");
        String description = req.getParameter("description");
        String contact = req.getParameter("contact");
        String secret = req.getParameter("secret");
        String isPublic = req.getParameter("public");
        try {
            if (StringValidator.isNullOrBlank(productIdString) || !StringValidator.isUUID(productIdString)) {
                req.setAttribute("ERROR_MESSAGE", "Tham số không hợp lệ!");
                dispatcher.forward(req, resp);
                return;
            }
            UUID productId = (UUID) UUID.fromString(productIdString);

            if(StringValidator.isNullOrBlank(priceString)){
                req.setAttribute("ERROR_MESSAGE", "Số tiền không hợp lệ!");
                dispatcher.forward(req, resp);
                return;
            }
            BigInteger price = new BigInteger(priceString);

            Optional<ProductEntity> productEntity = productRepository
                    .getProductById(productId);
            if (productEntity.isEmpty()) {
                req.setAttribute("ERROR_MESSAGE", "Không tìm thấy đơn trung gian!");
                dispatcher.forward(req, resp);
                return;
            }

            ProductEntity product = productEntity.get();
            if(!product.isUpdatable()){
                req.setAttribute("ERROR_MESSAGE", "Không thể cập nhật đơn trung gian này!");
                dispatcher.forward(req, resp);
                return;
            }

            // Tạo đối tượng ProductEntity để cập nhật
            product.setTitle(title);
            product.setDescription(description);
            product.setPrice(price);
            product.setContact(contact);
            product.setSecret(secret);
            product.setPublic(isPublic != null && isPublic.equals("on"));

            // Cập nhật thông tin sản phẩm trong cơ sở dữ liệu
            UUID id = UUID.fromString(productIdString);
            productRepository.update(product);
            resp.sendRedirect(req.getContextPath() + "/market" + "?id=" + productIdString);
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("resultMessage", "Đã xảy ra lỗi khi cập nhật thông tin sản phẩm!");
            dispatcher.forward(req, resp);
        }

    }
}