package Controllers;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class ActiveAccountController extends BaseController{
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Extract activation token from the request
        String activationToken = req.getParameter("t");

        // Perform activation logic (e.g., update user status in the database)
        boolean isActivated = activateUserAccount(activationToken);

        // Set activation status message for display on the JSP page
        req.setAttribute("activationMessage", isActivated ? "Account activated successfully!" : "Activation failed.");

        // Forward to the activation status JSP page
        RequestDispatcher dispatcher = req.getRequestDispatcher("/pages/activate.jsp");
        dispatcher.forward(req, resp);
    }

    // Implement the method to activate the user account using the activation token
    private boolean activateUserAccount(String activationToken) {
        // ... (Implement your activation logic, update user status, etc.)
        // Return true if activation is successful, false otherwise
        return true; // or false
    }
}
