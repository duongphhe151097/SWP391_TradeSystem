package controllers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dataAccess.NotificationRepository;
import models.NotificationEntity;
import utils.annotations.Authorization;
import utils.constants.UserConstant;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@WebServlet(name = "NotificationController", urlPatterns = {"/notification"})
@Authorization(role = "", isPublic = true)
public class NotificationController extends BaseController {
    private NotificationRepository notificationRepository;
    JsonObject jsonObject = new JsonObject();
    private Gson gson;

    @Override
    public void init() throws ServletException {
        this.notificationRepository = new NotificationRepository();
        gson = new Gson();
    }
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        UUID userToNotify = (UUID) session.getAttribute(UserConstant.SESSION_USERID);

        try {
            NotificationRepository notificationRepository = new NotificationRepository();
            List<NotificationEntity> notificationList = notificationRepository.getNotificationByUser(userToNotify);
            Gson gson = new Gson();
            String json = gson.toJson(notificationList);

            resp.setStatus(200);
            jsonObject.addProperty("code", 200);
            jsonObject.addProperty("message", "Cập nhật thông báo thành công!");
            resp.getWriter().write(gson.toJson(jsonObject));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");

        String notificationId = req.getParameter("notificationId");
        boolean isSeen = Boolean.parseBoolean(req.getParameter("isSeen"));

        JsonObject jsonObject = new JsonObject();
        Gson gson = new Gson();

        try {
            if (notificationId != null && !notificationId.isEmpty()) {
                boolean success = notificationRepository.updateNotificationStatus(UUID.fromString(notificationId), isSeen);
                if (success) {
                    resp.setStatus(200);
                    jsonObject.addProperty("message", "Cập nhật trạng thái thông báo thành công!");
                } else {
                    resp.setStatus(400);
                    jsonObject.addProperty("message", "Cập nhật trạng thái thông báo không thành công!");
                }
            } else {
                resp.setStatus(400);
                jsonObject.addProperty("message", "Tham số không hợp lệ!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(500);
            jsonObject.addProperty("message", "Đã xảy ra lỗi trong quá trình xử lý!");
        }

        resp.getWriter().write(gson.toJson(jsonObject));
    }
}