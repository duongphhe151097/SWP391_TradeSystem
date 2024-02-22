package Controllers;

import DataAccess.SessionManagerRepository;
import Models.SessionManagerEntity;
import Utils.Annotations.Authorization;
import Utils.Constants.UserConstant;
import Utils.Validation.StringValidator;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@WebServlet(name = "AdminAccountSessionController", urlPatterns = "/admin/account/session")
@Authorization(role = "ADMIN", isPublic = false)
public class AdminAccountSessionController extends BaseController {
    private SessionManagerRepository sessionManagerRepository;

    @Override
    public void init() throws ServletException {
        super.init();
        this.sessionManagerRepository = new SessionManagerRepository();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");

        String uid = req.getParameter("uid");
        String type = req.getParameter("type");

        JsonObject jsonObject = new JsonObject();
        Gson gson = new Gson();

        try {
            if (StringValidator.isNullOrBlank(uid) || StringValidator.isNullOrBlank(type)) {
                resp.setStatus(400);
                jsonObject.addProperty("code", 400);
                jsonObject.addProperty("message", "Tham số không hợp lệ!");
                resp.getWriter().write(gson.toJson(jsonObject));
                return;
            }

            HttpSession session = req.getSession(false);
            UUID sessionUserId = (UUID) session.getAttribute(UserConstant.SESSION_USERID);
            UUID requestUserId = UUID.fromString(uid);

            if (sessionUserId.equals(requestUserId)) {
                resp.setStatus(400);
                jsonObject.addProperty("code", 400);
                jsonObject.addProperty("message", "Không thể thục hiện yêu cầu!");
                resp.getWriter().write(gson.toJson(jsonObject));
                return;
            }

            Optional<SessionManagerEntity> optionalSessionManager = sessionManagerRepository
                    .getSessionByUserId(requestUserId);

            if(optionalSessionManager.isEmpty()){
                resp.setStatus(400);
                jsonObject.addProperty("code", 400);
                jsonObject.addProperty("message", "Session ko hợp lệ!");
                resp.getWriter().write(gson.toJson(jsonObject));
                return;
            }

            sessionManagerRepository.removeSession(optionalSessionManager.get().getSessionId(), requestUserId);

            resp.setStatus(200);
            jsonObject.addProperty("code", 200);
            jsonObject.addProperty("message", "Hủy session thành công!");
            resp.getWriter().write(gson.toJson(jsonObject));
        } catch (Exception e) {
            resp.setStatus(409);
            jsonObject.addProperty("code", 409);
            jsonObject.addProperty("message", "Hủy session không thành công!");
            resp.getWriter().write(gson.toJson(jsonObject));
        }
    }
}
