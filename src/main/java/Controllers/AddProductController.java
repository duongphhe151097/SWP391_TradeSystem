package Controllers;

import DataAccess.ProductRepository;
import Models.ProductEntity;
import Models.UserEntity;
import Utils.Annotations.Authorization;
import Utils.Validation.StringValidator;
import jakarta.persistence.EntityManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@WebServlet(name = "AddProductController", urlPatterns = "/addproduct")
@Authorization(role = "", isPublic = true)
public class AddProductController extends BaseController {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/pages/addproduct.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String title = req.getParameter("title");
        String priceString = req.getParameter("price");
        String description = req.getParameter("description");
        String contact = req.getParameter("contact");
        String secret = req.getParameter("secret");
        String isPublic = req.getParameter("public");

        try {
            // Validate input data
            if (!StringValidator.isValidDecimal(priceString)) {
                resp.getWriter().println("Invalid price format!");
                return;
            }

            BigDecimal price = new BigDecimal(priceString);

            ProductEntity product = new ProductEntity();
            product.setId(UUID.randomUUID());
            product.setUser_id(UUID.randomUUID());
            product.setTitle(title);
            product.setPrice(price);
            product.setDescription(description);
            product.setContact(contact);
            product.setSecret(secret);
            product.setPublic(isPublic != null && isPublic.equals("on"));

            ProductRepository productRepository = new ProductRepository();
            Optional<ProductEntity> addedProduct = productRepository.addProduct(product);

            if (addedProduct.isPresent()) {
                resp.getWriter().println("Product added successfully!");
            } else {
                resp.getWriter().println("Failed to add product!");
            }

            req.getRequestDispatcher("/pages/salesorders.jsp").forward(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
            resp.getWriter().println("An error occurred while adding the product: " + e.getMessage());
        }
    }
}
