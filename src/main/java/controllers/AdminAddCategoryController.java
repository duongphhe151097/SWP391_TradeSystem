package controllers;

import dataAccess.CategoryRepository;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.CategoryEntity;
import utils.annotations.Authorization;

import java.io.IOException;
import java.util.Optional;

@WebServlet(name = "AdminAddCategoryController", urlPatterns = {"/admin/category/add"})
@Authorization(role = "ADMIN", isPublic = false)
public class AdminAddCategoryController extends HttpServlet {
    private CategoryRepository cRepo;

    @Override
    public void init() throws ServletException {
        this.cRepo = new CategoryRepository();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/pages/admin/admin_add_category.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String parentId = req.getParameter("parent_id");
        String title = req.getParameter("title");
        RequestDispatcher dispatcher = req.getRequestDispatcher("/pages/admin/admin_add_category.jsp");
        CategoryEntity categoryEntity = CategoryEntity
                .builder()
                .parentId(Integer.parseInt(parentId))
                .title(title)
                .build();

        //Kiểu optional là đối tượng trả ra có thể là null hoặc not null
        Optional<CategoryEntity> categories = cRepo.addCategory(categoryEntity);
        if (categories.isEmpty()) {
            req.setAttribute("FAILED_MESSAGE", true);
            dispatcher.forward(req, resp);
            return;
        }
        resp.sendRedirect(req.getContextPath() + "/admin/category");
    }
}