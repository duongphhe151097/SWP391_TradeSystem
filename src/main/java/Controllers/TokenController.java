package Controllers;

import Models.ErrorEntity;
import Services.UserService;
import Utils.Annotations.Authorization;
import jakarta.servlet.RequestDispatcher;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.UUID;

@WebServlet(name = "TokenController", urlPatterns = {"/TokenController"})
@Authorization(role = "", isPublic = true)
public class TokenController extends BaseController {

    private UserService userService;

    @Override
    public void init() throws ServletException {
        userService = new UserService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("/pages/error.jsp");
        // Get the 'id' parameter from the request
        String id = request.getParameter("id");

        if (id != null && !id.isBlank()) {

            UUID uid = UUID.fromString(id);
            userService.UpdateUserStatus(uid, (short) 1);
            response.sendRedirect("/login");
        } else {
            request.setAttribute("error", new ErrorEntity(404, "missing id"));
            dispatcher.forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }



}
