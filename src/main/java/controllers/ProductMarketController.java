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
import models.common.Pagination;
import models.common.ViewPaging;
import utils.annotations.Authorization;
import utils.constants.UserConstant;
import utils.validation.StringValidator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@WebServlet(name = "ProductMarketController", urlPatterns = {"/product/market", "/market"})
@Authorization(role = "USER", isPublic = false)
public class ProductMarketController extends BaseController {

    private UserRepository userRepository;
    private ProductRepository productRepository;

    @Override
    public void init() throws ServletException {
        this.userRepository = new UserRepository();
        this.productRepository = new ProductRepository();
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher dispatcher = req.getRequestDispatcher("/pages/product/product-market.jsp");

        HttpSession session = req.getSession(false);
        UUID userId = (UUID) session.getAttribute(UserConstant.SESSION_USERID);

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
            List<ProductEntity> products = productRepository.getAllProducts(startPage, endPage);

            req.setAttribute("VIEW_PAGING", new ViewPaging<>(products, pagination));
        } catch (Exception e) {
            e.printStackTrace();
            Pagination pagination = new Pagination(0, 1, 5, 10);
            req.setAttribute("VIEW_PAGING", new ViewPaging<ProductEntity>(new ArrayList<>(), pagination));
        }
        dispatcher.forward(req, resp);
    }
}

