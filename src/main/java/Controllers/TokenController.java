package Controllers;

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
            // Validate the token (you may need to implement this method)
            if (isValidActivationToken(token)) {
                // Update user status in the database
                updateUserStatus(token);

                // Forward to a success page or redirect to the login page
                response.sendRedirect("/login?activation=success");
                return;
            }
        }

        // If the token is invalid or missing, forward to an error page
        request.setAttribute("error", "Invalid activation token");
        request.getRequestDispatcher("/error.jsp").forward(request, response);
    }

    private boolean isValidActivationToken(String token) {
        return token != null;
    }

    private void updateUserStatus(String token) {
        System.out.println("User status updated for token: " + token);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }



}
