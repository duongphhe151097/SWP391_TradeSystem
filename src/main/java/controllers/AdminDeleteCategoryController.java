package controllers;

import dataAccess.CategoryRepository;
import dataAccess.SessionManagerRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.annotations.Authorization;
import utils.validation.StringValidator;

import java.io.IOException;

@WebServlet(name = "AdminDeleteCategoryController", urlPatterns = "/admin/category/delete")
@Authorization(role = "ADMIN", isPublic = false)
public class AdminDeleteCategoryController extends BaseController {

    private CategoryRepository cRepo;
    private SessionManagerRepository sessionManagerRepository;

    public void init() throws ServletException {
        super.init();
        this.cRepo = new CategoryRepository();
        this.sessionManagerRepository = new SessionManagerRepository();
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


        String categoryId = req.getParameter("id");
        String type = req.getParameter("type");

        if (StringValidator.isNullOrBlank(categoryId) || StringValidator.isNullOrBlank(type)) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        try {
            int requestCategoryId = Integer.parseInt(categoryId);

            boolean categoryStatus = Boolean.parseBoolean(type);

            cRepo.updateCategoryStatus(requestCategoryId, categoryStatus);

            resp.sendRedirect(req.getContextPath() + "/admin/category");
        } catch (IllegalArgumentException e) {
            return;
        }

    }
}