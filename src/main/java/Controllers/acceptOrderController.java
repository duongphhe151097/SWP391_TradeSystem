package Controllers;

import DataAccess.ProductRepository;
import Models.ProductEntity;
import Utils.Annotations.Authorization;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.hibernate.Cache;

import java.io.IOException;
import java.util.UUID;

@WebServlet(name = "acceptOrderController", urlPatterns = "/acceptOrder")
@Authorization(role = "", isPublic = false)
public class acceptOrderController extends BaseController {

    private ProductRepository productRepository;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UUID id = UUID.fromString(request.getParameter("id"));
        UUID userId = (UUID) request.getSession().getAttribute("userId");

        ProductRepository productRepository = new ProductRepository();
        try {
            productRepository.purchaseOrder(id, userId);
            response.sendRedirect("purchaseSuccess.jsp");
        } catch (IllegalArgumentException e) {
            request.setAttribute("errorMessage", e.getMessage());
            request.getRequestDispatcher("purchaseError.jsp").forward(request, response);
        }
    }

}

