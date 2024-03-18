package controllers;

import dataAccess.SettingRepository;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.SettingEntity;
import utils.annotations.Authorization;

import java.io.IOException;
import java.util.Optional;

@WebServlet(name = "AdminAddSettingController", urlPatterns = {"/admin/setting/add"})
@Authorization(role = "ADMIN", isPublic = false)
public class AdminAddSettingController extends HttpServlet {
    private SettingRepository sRepo;

    @Override
    public void init() throws ServletException {
        this.sRepo = new SettingRepository();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/pages/admin/admin_add_setting.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String keyName = req.getParameter("key_name");
        String value = req.getParameter("value");
        RequestDispatcher dispatcher = req.getRequestDispatcher("/pages/admin/admin_add_setting.jsp");
        SettingEntity settingEntity = SettingEntity
                .builder()
                .keyName(keyName)
                .value(value)
                .build();

        //Kiểu optional là đối tượng trả ra có thể là null hoặc not null
        Optional<SettingEntity> settings = sRepo.addSetting(settingEntity);
        if (settings.isEmpty()) {
            req.setAttribute("FAILED_MESSAGE", true);
            dispatcher.forward(req, resp);
            return;
        }
        resp.sendRedirect(req.getContextPath() + "/admin/setting");
    }
}
