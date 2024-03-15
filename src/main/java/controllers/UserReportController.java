package controllers;

import dataAccess.InternalTransactionRepository;
import dataAccess.UserReportRepository;
import dataAccess.UserRepository;
import dtos.TransactionQueueDto;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.InternalTransactionEntity;
import models.UserEntity;
import models.UserReportEntity;
import models.common.Pagination;
import models.common.ViewPaging;
import utils.annotations.Authorization;
import utils.constants.ReportConstant;
import utils.constants.TransactionConstant;
import utils.constants.UserConstant;
import utils.convert.DateTimeConvertor;
import utils.validation.StringValidator;

import java.io.IOException;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.*;

@WebServlet(name = "UserReportController", urlPatterns = "/report")
@Authorization(role = "USER", isPublic = false)
public class UserReportController extends BaseController {
    private UserReportRepository userReportRepository;
    private UserRepository userRepository;
    private InternalTransactionRepository internalTransactionRepository;


    @Override
    public void init() throws ServletException {
        this.userReportRepository = new UserReportRepository();
        this.userRepository = new UserRepository();
        this.internalTransactionRepository = new InternalTransactionRepository();
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
                    .getReportByUserIdWithPaging(userId, startPage, endPage, title, startDateConvert, endDateConvert, status);

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
        String pid = req.getParameter("pid");
        String reportContent = req.getParameter("r_content");
        String reportTitle = req.getParameter("r_title");

        try {
            HttpSession httpSession = req.getSession(false);
            UUID userId = (UUID) httpSession.getAttribute(UserConstant.SESSION_USERID);

            if(StringValidator.isNullOrBlank(pid) || !StringValidator.isUUID(pid)){
                req.setAttribute("ERROR_MESSAGE", "Mã sản phẩm không hợp lệ!");
                return;
            }

            UUID productId = UUID.fromString(pid);

            Optional<UserEntity> userEntity = userRepository
                    .getUserById(userId);

            if(userEntity.isEmpty()){
                req.setAttribute("ERROR_MESSAGE", "Không tìm thấy người dùng!");
                return;
            }

            BigInteger balance = userEntity.get().getBalance();
            long fee = 50000;
            BigInteger bigIntFee = BigInteger.valueOf(fee);

            if(balance.compareTo(bigIntFee) < 0){
                req.setAttribute("ERROR_MESSAGE", "Bạn không đủ tiền để tạo khiếu nại!");
                return;
            }

            ServletContext context = getServletContext();
            Queue<TransactionQueueDto> transactionQueue = (Queue<TransactionQueueDto>) context
                    .getAttribute("transaction_queue");

            InternalTransactionEntity internalTransaction = InternalTransactionEntity.builder()
                    .id(UUID.randomUUID())
                    .from(userId)
                    .amount(bigIntFee)
                    .description("Thu tiền tạo báo cáo!")
                    .status(TransactionConstant.INTERNAL_SUB)
                    .build();
            internalTransactionRepository.add(internalTransaction);

            // Đưa vào queue để trừ tiền
            transactionQueue.add(new TransactionQueueDto(userId, "SUB_AM", bigIntFee));
            reportContent = reportContent.replace("&nbsp;", "");
            UserReportEntity userReportEntity = UserReportEntity.builder()
                    .id(UUID.randomUUID())
                    .userId(userId)
                    .type(ReportConstant.USER_REPORT)
                    .title(reportTitle)
                    .description(reportContent)
                    .status(ReportConstant.REPORT_CREATED)
                    .productTarget(productId)
                    .build();
            userReportRepository.add(userReportEntity);

            //TODO: Thêm chuyển hướng về trang /report
            //TODO: Nếu chưa thành công ở bất kì giai đoạn nào chuyển hướng về trang tạo report hiện tại
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
