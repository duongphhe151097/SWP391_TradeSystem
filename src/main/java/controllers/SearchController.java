package controllers;

import dataAccess.ProductRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.ProductEntity;
import utils.annotations.Authorization;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "SearchController", urlPatterns = "/product/search")
@Authorization(role = "USER", isPublic = false)
public class SearchController extends BaseController{
    private ProductRepository productRepository;

    public void init() throws ServletException {
        productRepository = new ProductRepository();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String keyword = request.getParameter("keyword");
        double minPrice = Double.parseDouble(request.getParameter("minPrice"));
        double maxPrice = Double.parseDouble(request.getParameter("maxPrice"));

        List<ProductEntity> productList = productRepository.searchProducts(keyword, minPrice, maxPrice);


        request.setAttribute("products", productList);

        request.getRequestDispatcher("/pages/product/product-sale.jsp").forward(request, response);
    }
}
