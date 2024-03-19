package controllers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
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
import utils.annotations.Authorization;
import utils.constants.ReportConstant;
import utils.constants.TransactionConstant;
import utils.constants.UserConstant;
import utils.validation.StringValidator;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Optional;
import java.util.Queue;
import java.util.UUID;

@WebServlet(name = "UserReportDetailController", urlPatterns = "/report/detail")
@Authorization(role = "USER", isPublic = false)
public class UserReportDetailController extends BaseController {
    private UserReportRepository userReportRepository;
    private UserRepository userRepository;
    private InternalTransactionRepository internalTransactionRepository;
    private Gson gson;

    @Override
    public void init() throws ServletException {
        this.userReportRepository = new UserReportRepository();
        this.userRepository = new UserRepository();
        this.internalTransactionRepository = new InternalTransactionRepository();
        this.gson = new Gson();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher dispatcher = req.getRequestDispatcher("/pages/report/user_report-detail.jsp");

        String rid = req.getParameter("id");

        try {
            if (StringValidator.isNullOrBlank(rid) || !StringValidator.isUUID(rid)) {
                req.setAttribute("ERROR_MESSAGE", "Id không hợp lệ!");
                dispatcher.forward(req, resp);
            }

            UUID id = UUID.fromString(rid);
            Optional<UserReportEntity> optionalUserReport = userReportRepository
                    .getReportById(id);

            if (optionalUserReport.isEmpty()) {
                req.setAttribute("ERROR_MESSAGE", "Không tìm thấy báo cáo!");
                dispatcher.forward(req, resp);
                return;
            }

            UserReportEntity userReportEntity = optionalUserReport.get();
            req.setAttribute("VAR_REPORT_DETAIL", userReportEntity);
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("ERROR_MESSAGE", "Không tìm thấy báo cáo!");
        }

        dispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");

        String reqId = req.getParameter("id");
        String type = req.getParameter("type");
        String sellerResp = req.getParameter("seller_resp");
        try {
            HttpSession httpSession = req.getSession(false);
            UUID userId = (UUID) httpSession.getAttribute(UserConstant.SESSION_USERID);

            if (StringValidator.isNullOrBlank(reqId) || StringValidator.isNullOrBlank(type) || !StringValidator.isUUID(reqId)) {
                returnResult(resp, 400, "Tham số không hợp lệ!");
                return;
            }

            UUID rid = UUID.fromString(reqId);
            Optional<UserReportEntity> optionalUserReport = userReportRepository
                    .getReportById(rid);

            if (optionalUserReport.isEmpty()) {
                returnResult(resp, 400, "Không tìm thấy báo cáo!");
                return;
            }

            UserReportEntity userReportEntity = optionalUserReport.get();
            switch (type) {
                case "ABORTED":
                    userReportEntity.setStatus(ReportConstant.REPORT_BUYER_ABORT);
                    break;

                case "DENIED":
                    userReportEntity.setStatus(ReportConstant.REPORT_SELLER_DENIED);
                    break;

                case "REQUEST_ADMIN":
                    Optional<UserEntity> userEntity = userRepository
                            .getUserById(userId);

                    if(userEntity.isEmpty()){
                        returnResult(resp, 400, "Không tìm thấy người dùng!");
                        return;
                    }

                    BigInteger balance = userEntity.get().getBalance();
                    long fee = 50000;
                    BigInteger bigIntFee = BigInteger.valueOf(fee);

                    if(balance.compareTo(bigIntFee) < 0){
                        returnResult(resp, 400, "NOT_ENOUGH_MONEY");
                        return;
                    }

                    ServletContext context = getServletContext();
                    Queue<TransactionQueueDto> transactionQueue = (Queue<TransactionQueueDto>) context
                            .getAttribute("transaction_queue");

                    InternalTransactionEntity internalTransaction = InternalTransactionEntity.builder()
                            .id(UUID.randomUUID())
                            .from(userId)
                            .amount(bigIntFee)
                            .description("Thu tiền yêu cầu admin xử lý đơn trung gian!")
                            .status(TransactionConstant.INTERNAL_SUB)
                            .build();

                    internalTransactionRepository.add(internalTransaction);
                    transactionQueue.add(new TransactionQueueDto(userId, "SUB_AM", bigIntFee));

                    userReportEntity.setStatus(ReportConstant.REPORT_ADMIN_REQUEST);
                    break;

                case "ACCEPTED":
                    userReportEntity.setStatus(ReportConstant.REPORT_SELLER_ACCEPT);
                    break;

                case "BUYER_ACCEPT_SELLER":
                    if(StringValidator.isNullOrBlank(sellerResp)){
                        returnResult(resp, 400, "Phản hồi không được để trống!");
                        return;
                    }

                    userReportEntity.setSellerResponse(sellerResp.replace("&nbsp;", ""));
                    userReportEntity.setStatus(ReportConstant.REPORT_BUYER_ACCEPT_SELLER_RESPONSE);
                    break;

                default:
                    returnResult(resp, 400, "Tham số không hợp lệ!");
                    return;
            }

            boolean isSuccess = userReportRepository.update(userReportEntity);
            if (isSuccess) {
                returnResult(resp, 200, "Cập nhật thành công!");
                return;
            }

            returnResult(resp, 409, "Cập nhật không thành công!");
        } catch (Exception e) {
            e.printStackTrace();
            returnResult(resp, 500, "Lỗi server!");
        }
    }

    private void returnResult(HttpServletResponse resp, int code, String message) throws IOException {
        resp.setStatus(code);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("code", code);
        jsonObject.addProperty("message", message);
        resp.getWriter().write(gson.toJson(jsonObject));
    }
}
