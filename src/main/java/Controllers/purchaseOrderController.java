package Controllers;

import DataAccess.ProductRepository;
import Models.ProductEntity;
import Utils.Annotations.Authorization;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.UUID;

@WebServlet(name = "purchaseOrderController", urlPatterns = "/purchaseOrder")
@Authorization(role = "", isPublic = false)
public class purchaseOrderController extends BaseController {

    private ProductRepository productRepository;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UUID id = UUID.fromString(request.getParameter("id"));
        ProductRepository productRepository = new ProductRepository();
        ProductEntity order = productRepository.getOrderDetails(id);

        request.setAttribute("order", order);
        request.getRequestDispatcher("/purchaseOrder.jsp").forward(request, response);
    }

}