package Controllers;

import DataAccess.TransactionManagerRepository;
import Models.Common.Pagination;
import Models.Common.ViewPaging;
import Models.ExternalTransactionEntity;
import Models.UserEntity;
import Utils.Annotations.Authorization;
import Utils.Validation.StringValidator;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "ExternalTransactionController", urlPatterns = {"/externalTransaction"})
@Authorization(role = "", isPublic = true)
public class ExternalTransactionController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        TransactionManagerRepository transactionManagerRepository = new TransactionManagerRepository();

        // Lấy thông tin từ yêu cầu
        String currentPage = req.getParameter("current");
        String pageSize = req.getParameter("size");
        String pageRange = req.getParameter("range");

        try {
            if (StringValidator.isNullOrBlank(currentPage)
                    || StringValidator.isNullOrBlank(pageSize)
                    || StringValidator.isNullOrBlank(pageRange)) {
                currentPage = "1";
                pageSize = "5";
                pageRange = "5";
            }





            long userCount = transactionManagerRepository.countAll();
            Pagination pagination
                    = new Pagination(userCount, Integer.parseInt(currentPage), Integer.parseInt(pageRange), Integer.parseInt(pageSize));

            int startPage = (pagination.getCurrentPage() - 1) * pagination.getPageSize();
            int endPage = pagination.getPageSize();
            List<ExternalTransactionEntity> externalTransactions = transactionManagerRepository
                    .getExternalTransactionsWithPaging(startPage, endPage);

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

