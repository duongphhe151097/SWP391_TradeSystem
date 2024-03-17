package controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.SettingEntity;
import models.UserEntity;
import utils.constants.UserConstant;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Optional;
import java.util.UUID;

@WebServlet(name = "SettingController", urlPatterns = {"/setting/add"})

public class AddSettingController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/pages/add_setting.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String keyName = req.getParameter("key_name");
        String value = req.getParameter("value");



        SettingEntity settingEntity = SettingEntity
                .builder()
                .keyName(keyName)
                .value(value)
                .
                .build();


        //Kiểu optional là đối tượng trả ra có thể là null hoặc not null
        Optional<UserEntity> user = userRepository.addUser(userEntity);
        if(user.isEmpty()){
            req.setAttribute("FAILED_MESSAGE", "Có lỗi xảy ra khi tạo tài khoản!");
            dispatcher.forward(req, resp);
            return;
        }

    }
}
