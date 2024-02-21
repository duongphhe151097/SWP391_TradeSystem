package Controllers;


import DataAccess.UserRepository;
import Models.UserEntity;
import Utils.Annotations.Authorization;
import Utils.Constants.UserConstant;
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

@WebServlet(name = "UserBalanceController", urlPatterns = {"/user/balance"})
@Authorization(role = "", isPublic = false)
public class UserBalanceController extends BaseController {

    private UserRepository userRepository;

    @Override
    public void init() throws ServletException {
        userRepository = new UserRepository();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");

        HttpSession session = req.getSession(false);
        UUID userId = (UUID) session.getAttribute(UserConstant.SESSION_USERID);

        Optional<UserEntity> userEntity = userRepository
                .getUserById(userId);

        JsonObject jsonObject = new JsonObject();
        Gson gson = new Gson();

        if(userEntity.isEmpty()){
            resp.setStatus(400);
            jsonObject.addProperty("code", 400);
            jsonObject.addProperty("message", "Người dùng không hợp lệ!");
            resp.getWriter().write(gson.toJson(jsonObject));
            return;
        }

        session.setAttribute(UserConstant.SESSION_BALANCE, userEntity.get().getBalance());

        resp.setStatus(200);
        jsonObject.addProperty("code", 200);
        jsonObject.addProperty("message", userEntity.get().getBalance());
        resp.getWriter().write(gson.toJson(jsonObject));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}
