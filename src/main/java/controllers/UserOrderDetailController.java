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
import jakarta.servlet.http.HttpSession;
import models.InternalTransactionEntity;
import models.OrderEntity;
import models.ProductEntity;
import models.UserReportEntity;
import utils.annotations.Authorization;
import utils.constants.OrderConstant;
import utils.constants.TransactionConstant;
import utils.constants.UserConstant;
import utils.validation.StringValidator;

import java.io.IOException;
import java.util.Optional;
import java.util.Queue;
import java.util.UUID;

@WebServlet(name = "UserOrderDetailController", urlPatterns = {"/user/order/detail", "/order/detail"})
@Authorization(role = "USER", isPublic = false)
public class UserOrderDetailController extends BaseController {
    private OrderRepository orderRepository;
    private UserReportRepository userReportRepository;
    private InternalTransactionRepository internalTransactionRepository;
    private ProductRepository productRepository;
    private Gson gson;

    @Override
    public void init() throws ServletException {
        this.orderRepository = new OrderRepository();
        this.userReportRepository = new UserReportRepository();
        this.internalTransactionRepository = new InternalTransactionRepository();
        this.productRepository = new ProductRepository();
        this.gson = new Gson();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher dispatcher = req.getRequestDispatcher("/pages/order/user_order-detail.jsp");

        String pid = req.getParameter("id");

        try {
            if (StringValidator.isNullOrBlank(pid) || !StringValidator.isUUID(pid)) {
                req.setAttribute("ERROR_MESSAGE", "Mã đơn trung gian không hợp lệ!");
                dispatcher.forward(req, resp);
                return;
            }
            UUID productId = UUID.fromString(pid);

            HttpSession httpSession = req.getSession(false);
            UUID userId = (UUID) httpSession.getAttribute(UserConstant.SESSION_USERID);

            Optional<OrderEntity> optionalOrderEntity = orderRepository
                    .getOrderByUserId(userId, productId);

            Optional<UserReportEntity> optionalUserReport = userReportRepository
                    .getReportByProductId(productId);

            if (optionalOrderEntity.isEmpty()) {
                req.setAttribute("ERROR_MESSAGE", "Không tìm thấy đơn trung gian!");
                dispatcher.forward(req, resp);
                return;
            }

            OrderEntity order = optionalOrderEntity.get();
            req.setAttribute("VAR_ORDER", order);
            req.setAttribute("VAR_REPORT", optionalUserReport.get());
            dispatcher.forward(req, resp);
        } catch (Exception e) {
            req.setAttribute("ERROR_MESSAGE", "Không tìm thấy đơn trung gian!");
            dispatcher.forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");

        String oId = req.getParameter("id");
        String type = req.getParameter("type");

        JsonObject jsonObject = new JsonObject();
        try {
            if (StringValidator.isNullOrBlank(oId) || StringValidator.isNullOrBlank(type)) {
                resp.setStatus(400);
                jsonObject.addProperty("code", 400);
                jsonObject.addProperty("message", "Tham số không hợp lệ!");
                printJson(resp, gson.toJson(jsonObject));
                return;
            }

            if (!StringValidator.isUUID(oId)) {
                resp.setStatus(400);
                jsonObject.addProperty("code", 400);
                jsonObject.addProperty("message", "Mã đơn mua không hợp lệ!");
                printJson(resp, gson.toJson(jsonObject));
                return;
            }

            if (!type.equals("CONFIRM")) {
                resp.setStatus(400);
                jsonObject.addProperty("code", 400);
                jsonObject.addProperty("message", "Hành động không hợp lệ!");
                printJson(resp, gson.toJson(jsonObject));
                return;
            }

            UUID orderId = UUID.fromString(oId);

            Optional<OrderEntity> optionalOrderEntity = orderRepository
                    .getOrderById(orderId);

            if (optionalOrderEntity.isEmpty()) {
                resp.setStatus(400);
                jsonObject.addProperty("code", 400);
                jsonObject.addProperty("message", "Không tìm thấy đơn mua!");
                printJson(resp, gson.toJson(jsonObject));
                return;
            }

            OrderEntity order = optionalOrderEntity.get();
            order.setStatus(OrderConstant.ORDER_SUCCESS);
            orderRepository.update(order);

            Optional<ProductEntity> optionalProductEntity = productRepository.getProductById(order.getProductId());
            if (optionalProductEntity.isEmpty()) {
                resp.setStatus(400);
                jsonObject.addProperty("code", 400);
                jsonObject.addProperty("message", "Đơn trung gian không tồn tại!");
                printJson(resp, gson.toJson(jsonObject));
                return;
            }
            ProductEntity product = optionalProductEntity.get();

            ServletContext context = getServletContext();
            Queue<TransactionQueueDto> transactionQueue = (Queue<TransactionQueueDto>) context
                    .getAttribute("transaction_queue");

            InternalTransactionEntity internalTransaction = InternalTransactionEntity.builder()
                    .id(UUID.randomUUID())
                    .to(product.getUserId())
                    .amount(product.getPrice())
                    .description("Trả tiền đơn trung gian thành công!")
                    .status(TransactionConstant.INTERNAL_ADD)
                    .build();

            internalTransactionRepository.add(internalTransaction);
            transactionQueue.add(new TransactionQueueDto(product.getUserId(), "ADD_AM", product.getPrice()));

            resp.setStatus(200);
            jsonObject.addProperty("code", 200);
            jsonObject.addProperty("message", "Cập nhật thành công!");
            printJson(resp, gson.toJson(jsonObject));
        } catch (Exception e) {
            resp.setStatus(500);
            jsonObject.addProperty("code", 500);
            jsonObject.addProperty("message", "Lỗi server!");
            printJson(resp, gson.toJson(jsonObject));
        }
    }
}
