package controllers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dataAccess.SessionManagerRepository;
import dataAccess.SettingRepository;
import dataAccess.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.SessionManagerEntity;
import utils.constants.UserConstant;
import utils.validation.StringValidator;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;


@WebServlet(name = "AdminDeleteSetting", urlPatterns = "/admin/setting/delete")
public class AdminDeleteSetting extends BaseController {

    private SettingRepository settingRepository;
    private SessionManagerRepository sessionManagerRepository;

    public void init() throws ServletException {
        super.init();
        this.settingRepository = new SettingRepository();
        this.sessionManagerRepository = new SessionManagerRepository();
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


        String settingId = req.getParameter("id");
        String type = req.getParameter("type");

        if (StringValidator.isNullOrBlank(settingId) || StringValidator.isNullOrBlank(type)) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        try {
            int requestSettingId = Integer.parseInt(settingId);

            boolean settingStatus = Boolean.parseBoolean(type);

            settingRepository.updateSettingStatus(requestSettingId, settingStatus);

            resp.sendRedirect(req.getContextPath() + "/admin/setting");
        } catch (IllegalArgumentException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

    }
}
