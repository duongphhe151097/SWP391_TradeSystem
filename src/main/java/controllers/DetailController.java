package controllers;

import dataAccess.ProductRepository;
import dataAccess.UserRepository;
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
                    req.getRequestDispatcher("/pages/detail.jsp").forward(req, resp);
                } else {
                    resp.getWriter().println("Sản phẩm không tồn tại");
                }
            } catch (IllegalArgumentException e) {
                resp.getWriter().println("ID sản phẩm không hợp lệ");
            } catch (Exception e) {
                e.printStackTrace();
                resp.getWriter().println("Đã xảy ra lỗi");
            }
        }

        protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
   super.doPost(req,resp);
        }
    }

