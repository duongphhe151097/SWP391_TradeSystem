package Controllers;

import DataAccess.NotificationRepository;
import DataAccess.UserRepository;
import jakarta.servlet.RequestDispatcher;
import Models.NotificationEntity;
import Models.UserEntity;
import Utils.Annotations.Authorization;
import Utils.Constants.UserConstant;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@WebServlet(name = "ProfileController", urlPatterns = "/profile")
@Authorization(role = "", isPublic = true)
public class ProfileController extends BaseController {
    private UserRepository userRepository;
    private NotificationRepository notificationRepository;

    @Override
    public void init() throws ServletException {
        super.init();
        this.userRepository = new UserRepository();
        this.notificationRepository = new NotificationRepository();
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
                String message ="Bạn đã cập nhật đơn hàng mới thành công";
                NotificationEntity notificationEntity = NotificationEntity
                        .builder()
                        .id(UUID.randomUUID())
                        .userFriedNotify(userId)
                        .userToNotify(userId)
                        .type((short) 1)
                        .message(message)
                        .isSeen(false)
                        .createAt(LocalDateTime.now())
                        .build();
                notificationRepository.add(notificationEntity);
                resp.sendRedirect("profile");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}







