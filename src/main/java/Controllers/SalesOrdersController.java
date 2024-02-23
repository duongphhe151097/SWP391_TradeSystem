package Controllers;

import DataAccess.ProductRepository;
import Models.ProductEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "SalesOrdersController", urlPatterns = "/sale")
public class SalesOrdersController extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        EntityManager entityManager = Persistence.createEntityManagerFactory("tradesys").createEntityManager();

        ProductRepository productRepository = new ProductRepository();
        List<ProductEntity> products = productRepository.getAllProducts();

        req.setAttribute("products", products);
        req.getRequestDispatcher("/pages/salesorders.jsp").forward(req, resp);
    }
}
