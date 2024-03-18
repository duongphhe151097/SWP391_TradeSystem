package controllers;

import dataAccess.SettingRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.SettingEntity;
import models.common.Pagination;
import models.common.ViewPaging;
import utils.validation.StringValidator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "AdminSettingController", urlPatterns = {"/admin/setting"})
public class AdminSettingController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        SettingRepository settingRepository = new SettingRepository();
        settingRepository.getSetting();

        String currentPage = req.getParameter("current");
        String pageSize = req.getParameter("size");
        String pageRange = req.getParameter("range");
        try {
            if (StringValidator.isNullOrBlank(currentPage)
                    || StringValidator.isNullOrBlank(pageSize)
                    || StringValidator.isNullOrBlank(pageRange)) {
                currentPage = "1";
                pageSize = "10";
                pageRange = "5";
            }



            long userCount = settingRepository.countAllSetting();
            Pagination pagination
                    = new Pagination(userCount, Integer.parseInt(currentPage), Integer.parseInt(pageRange), Integer.parseInt(pageSize));

            int startPage = (pagination.getCurrentPage() - 1) * pagination.getPageSize();
            int endPage = pagination.getPageSize();
            List<SettingEntity> SetSettings = settingRepository
                    .getSettingsWithPaging(startPage, endPage);
            req.setAttribute("VIEW_PAGING", new ViewPaging<>(SetSettings, pagination));

        } catch (Exception e) {
            e.printStackTrace();
            Pagination pagination
                    = new Pagination(0, 1, 5, 10);
            req.setAttribute("VIEW_PAGING", new ViewPaging<SettingEntity>(new ArrayList<>(), pagination));
        }
        req.getRequestDispatcher("/pages/admin/admin_setting.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }
}
