package controllers;
import models.NotificationEntity;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dataAccess.NotificationRepository;
import utils.annotations.Authorization;
import utils.constants.UserConstant;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@WebServlet(name = "NotificationController", urlPatterns = {"/notification"})
@Authorization(role = "", isPublic = false)
public class NotificationController extends BaseController {
    private NotificationRepository notificationRepository;

    @Override
    public void init() throws ServletException {
        this.notificationRepository = new NotificationRepository();
    }
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        Gson gson = new Gson();
        JsonObject jsonObject = new JsonObject();
        HttpSession session = req.getSession(false);
        UUID userToNotify = (UUID) session.getAttribute(UserConstant.SESSION_USERID);

        try {
            // Lấy danh sách thông báo từ repository
            List<NotificationEntity> notificationList = notificationRepository.getNotificationByUser(userToNotify);

            String json = gson.toJson(notificationList);
            resp.setStatus(200);
            resp.getWriter().write(json);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        Gson gson = new Gson();
        JsonObject jsonObject = new JsonObject();

        try {
            // Đọc dữ liệu JSON từ request body
            BufferedReader reader = req.getReader();
            JsonObject requestData = gson.fromJson(reader, JsonObject.class);

            // Trích xuất các thông tin từ dữ liệu JSON
            String notificationId = requestData.get("notificationId").getAsString();
            boolean isSeen = requestData.get("isSeen").getAsBoolean();

            // Thực hiện cập nhật trạng thái thông báo
            if (notificationId != null && !notificationId.isEmpty()) {
                boolean success = notificationRepository.updateNotificationStatus(UUID.fromString(notificationId), isSeen);
                if (success) {
                    resp.setStatus(200);
                    jsonObject.addProperty("code", 200);
                    jsonObject.addProperty("message", "Cập nhật thành công!");
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
