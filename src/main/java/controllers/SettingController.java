package controllers;

import dataAccess.TransactionManagerRepository;
import dataAccess.settingRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.UserEntity;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "SettingController", urlPatterns = {"/setting"})
public class SettingController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/pages/setting.jsp").forward(req, resp);
    }
}
