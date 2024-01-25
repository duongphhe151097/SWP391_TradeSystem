package Controllers;

import Models.UserEntity;
import Utils.Annotations.Authorization;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "TokenController", urlPatterns = {"/TokenController"})
@Authorization(role = "", isPublic = true)
public class TokenController extends BaseController {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String token = request.getParameter("token");

        if (token != null) {
            // Validate the token
            // Update user status in the database
            updateUserStatus(token);

            // Forward to a success page or redirect to the login page
            response.sendRedirect("/login?activation=success");
            return;
        }else{
            // If the token is invalid or missing, forward to an error page
            request.setAttribute("error", "Invalid activation token");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }

    private void updateUserStatus(String token) {
        
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }



}
