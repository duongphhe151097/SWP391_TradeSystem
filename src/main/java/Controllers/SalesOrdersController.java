package Controllers;

import DataAccess.ProductRepository;
import Models.ProductEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/sales_orders")
public class SalesOrdersController {


        private EntityManager entityManager;
        private ProductRepository productRepository;


        public void init() throws ServletException {

            EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("persistenceUnitName");
            entityManager = entityManagerFactory.createEntityManager();
            productRepository = new ProductRepository(entityManager);
        }


        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            List<ProductEntity> productList = productRepository.getAllProducts();
            request.setAttribute("orders", productList);
            request.getRequestDispatcher("/sales_orders.jsp").forward(request, response);
        }


        public void destroy() {
            entityManager.close();
        }
    }

