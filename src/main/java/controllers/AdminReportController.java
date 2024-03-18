package controllers;

import dataAccess.UserReportRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.UserEntity;
import models.UserReportEntity;
import models.common.Pagination;
import models.common.ViewPaging;
import utils.annotations.Authorization;
import utils.convert.DateTimeConvertor;
import utils.validation.StringValidator;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "AdminReportController", urlPatterns = "/admin/report")
@Authorization(role = "ADMIN", isPublic = false)
public class AdminReportController extends BaseController {
    private UserReportRepository userReportRepository;

    @Override
    public void init() throws ServletException {
        this.userReportRepository = new UserReportRepository();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String currentPage = req.getParameter("current");
        String pageSize = req.getParameter("size");
        String pageRange = req.getParameter("range");

        String uname = req.getParameter("f_uname");
        String title = req.getParameter("f_title");
        String filterStatus = req.getParameter("f_status");
        String startDate = req.getParameter("f_start");
        String endDate = req.getParameter("f_end");

        try {
            if (StringValidator.isNullOrBlank(currentPage)
                    || StringValidator.isNullOrBlank(pageSize)
                    || StringValidator.isNullOrBlank(pageRange)) {
                currentPage = "1";
                pageSize = "10";
                pageRange = "5";
            }

            if (StringValidator.isNullOrBlank(uname)) {
                uname = "";
            }

            if (StringValidator.isNullOrBlank(title)) {
                title = "";
            }

            short status = 0;
            if (!StringValidator.isNullOrBlank(filterStatus)) {
                status = Short.parseShort(filterStatus);
            }

            LocalDateTime startDateConvert = null;
            if (!StringValidator.isNullOrBlank(startDate)) {
                startDateConvert = DateTimeConvertor.toLocalDateTime(startDate);
            }

            LocalDateTime endDateConvert = null;
            if (!StringValidator.isNullOrBlank(startDate)) {
                endDateConvert = DateTimeConvertor.toLocalDateTime(endDate);
            }

            long reportCount = userReportRepository
                    .countReportWithPaging(title, uname, startDateConvert, endDateConvert, status);

            Pagination pagination
                    = new Pagination(reportCount, Integer.parseInt(currentPage), Integer.parseInt(pageRange), Integer.parseInt(pageSize));

            int startPage = (pagination.getCurrentPage() - 1) * pagination.getPageSize();
            int endPage = pagination.getPageSize();
            List<UserReportEntity> userReports = userReportRepository
                    .getReportWithPaging(startPage, endPage, title, uname, startDateConvert, endDateConvert, status);

            req.setAttribute("FILTER_UNAME", uname);
            req.setAttribute("FILTER_TITLE", title);
            req.setAttribute("FILTER_STATUS", filterStatus);
            req.setAttribute("FILTER_STARTDATE", startDate);
            req.setAttribute("FILTER_ENDDATE", endDate);
            req.setAttribute("VIEW_PAGING", new ViewPaging<>(userReports, pagination));
        } catch (Exception e) {
            e.printStackTrace();
            Pagination pagination
                    = new Pagination(0, 1, 5, 10);
            req.setAttribute("VIEW_PAGING", new ViewPaging<UserEntity>(new ArrayList<>(), pagination));
        }

        req.getRequestDispatcher("/pages/admin/admin_report-manager.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}
