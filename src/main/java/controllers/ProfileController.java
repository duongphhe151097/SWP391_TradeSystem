package controllers;

import dataAccess.NotificationRepository;
import dataAccess.UserRepository;
import models.NotificationEntity;
import models.UserEntity;
import utils.annotations.Authorization;
import utils.constants.UserConstant;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@WebServlet(name = "ProfileController", urlPatterns = "/profile")
@Authorization(role = "", isPublic = false)
public class ProfileController extends BaseController {
    private NotificationRepository notificationRepository;
    private UserRepository userRepository;
    @Override
    public void init() throws ServletException {
        super.init();
        this.notificationRepository = new NotificationRepository();
        this.userRepository = new UserRepository();
    }
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
                String fullname = req.getParameter("fullname");
                String phone_number = req.getParameter("phone_number");
                user.setFullName(fullname);
                user.setPhoneNumber(phone_number);

                userRepository.updateUserProfile(userId, fullname, phone_number);
                String message = "Bạn đã cập nhật đơn hàng mới thành công.";
                NotificationEntity notificationEntity = NotificationEntity
                        .builder()
                        .id(UUID.randomUUID())
                        .userToNotify(userId)
                        .type((short)1)
                        .message(message)
                        .isSeen(false)
                        .build();
                notificationRepository.add(notificationEntity);

                resp.sendRedirect("profile");
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }
}







