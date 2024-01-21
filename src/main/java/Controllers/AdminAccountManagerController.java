package Controllers;

import DataAccess.UserRepository;
import Models.Common.Pagination;
import Models.Common.ViewPaging;
import Models.UserEntity;
import Utils.Annotations.Authorization;
import Utils.Convert.DateTimeConvertor;
import Utils.Validation.StringValidator;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "AdminAccountManagerController", urlPatterns = "/admin/account")
@Authorization(role = "ADMIN", isPublic = false)
public class AdminAccountManagerController extends BaseController {
    private UserRepository userRepository;

    @Override
    public void init() throws ServletException {
        this.userRepository = new UserRepository();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String currentPage = req.getParameter("current");
        String pageSize = req.getParameter("size");
        String pageRange = req.getParameter("range");
        String search = req.getParameter("search");
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

            if (search == null || search.isBlank()) {
                search = "";
            }

            if (!StringValidator.isValidUserStatus(filterStatus)) {
                filterStatus = "ALL";
            }

            LocalDateTime startDateConvert = null;
            if (startDate != null && !startDate.isBlank()){
                startDateConvert = DateTimeConvertor.toLocalDateTime(startDate);
            }

            LocalDateTime endDateConvert = null;
            if (startDate != null && !startDate.isBlank()){
                endDateConvert = DateTimeConvertor.toLocalDateTime(endDate);
            }

            long userCount = userRepository.countAll(search, filterStatus, startDateConvert, endDateConvert);
            Pagination pagination
                    = new Pagination(userCount, Integer.parseInt(currentPage), Integer.parseInt(pageRange), Integer.parseInt(pageSize));

            int startPage = (pagination.getCurrentPage() - 1) * pagination.getPageSize();
            int endPage = pagination.getPageSize();
            List<UserEntity> users = userRepository
                    .getAllWithPaging(startPage, endPage, search, filterStatus, startDateConvert, endDateConvert);
            req.setAttribute("FILTER_SEARCH", search);
            req.setAttribute("FILTER_STATUS", filterStatus);
            req.setAttribute("FILTER_STARTDATE", startDate);
            req.setAttribute("FILTER_ENDDATE", endDate);
            req.setAttribute("VIEW_PAGING", new ViewPaging<>(users, pagination));
        } catch (Exception e) {
            e.printStackTrace();
            Pagination pagination
                    = new Pagination(0, 1, 5, 10);
            req.setAttribute("VIEW_PAGING", new ViewPaging<UserEntity>(new ArrayList<>(), pagination));
        }

        req.getRequestDispatcher("/pages/admin/admin_account-manager.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}
