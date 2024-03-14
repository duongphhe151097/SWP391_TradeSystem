package controllers;

import dataAccess.UserReportRepository;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.UserEntity;
import models.UserReportEntity;
import models.common.Pagination;
import models.common.ViewPaging;
import utils.annotations.Authorization;
import utils.constants.UserConstant;
import utils.convert.DateTimeConvertor;
import utils.validation.StringValidator;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@WebServlet(name = "UserReportController", urlPatterns = "/report")
@Authorization(role = "USER", isPublic = false)
public class UserReportController extends BaseController {
    private UserReportRepository userReportRepository;

    @Override
    public void init() throws ServletException {
        this.userReportRepository = new UserReportRepository();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher dispatcher = req.getRequestDispatcher("/pages/report/user_report.jsp");

        String currentPage = req.getParameter("current");
        String pageSize = req.getParameter("size");
        String pageRange = req.getParameter("range");

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

            if (StringValidator.isNullOrBlank(title)) {
                title = "";
            }

            short status = 0;
            if (StringValidator.isValidReportStatus(filterStatus)) {
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

            HttpSession session = req.getSession(false);
            UUID userId = (UUID) session.getAttribute(UserConstant.SESSION_USERID);

            long reportCount = userReportRepository
                    .countReportByUserIdWithPaging(userId, title, startDateConvert, endDateConvert, status);

            Pagination pagination
                    = new Pagination(reportCount, Integer.parseInt(currentPage), Integer.parseInt(pageRange), Integer.parseInt(pageSize));

            int startPage = (pagination.getCurrentPage() - 1) * pagination.getPageSize();
            int endPage = pagination.getPageSize();
            List<UserReportEntity> userReports = userReportRepository
                    .getReportByUserIdWithPaging(userId ,startPage, endPage, title, startDateConvert, endDateConvert, status);

            req.setAttribute("FILTER_TITLE", title);
            req.setAttribute("FILTER_STATUS", filterStatus);
            req.setAttribute("FILTER_STARTDATE", startDate);
            req.setAttribute("FILTER_ENDDATE", endDate);
            req.setAttribute("VIEW_PAGING", new ViewPaging<>(userReports, pagination));
        } catch (Exception e) {
            Pagination pagination
                    = new Pagination(0, 1, 5, 10);
            req.setAttribute("VIEW_PAGING", new ViewPaging<UserEntity>(new ArrayList<>(), pagination));
        }

        dispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}
