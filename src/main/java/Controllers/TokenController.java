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
import Services.SendMailService;
import Models.*;

@WebServlet(name = "TokenController", urlPatterns = {"/TokenController"})
@Authorization(role = "", isPublic = true)
public class TokenController extends BaseController {

    private UserService userService;
    private SendMailService emailService;

    @Override
    public void init() throws ServletException {
        userService = new UserService();
        emailService = new SendMailService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("/pages/error.jsp");
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
        // Implement your validation logic here
        // This may involve checking the token against the database and ensuring it's not expired
        // For simplicity, this example assumes the token is valid if not null
        return token != null;
    }

    private void updateUserStatus(String token) {
        // Implement your logic to update the user status in the database
        // You may use the token to identify the user and update their status
        // For simplicity, this example does not include the actual database update
        System.out.println("User status updated for token: " + token);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }



}
