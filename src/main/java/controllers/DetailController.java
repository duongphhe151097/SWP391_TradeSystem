package controllers;

import dataAccess.ProductRepository;
import dataAccess.UserRepository;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.ProductEntity;
import models.UserEntity;
import utils.constants.UserConstant;


import java.io.IOException;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@WebServlet(name = "DetailController", urlPatterns = "/detail")
public class DetailController extends HttpServlet {

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String productIdString = req.getParameter("id");


        try {
            UUID id = UUID.fromString(productIdString);
            ProductRepository productRepository = new ProductRepository();
            Optional<ProductEntity> productOptional = productRepository.getProductById(id);

            if (productOptional.isPresent()) {
                ProductEntity product = productOptional.get();
                req.setAttribute("product", product);
                HttpSession session = req.getSession(false);
                UUID userId = (UUID) session.getAttribute(UserConstant.SESSION_USERID);
                boolean canViewSecret = product.isPublic() || (userId != null && product.getUserId().equals(userId));
                req.setAttribute("canViewSecret", canViewSecret);

                req.getRequestDispatcher("/pages/detail.jsp").forward(req, resp);
            } else {
                resp.getWriter().println("Sản phẩm không tồn tại");
            }
        } catch (Exception e) {
            e.printStackTrace();
            resp.getWriter().println("Đã xảy ra lỗi");
        }
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ProductRepository productRepository = new ProductRepository();

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
            if (userId == null) {
                req.setAttribute("resultMessage", "Phiên người dùng không hợp lệ!");
                req.getRequestDispatcher("/pages/salesorders.jsp").forward(req, resp);
                return;
            }

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
            resp.sendRedirect(req.getContextPath() + "/sale");

        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("resultMessage", "Đã xảy ra lỗi khi cập nhật thông tin sản phẩm!");
            req.getRequestDispatcher("/pages/salesorders.jsp").forward(req, resp);
        }

    }
}