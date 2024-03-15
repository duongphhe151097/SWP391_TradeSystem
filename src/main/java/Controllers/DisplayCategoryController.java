package Controllers;

import DataAccess.CategoryRepository;
import Models.CategoryEntity;
import Utils.Annotations.Authorization;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.hibernate.SessionFactory;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "DisplayCategoryServlet", value = "/displayCategories")
@Authorization(role = "", isPublic = false)
public class DisplayCategoryController extends BaseController {
    private CategoryRepository categoryRepository;

    @Override
    public void init() throws ServletException {
        super.init();
        categoryRepository = new CategoryRepository((SessionFactory) getServletContext().getAttribute("sessionFactory"));
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<CategoryEntity> categories = categoryRepository.getAllCategories();
        request.setAttribute("categories", categories);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/display_categories.jsp");
        dispatcher.forward(request, response);
    }
}