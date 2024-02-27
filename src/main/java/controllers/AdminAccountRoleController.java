package controllers;

import dataAccess.RoleRepository;
import utils.annotations.Authorization;
import utils.constants.UserConstant;
import utils.validation.StringValidator;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.UUID;

@WebServlet(name = "AdminAccountRoleController", urlPatterns = "/admin/account/role")
@Authorization(role = "ADMIN", isPublic = false)
public class AdminAccountRoleController extends BaseController {
    private RoleRepository roleRepository;

    @Override
    public void init() throws ServletException {
        super.init();
        this.roleRepository = new RoleRepository();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");

        String uid = req.getParameter("uid");
        String rid = req.getParameter("rid");
        String type = req.getParameter("type");

        JsonObject jsonObject = new JsonObject();
        Gson gson = new Gson();

        try {
            if (StringValidator.isNullOrBlank(uid) || StringValidator.isNullOrBlank(rid) || StringValidator.isNullOrBlank(type)) {
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
                jsonObject.addProperty("message", "Không thể cập vai trò của account hiện tại!");
                resp.getWriter().write(gson.toJson(jsonObject));
                return;
            }

            int roleId = Integer.parseInt(rid);
            boolean isSuccess = roleRepository
                    .removeUserRole(requestUserId, roleId);

            if (!isSuccess) {
                resp.setStatus(409);
                jsonObject.addProperty("code", 409);
                jsonObject.addProperty("message", "Cập nhật thất bại!");
                resp.getWriter().write(gson.toJson(jsonObject));
                return;
            }

            resp.setStatus(200);
            jsonObject.addProperty("code", 200);
            jsonObject.addProperty("message", "Xóa thành công!");
            resp.getWriter().write(gson.toJson(jsonObject));
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(409);
            jsonObject.addProperty("code", 409);
            jsonObject.addProperty("message", "Cập nhật thất bại!");
            resp.getWriter().write(gson.toJson(jsonObject));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");

        String uid = req.getParameter("uid");
        String rid = req.getParameter("rid");
        JsonObject jsonObject = new JsonObject();
        Gson gson = new Gson();
        try {
            if (StringValidator.isNullOrBlank(uid) || StringValidator.isNullOrBlank(rid)) {
                resp.setStatus(400);
                jsonObject.addProperty("code", 400);
                jsonObject.addProperty("message", "Tham số không hợp lệ!");
                resp.getWriter().write(gson.toJson(jsonObject));
                return;
            }

            UUID requestUserId = UUID.fromString(uid);
            int roleId = Integer.parseInt(rid);

            boolean isSuccess = roleRepository
                    .addUserRole(requestUserId, roleId);

            if (!isSuccess) {
                resp.setStatus(409);
                jsonObject.addProperty("code", 409);
                jsonObject.addProperty("message", "Thêm thất bại!");
                resp.getWriter().write(gson.toJson(jsonObject));
                return;
            }

            resp.setStatus(200);
            jsonObject.addProperty("code", 200);
            jsonObject.addProperty("message", "Thêm thành công!");
            resp.getWriter().write(gson.toJson(jsonObject));
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(409);
            jsonObject.addProperty("code", 409);
            jsonObject.addProperty("message", "Thêm thất bại!");
            resp.getWriter().write(gson.toJson(jsonObject));
        }
    }

}
