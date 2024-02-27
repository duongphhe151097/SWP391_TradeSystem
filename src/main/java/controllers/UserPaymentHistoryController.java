package controllers;

import dataAccess.TransactionManagerRepository;
import models.common.Pagination;
import models.common.ViewPaging;
import models.ExternalTransactionEntity;
import models.UserEntity;
import utils.annotations.Authorization;
import utils.constants.UserConstant;
import utils.validation.StringValidator;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@WebServlet(name = "UserPaymentHistoryController", urlPatterns = {"/payment/userhistory"})
@Authorization(role = "", isPublic = false)
public class UserPaymentHistoryController extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        HttpSession session = req.getSession(false);
//        if (session != null) {
//            UUID userId = (UUID) session.getAttribute(UserConstant.SESSION_USERID);
//
//            // Lấy thông tin từ request
//            String currentPage = req.getParameter("current");
//            String pageSize = req.getParameter("size");
//            String pageRange = req.getParameter("range");
//            String id = req.getParameter("id");
//            String type = req.getParameter("type");
//            String command = req.getParameter("command");
//            String amount = req.getParameter("amount");
//            String status = req.getParameter("status");
//            String createAt = req.getParameter("createAt");
//            String createBy = req.getParameter("createBy");
//            String updateAt = req.getParameter("updateAt");
//            String updateBy = req.getParameter("updateBy");
//            String startDate = req.getParameter("startDate");
//            String endDate = req.getParameter("endDate");
//
//            try {
//                if (StringValidator.isNullOrBlank(currentPage)
//                        || StringValidator.isNullOrBlank(pageSize)
//                        || StringValidator.isNullOrBlank(pageRange)) {
//                    currentPage = "1";
//                    pageSize = "5";
//                    pageRange = "5";
//                }
//
////                // Xử lý giá trị mặc định và chuyển đổi kiểu dữ liệu
//                if (StringValidator.isNullOrBlank(id)) {
//                    id = "";
//                }
//
////                if (!StringValidator.isValidUserStatus(status)) {
////                    status = "ALL";
////                }
//
//                // Chuyển đổi các tham số thời gian sang LocalDateTime
//
//                LocalDateTime createAtConvert = DateTimeConvertor.toLocalDateTime(createAt);
//                LocalDateTime createByConvert = DateTimeConvertor.toLocalDateTime(createBy);
//                LocalDateTime updateAtConvert = DateTimeConvertor.toLocalDateTime(updateAt);
//                LocalDateTime updateByConvert = DateTimeConvertor.toLocalDateTime(updateBy);
//                LocalDateTime startDateConvert = DateTimeConvertor.toLocalDateTime(startDate);
//                LocalDateTime endDateConvert = DateTimeConvertor.toLocalDateTime(endDate);
//
//                // Khởi tạo repository và lấy dữ liệu giao dịch ngoại tuyến của người dùng
//                TransactionManagerRepository transactionManagerRepository = new TransactionManagerRepository();
//                long transactionCount = transactionManagerRepository.countAllWithSearch(
//                        UUID.fromString(id),
//                        type,
//                        BigInteger.valueOf(Long.parseLong(amount))
//                );
//
//                // Tạo đối tượng phân trang và lấy dữ liệu từ repository
//                Pagination pagination = new Pagination(
//                        transactionCount,
//                        Integer.parseInt(currentPage),
//                        Integer.parseInt(pageRange),
//                        Integer.parseInt(pageSize)
//                );
//                int startPage = (pagination.getCurrentPage() - 1) * pagination.getPageSize();
//                int endPage = pagination.getPageSize();
////                List<UserEntity> transactions = transactionManagerRepository.getAllWithPagingAndFilter(
////                        startPage,
////                        endPage,
////                        userId,
////                        UUID.fromString(id)
//////                        ,
//////                        type,
//////                        new BigInteger(amount),
//////                        createAtConvert,
//////                        createByConvert,
//////                        updateAtConvert,
//////                        updateByConvert,
//////                        startDateConvert,
//////                        endDateConvert
////                );
//                List<ExternalTransactionEntity> externalTransactions;
//                if (amount != null && !amount.isEmpty()) {
//                    externalTransactions  = transactionManagerRepository
//                            .getUserExternalTransactionsWithPaging(startPage, endPage, UUID.fromString(userId));
//                } else {
//                    externalTransactions = transactionManagerRepository
//                            .getExternalTransactionsWithPaging(startPage, endPage);
//                }
//
//
//                // Đặt các thuộc tính cho request và chuyển hướng tới trang JSP
////                req.setAttribute("id_SEARCH", id);
//////                req.setAttribute("status_SEARCH", status);
////                req.setAttribute("type_SEARCH", type);
////                req.setAttribute("command_SEARCH", command);
////                req.setAttribute("amount_SEARCH", amount);
////                req.setAttribute("status_SEARCH", status);
////                req.setAttribute("createAt_SEARCH", createAt);
////                req.setAttribute("createBy_SEARCH", createBy);
////                req.setAttribute("updateAt_SEARCH", updateAt);
////                req.setAttribute("updateBy_SEARCH", updateBy);
////                req.setAttribute("FILTER_STARTDATE", startDate);
////                req.setAttribute("FILTER_ENDDATE", endDate);
//                req.setAttribute("VIEW_PAGING", new ViewPaging<>(transactions, pagination));
//                req.getRequestDispatcher("/pages/Transactions/user_payment_history.jsp").forward(req, resp);
//
//            } catch (Exception e) {
//                e.printStackTrace();
//                // Xử lý ngoại lệ và trả về trang không có dữ liệu
//                Pagination pagination = new Pagination(0, 1, 5, 10);
//                req.setAttribute("VIEW_PAGING", new ViewPaging<>(new ArrayList<>(), pagination));
//                req.getRequestDispatcher("/pages/Transactions/user_payment_history.jsp").forward(req, resp);
//            }
//        } else {
//            // Xử lý khi session không tồn tại
//            resp.sendRedirect(req.getContextPath() + "/login");

        HttpSession session = req.getSession(false);
        if (session != null) {
            UUID userId = (UUID) session.getAttribute(UserConstant.SESSION_USERID);
            String sessionId = session.getId();
            // Perform any additional cleanup actions if needed
            TransactionManagerRepository transactionManagerRepository = new TransactionManagerRepository();

            transactionManagerRepository.getExternalTransactionByUser(userId);
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





                long userCount = transactionManagerRepository.countAllByUser(userId);
                Pagination pagination
                        = new Pagination(userCount, Integer.parseInt(currentPage), Integer.parseInt(pageRange), Integer.parseInt(pageSize));

                int startPage = (pagination.getCurrentPage() - 1) * pagination.getPageSize();
                int endPage = pagination.getPageSize();
                List<ExternalTransactionEntity> externalTransactions = transactionManagerRepository
                        .getUserExternalTransactionsWithPaging(startPage, endPage, userId);

                req.setAttribute("VIEW_PAGING", new ViewPaging<>(externalTransactions, pagination));

                req.getRequestDispatcher("/pages/transactions/user_payment_history.jsp").forward(req, resp);
            } catch (Exception e) {
                e.printStackTrace();
                Pagination pagination
                        = new Pagination(0, 1, 5, 10);
                req.setAttribute("VIEW_PAGING", new ViewPaging<UserEntity>(new ArrayList<>(), pagination));
            }

        }

        }
    }



