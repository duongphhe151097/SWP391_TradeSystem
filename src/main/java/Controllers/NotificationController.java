package Controllers;

import DataAccess.NotificationRepository;
import Models.NotificationEntity;
import Utils.Annotations.Authorization;
import Utils.Constants.UserConstant;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
@WebServlet(name = "NotificationController", urlPatterns = {"/notification"})
@Authorization(role = "", isPublic = true)
public class NotificationController extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        UUID userToNotify = (UUID) session.getAttribute(UserConstant.SESSION_USERID);

        try {
            NotificationRepository notificationRepository = new NotificationRepository();
            List<NotificationEntity> notificationList = notificationRepository.getNotificationByUser(userToNotify);
            req.setAttribute("notificationList", notificationList);
            req.getRequestDispatcher("/common/header.jsp").forward(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


