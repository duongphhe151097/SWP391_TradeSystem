package controllers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dataAccess.InternalTransactionRepository;
import dataAccess.OrderRepository;
import dataAccess.ProductRepository;
import dataAccess.UserReportRepository;
import dtos.TransactionQueueDto;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.InternalTransactionEntity;
import models.OrderEntity;
import models.ProductEntity;
import models.UserReportEntity;
import utils.annotations.Authorization;
import utils.constants.OrderConstant;
import utils.constants.ReportConstant;
import utils.constants.TransactionConstant;
import utils.validation.StringValidator;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Optional;
import java.util.Queue;
import java.util.UUID;

@WebServlet(name = "AdminReportDetailController", urlPatterns = "/admin/report/detail")
@Authorization(role = "ADMIN", isPublic = false)
public class AdminReportDetailController extends BaseController {
    private UserReportRepository userReportRepository;
    private InternalTransactionRepository internalTransactionRepository;
    private OrderRepository orderRepository;
    private ProductRepository productRepository;
    private Gson gson;
    private Queue<TransactionQueueDto> transactionQueue;

    @Override
    public void init() throws ServletException {
        this.userReportRepository = new UserReportRepository();
        this.internalTransactionRepository = new InternalTransactionRepository();
        this.orderRepository = new OrderRepository();
        this.productRepository = new ProductRepository();
        this.gson = new Gson();
        ServletContext context = getServletContext();
        this.transactionQueue = (Queue<TransactionQueueDto>) context
                .getAttribute("transaction_queue");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher dispatcher = req.getRequestDispatcher("/pages/admin/admin_report-detail.jsp");
        try {

            String reqId = req.getParameter("id");

            if (StringValidator.isNullOrBlank(reqId) || !StringValidator.isUUID(reqId)) {
                req.setAttribute("ERROR_MESSAGE", "Id báo cáo không hợp lệ!");
                dispatcher.forward(req, resp);
                return;
            }

            UUID id = UUID.fromString(reqId);
            Optional<UserReportEntity> optionalUserReport = userReportRepository
                    .getReportById(id);

            if (optionalUserReport.isEmpty()) {
                req.setAttribute("ERROR_MESSAGE", "Không tìm thấy báo cáo!");
                dispatcher.forward(req, resp);
                return;
            }

            req.setAttribute("VAR_DATA", optionalUserReport.get());
        } catch (Exception e) {
            req.setAttribute("ERROR_MESSAGE", "Xảy ra lỗi khi load dữ liệu");
        }
        dispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        JsonObject jsonObject = new JsonObject();

        String reqId = req.getParameter("id");
        String reqType = req.getParameter("type");
        String reqAdminResponse = req.getParameter("adm_res");
        String reqIsRightReport = req.getParameter("right_report");

        try {
            if (StringValidator.isNullOrBlank(reqId) || !StringValidator.isUUID(reqId) || StringValidator.isNullOrBlank(reqType)) {
                resp.setStatus(400);
                jsonObject.addProperty("code", 400);
                jsonObject.addProperty("message", "Tham số không hợp lệ!");
                resp.getWriter().write(gson.toJson(jsonObject));
                return;
            }

            UUID id = UUID.fromString(reqId);
            Optional<UserReportEntity> optionalUserReport = userReportRepository
                    .getReportById(id);

            if (optionalUserReport.isEmpty()) {
                resp.setStatus(400);
                jsonObject.addProperty("code", 400);
                jsonObject.addProperty("message", "Không tìm thấy báo cáo!");
                resp.getWriter().write(gson.toJson(jsonObject));
                return;
            }

            UserReportEntity userReportEntity = optionalUserReport.get();
            UUID productId = userReportEntity.getProductTarget();
            Optional<OrderEntity> optionalOrderEntity = orderRepository
                    .getOrderByUserId(userReportEntity.getUserId(), productId);

            if (optionalOrderEntity.isEmpty()) {
                resp.setStatus(400);
                jsonObject.addProperty("code", 400);
                jsonObject.addProperty("message", "Không tìm thấy giao dịch!");
                resp.getWriter().write(gson.toJson(jsonObject));
                return;
            }
            OrderEntity order = optionalOrderEntity.get();

            Optional<ProductEntity> optionalProductEntity = productRepository
                    .getProductById(productId);
            if (optionalProductEntity.isEmpty()) {
                resp.setStatus(400);
                jsonObject.addProperty("code", 400);
                jsonObject.addProperty("message", "Không tìm thấy đơn trung gian!");
                resp.getWriter().write(gson.toJson(jsonObject));
                return;
            }

            switch (reqType) {
                case "PROCESSING":
                    userReportEntity.setStatus(ReportConstant.REPORT_ADMIN_CHECKING);
                    break;

                case "PROCESSED":
                    if (StringValidator.isNullOrBlank(reqAdminResponse)) {
                        resp.setStatus(400);
                        jsonObject.addProperty("code", 400);
                        jsonObject.addProperty("message", "Không được để trống phản hồi");
                        resp.getWriter().write(gson.toJson(jsonObject));
                        return;
                    }

                    userReportEntity.setAdminResponse(reqAdminResponse.replace("&nbsp;", ""));

                    if (!StringValidator.isNullOrBlank(reqIsRightReport) && reqIsRightReport.equals("checked")) {
                        //Người mua report sai
                        userReportEntity.setStatus(ReportConstant.REPORT_ADMIN_RESPONSE_BUYER_WRONG);

                        //Tạo giao dịch trả tiền cho người bán
                        InternalTransactionEntity cancelOrder = InternalTransactionEntity.builder()
                                .id(UUID.randomUUID())
                                .to(userReportEntity.getUserTarget())
                                .amount(order.getAmount())
                                .description("Trả tiền đơn trung gian thành công!")
                                .status(TransactionConstant.INTERNAL_ADD)
                                .build();

                        //Đưa order về trạng thái thành công
                        order.setStatus(OrderConstant.ORDER_SUCCESS);
                        orderRepository.update(order);

                        internalTransactionRepository.add(cancelOrder);
                        transactionQueue.add(new TransactionQueueDto(userReportEntity.getUserTarget(), "ADD_AM", order.getAmount()));
                    } else {
                        //Người mua report đúng
                        userReportEntity.setStatus(ReportConstant.REPORT_ADMIN_RESPONSE_BUYER_RIGHT);

                        //Trả tiền cọc khiếu nại
                        BigInteger refundReportFeeAmount = BigInteger.valueOf(50000);
                        InternalTransactionEntity refundReportFee = InternalTransactionEntity.builder()
                                .id(UUID.randomUUID())
                                .to(userReportEntity.getUserId())
                                .amount(refundReportFeeAmount)
                                .description("Trả lại tiền báo cáo thành công!")
                                .status(TransactionConstant.INTERNAL_ADD)
                                .build();
                        internalTransactionRepository.add(refundReportFee);

                        //Trả lại tiền mua hàng
                        BigInteger refundOrderAmount = order.getAmount().add(order.getFee());
                        InternalTransactionEntity refundOrder = InternalTransactionEntity.builder()
                                .id(UUID.randomUUID())
                                .to(userReportEntity.getUserId())
                                .amount(refundOrderAmount)
                                .description("Trả lại tiền mua hàng!")
                                .status(TransactionConstant.INTERNAL_ADD)
                                .build();
                        internalTransactionRepository.add(refundOrder);

                        order.setStatus(OrderConstant.ORDER_ABORT);
                        orderRepository.update(order);

                        //Đưa vào queue để cộng tiền
                        transactionQueue.add(new TransactionQueueDto(userReportEntity.getUserId(), "ADD_AM", refundReportFeeAmount));
                        transactionQueue.add(new TransactionQueueDto(userReportEntity.getUserId(), "ADD_AM", refundOrderAmount));
                    }
                    break;
            }

            boolean isSuccess = userReportRepository.update(userReportEntity);
            if (isSuccess) {
                resp.setStatus(200);
                jsonObject.addProperty("code", 200);
                jsonObject.addProperty("message", "Cập nhật thành công!");
                resp.getWriter().write(gson.toJson(jsonObject));
                return;
            }

            resp.setStatus(409);
            jsonObject.addProperty("code", 409);
            jsonObject.addProperty("message", "Cập nhật không thành công!");
            resp.getWriter().write(gson.toJson(jsonObject));
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(500);
            jsonObject.addProperty("code", 500);
            jsonObject.addProperty("message", "Lỗi server!");
            resp.getWriter().write(gson.toJson(jsonObject));
        }
    }
}
