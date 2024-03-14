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
        HttpSession session = req.getSession();
        UUID userId = (UUID) session.getAttribute(UserConstant.SESSION_USERID);

        try {
            ProductRepository productRepository = new ProductRepository();
            Optional<ProductEntity> productOptional = productRepository.getProductById(userId);

            if (productOptional.isPresent()) {
                ProductEntity product = productOptional.get();
                req.setAttribute("product", product);
                req.getRequestDispatcher("/pages/detail.jsp").forward(req, resp);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }





    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        UUID userId = (UUID) session.getAttribute(UserConstant.SESSION_USERID);
        doGet(req,resp);


        try {
            ProductRepository productRepository = new ProductRepository();
            Optional<ProductEntity> productOptional = productRepository.getProductById(userId);

            if (productOptional.isPresent()) {
                ProductEntity product = productOptional.get();

                String title = req.getParameter("title");
                String priceString = req.getParameter("price");
                String role = req.getParameter("role");
                String description = req.getParameter("description");
                String contact = req.getParameter("contact");
                String secret = req.getParameter("secret");
                String isPublic = req.getParameter("public");
                BigInteger price = new BigInteger(priceString);
                product.getTitle();
                product.getPrice();
                product.getDescription();
                product.getPrice();
                product.getContact();
                product.setSecret(secret);
                product.isPublic();




                      productRepository.updateProduct( title, price, description, contact, secret, isPublic);
                req.getRequestDispatcher("/pages/detail.jsp").forward(req, resp);
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }
}
