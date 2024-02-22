package Controllers;

import DataAccess.TransactionManagerRepository;
import Models.Common.Pagination;
import Models.Common.ViewPaging;
import Models.ExternalTransactionEntity;
import Models.UserEntity;
import Utils.Annotations.Authorization;
import Utils.Convert.DateTimeConvertor;
import Utils.Validation.StringValidator;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "ExternalTransactionController", urlPatterns = {"/externalTransaction"})
@Authorization(role = "", isPublic = false)
public class ExternalTransactionController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        TransactionManagerRepository transactionManagerRepository = new TransactionManagerRepository();

        // Lấy thông tin từ yêu cầu
        String currentPage = req.getParameter("current");
        String pageSize = req.getParameter("size");
        String pageRange = req.getParameter("range");
        String startDate = req.getParameter("f_start");
        String endDate = req.getParameter("f_end");

        try {
            if (StringValidator.isNullOrBlank(currentPage)
                    || StringValidator.isNullOrBlank(pageSize)
                    || StringValidator.isNullOrBlank(pageRange)) {
                currentPage = "1";
                pageSize = "5";
                pageRange = "5";
            }

            LocalDateTime startDateConvert = null;
            if (!StringValidator.isNullOrBlank(startDate)){
                startDateConvert = DateTimeConvertor.toLocalDateTime(startDate);
            }

            LocalDateTime endDateConvert = null;
            if (!StringValidator.isNullOrBlank(endDate)){
                endDateConvert = DateTimeConvertor.toLocalDateTime(endDate);
            }


            long userCount = transactionManagerRepository.countAll( startDateConvert, endDateConvert);
            Pagination pagination
                    = new Pagination(userCount, Integer.parseInt(currentPage), Integer.parseInt(pageRange), Integer.parseInt(pageSize));

            int startPage = (pagination.getCurrentPage() - 1) * pagination.getPageSize();
            int endPage = pagination.getPageSize();
            List<UserEntity> externalTransactions = transactionManagerRepository
                    .getExternalTransactionsWithPaging(startPage, endPage, startDateConvert, endDateConvert);

            req.setAttribute("FILTER_STARTDATE", startDate);
            req.setAttribute("FILTER_ENDDATE", endDate);
            req.setAttribute("VIEW_PAGING", new ViewPaging<>(externalTransactions, pagination));

            req.getRequestDispatcher("/pages/Transactions/ExternalTransactions.jsp").forward(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
            Pagination pagination
                    = new Pagination(0, 1, 5, 10);
            req.setAttribute("VIEW_PAGING", new ViewPaging<UserEntity>(new ArrayList<>(), pagination));
        }

    }
    }

