package Controllers;

import DataAccess.UserRepository;
import Models.UserEntity;
import Utils.Annotations.Authorization;
import Utils.Constants.UserConstant;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@WebServlet(name = "ProfileController", urlPatterns = "/profile")
@Authorization(role = "", isPublic = false)
public class ProfileController extends BaseController {
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        UUID userId = (UUID) session.getAttribute(UserConstant.SESSION_USERID);

        try {
            UserRepository userRepository = new UserRepository();
            Optional<UserEntity> userOptional = userRepository.getUserById(userId);

            if (userOptional.isPresent()) {
                UserEntity user = userOptional.get();
                req.setAttribute("user", user);
                req.getRequestDispatcher("/pages/profile.jsp").forward(req, resp);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        UUID userId = (UUID) session.getAttribute(UserConstant.SESSION_USERID);
        try {
            UserRepository userRepository = new UserRepository();
            Optional<UserEntity> userOptional = userRepository.getUserById(userId);

            if (userOptional.isPresent()) {
                UserEntity user = userOptional.get();

                String username = req.getParameter("username");
                String fullname = req.getParameter("fullname");
                String phone_number = req.getParameter("phone_number");

                user.setUsername(username);
                user.setFullName(fullname);
                user.setPhoneNumber(phone_number);

                userRepository.updateUserProfile(userId, username, fullname, phone_number);
                resp.sendRedirect("profile");
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }
}







