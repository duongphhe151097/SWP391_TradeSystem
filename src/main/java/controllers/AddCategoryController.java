package controllers;

import dataAccess.CategoryRepository;
import Models.CategoryEntity;
import Utils.Annotations.Authorization;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.hibernate.SessionFactory;

import java.io.IOException;

@WebServlet(name = "AddCategoryController", value = "/addCategory")
@Authorization(role = "ADMIN", isPublic = false)
public class AddCategoryController extends BaseController {
    private CategoryRepository categoryRepository;

    @Override
    public void init() throws ServletException {
        super.init();
        categoryRepository = new CategoryRepository((SessionFactory) getServletContext().getAttribute("sessionFactory"));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int parentId = Integer.parseInt(request.getParameter("parent_id"));
            String title = request.getParameter("title");

            if (parentId < 0) {
                throw new IllegalArgumentException("Parent ID không thể âm.");
            }
            if (title == null || title.isEmpty()) {
                throw new IllegalArgumentException("Title không được bỏ trống.");
            }
            if (categoryRepository.isIdExists(parentId)) {
                throw new IllegalArgumentException("ID đã tồn tại.");
            }

            CategoryEntity category = new CategoryEntity();
            category.setParentId(parentId);
            category.setTitle(title);

            categoryRepository.addCategory(category);

            response.sendRedirect(request.getContextPath() + "/display_categories.jsp");
        } catch (NumberFormatException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Parent ID không hợp lệ.");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Xảy ra lỗi trong quá trình thêm danh mục.");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }
}

