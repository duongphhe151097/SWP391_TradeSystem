package controllers;

import dataAccess.ProductRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.ProductEntity;
import models.common.Pagination;
import models.common.ViewPaging;
import utils.constants.UserConstant;
import utils.validation.StringValidator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@WebServlet(name = "SalesOrdersController", urlPatterns = "/sale")
public class SalesOrdersController extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ProductRepository productRepository = new ProductRepository();

        HttpSession session = req.getSession(false);
        if (session != null) {
            UUID userId = (UUID) session.getAttribute(UserConstant.SESSION_USERID);
            String sessionId = session.getId();


            boolean isSeller = productRepository.isUserSeller(userId);

            if (isSeller) {
                String currentPage = req.getParameter("current");
                String pageSize = req.getParameter("size");
                String pageRange = req.getParameter("range");

                try {
                    if (StringValidator.isNullOrBlank(currentPage)
                            || StringValidator.isNullOrBlank(pageSize)
                            || StringValidator.isNullOrBlank(pageRange)) {
                        currentPage = "1";
                        pageSize = "5";
                        pageRange = "5";
                    }

                    long userCount = productRepository.countAllByUser(userId);
                    Pagination pagination = new Pagination(userCount, Integer.parseInt(currentPage), Integer.parseInt(pageRange), Integer.parseInt(pageSize));

                    int startPage = (pagination.getCurrentPage() - 1) * pagination.getPageSize();
                    int endPage = pagination.getPageSize();
                    List<ProductEntity> product = productRepository.getUserProductWithPaging(startPage, endPage, userId);

                    req.setAttribute("VIEW_PAGING", new ViewPaging<>(product, pagination));

                    req.getRequestDispatcher("/pages/salesorders.jsp").forward(req, resp);
                } catch (Exception e) {
                    e.printStackTrace();
                    Pagination pagination = new Pagination(0, 1, 5, 10);
                    req.setAttribute("VIEW_PAGING", new ViewPaging<ProductEntity>(new ArrayList<>(), pagination));
                }
            } else {

                resp.sendRedirect("/not_seller.jsp");
            }
        }
    }
}
