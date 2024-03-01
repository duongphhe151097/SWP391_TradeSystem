package Controllers;

import DataAccess.ProductRepository;
import Utils.Annotations.Authorization;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.hibernate.Cache;

import java.io.IOException;
import java.util.UUID;

@WebServlet(name = "purchaseOrderController", urlPatterns = "/purchaseOrder")
@Authorization(role = "", isPublic = false)
public class purchaseOrderController extends BaseController {

    private ProductRepository productRepository;

    protected void doPost(HttpServletRequest request, HttpServletResponse response, Cache HibernateUtil) throws ServletException, IOException {
        UUID orderId = UUID.fromString(request.getParameter("orderId"));
        UUID userId = (UUID) request.getSession().getAttribute("userId");

        ProductRepository productRepository = new ProductRepository();
        try {
            productRepository.purchaseOrder(orderId, userId, HibernateUtil);
            response.sendRedirect("purchaseSuccess.jsp");
        } catch (IllegalArgumentException e) {
            response.sendRedirect("purchaseError.jsp?message=" + e.getMessage());
        }
    }
}
