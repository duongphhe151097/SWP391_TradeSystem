package controllers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dataAccess.SessionManagerRepository;
import dataAccess.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.SessionManagerEntity;
import utils.annotations.Authorization;
import utils.constants.UserConstant;
import utils.validation.StringValidator;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@WebServlet(name = "AdminAccountChangeStatusController", urlPatterns = "/admin/account/status")
@Authorization(role = "ADMIN", isPublic = false)
public class AdminAccountChangeStatusController extends BaseController{
    private UserRepository userRepository;
    private SessionManagerRepository sessionManagerRepository;

    @Override
    public void init() throws ServletException {
        super.init();
        this.userRepository = new UserRepository();
        this.sessionManagerRepository = new SessionManagerRepository();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");

        String userId = req.getParameter("id");
        String type = req.getParameter("type");

        JsonObject jsonObject = new JsonObject();
        Gson gson = new Gson();

        if(StringValidator.isNullOrBlank(userId) || StringValidator.isNullOrBlank(type)) {
            resp.setStatus(400);
            jsonObject.addProperty("code", 400);
            jsonObject.addProperty("message", "id người dùng không hợp lệ!");
            resp.getWriter().write(gson.toJson(jsonObject));
            return;
        }

        HttpSession session = req.getSession(false);
        UUID sessionUserId = (UUID) session.getAttribute(UserConstant.SESSION_USERID);
        UUID requestUserId = UUID.fromString(userId);
        if(sessionUserId.equals(requestUserId)){
            resp.setStatus(400);
            jsonObject.addProperty("code", 400);
            jsonObject.addProperty("message", "Không thể cập nhật trạng thái của account hiện tại!");
            resp.getWriter().write(gson.toJson(jsonObject));
            return;
        }

        short userStatus = UserConstant.ACTIVE;
        if(type.equals("active")){
            userStatus = UserConstant.ACTIVE;
        }
        if(type.equals("banned")){
            userStatus = UserConstant.BANNED;
        }

        userRepository.updateUserStatus(requestUserId, userStatus);
        Optional<SessionManagerEntity> sessionManagerEntity = sessionManagerRepository
                .getSessionByUserId(requestUserId);
        sessionManagerEntity.ifPresent(entity -> sessionManagerRepository.removeSession(entity.getSessionId(), requestUserId));

        resp.setStatus(200);
        jsonObject.addProperty("code", 200);
        jsonObject.addProperty("message", "Cập nhật trạng thái hiện tại!");
        resp.getWriter().write(gson.toJson(jsonObject));
    }
}
