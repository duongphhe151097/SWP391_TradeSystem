package controllers;

import dataAccess.TransactionManagerRepository;
import models.common.Pagination;
import models.common.ViewPaging;
import models.UserEntity;
import utils.annotations.Authorization;
import utils.convert.DateTimeConvertor;
import utils.validation.StringValidator;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@WebServlet(name = "PaymentHistoryController", urlPatterns = {"/admin/payment/history"})
@Authorization(role = "ADMIN", isPublic = false)
public class PaymentHistoryController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        TransactionManagerRepository transactionManagerRepository = new TransactionManagerRepository();

        // Lấy thông tin từ yêu cầu
        String currentPage = req.getParameter("current");
        String pageSize = req.getParameter("size");
        String pageRange = req.getParameter("range");
        String amountFrom = req.getParameter("f_amountFrom");
        String amountTo = req.getParameter("f_amountTo");
        String startDate = req.getParameter("f_start");
        String endDate = req.getParameter("f_end");
        String id = req.getParameter("id");
        String createBy = req.getParameter("user");

        try {
            if (StringValidator.isNullOrBlank(currentPage)
                    || StringValidator.isNullOrBlank(pageSize)
                    || StringValidator.isNullOrBlank(pageRange)) {
                currentPage = "1";
                pageSize = "10";
                pageRange = "5";
            }

            BigInteger amountFromValue = null;
            BigInteger amountToValue = null;


            if (amountFrom != null && !amountFrom.isEmpty()) {
                amountFromValue = new BigInteger(amountFrom);
            }

            if (amountTo != null && !amountTo.isEmpty()) {
                amountToValue = new BigInteger(amountTo);
            }

            UUID f_id = null;
            if (!StringValidator.isNullOrBlank(id)) {
                f_id = UUID.fromString(id);
            }

            if (StringValidator.isNullOrBlank(createBy)) {
                createBy = "";
            }

            LocalDateTime startDateConvert = null;
            if (!StringValidator.isNullOrBlank(startDate)){
                startDateConvert = DateTimeConvertor.toLocalDateTime(startDate);
            }

            LocalDateTime endDateConvert = null;
            if (!StringValidator.isNullOrBlank(endDate)){
                endDateConvert = DateTimeConvertor.toLocalDateTime(endDate);
            }


            long userCount = transactionManagerRepository.countAll(startDateConvert, endDateConvert, amountFromValue, amountToValue, createBy, f_id);
            Pagination pagination
                    = new Pagination(userCount, Integer.parseInt(currentPage), Integer.parseInt(pageRange), Integer.parseInt(pageSize));

            int startPage = (pagination.getCurrentPage() - 1) * pagination.getPageSize();
            int endPage = pagination.getPageSize();
            List<UserEntity> externalTransactions = transactionManagerRepository
                    .getExternalTransactionsWithPaging(startPage, endPage, amountFromValue,amountToValue,f_id, createBy, startDateConvert, endDateConvert);

            req.setAttribute("FILTER_AmountFrom", amountFrom);
            req.setAttribute("FILTER_AmountTo", amountTo);
            req.setAttribute("FILTER_STARTDATE", startDate);
            req.setAttribute("FILTER_ENDDATE", endDate);
            req.setAttribute("FILTER_ID", id);
            req.setAttribute("FILTER_USER", createBy);

            req.setAttribute("VIEW_PAGING", new ViewPaging<>(externalTransactions, pagination));

            req.getRequestDispatcher("/pages/admin/admin_payment_history.jsp").forward(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
            Pagination pagination
                    = new Pagination(0, 1, 5, 10);
            req.setAttribute("VIEW_PAGING", new ViewPaging<UserEntity>(new ArrayList<>(), pagination));
        }

    }
    }





