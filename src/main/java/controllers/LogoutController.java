package controllers;

import dataAccess.SessionManagerRepository;
import utils.annotations.Authorization;
import utils.constants.UserConstant;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.UUID;

@WebServlet(name = "LogoutController", urlPatterns = "/logout")
@Authorization(role = "", isPublic = false)
public class LogoutController extends HttpServlet{

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        /// Invalidate the session (log out the user)
        HttpSession session = request.getSession(false);
        if (session != null) {
            UUID userId = (UUID) session.getAttribute(UserConstant.SESSION_USERID);
            String sessionId = session.getId();
            // Perform any additional cleanup actions if needed
            SessionManagerRepository sessionManagerRepository = new SessionManagerRepository();

            // Delete the session data from the database using the retrieved user ID
            sessionManagerRepository.removeSession(sessionId, userId);
        }

        // Redirect to the login page or any other appropriate page after logout
        response.sendRedirect(request.getContextPath() + "/");
    }

}
