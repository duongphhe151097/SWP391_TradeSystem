package controllers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dataAccess.InternalTransactionRepository;
import dataAccess.OrderRepository;
import dataAccess.ProductRepository;
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
import models.OrderEntity;
import models.ProductEntity;
import models.UserEntity;
import models.common.Pagination;
import models.common.ViewPaging;
import utils.annotations.Authorization;
import utils.constants.OrderConstant;
import utils.constants.ProductConstant;
import utils.constants.TransactionConstant;
import utils.constants.UserConstant;
import utils.convert.DateTimeConvertor;
import utils.validation.StringValidator;

import java.io.IOException;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.*;

@WebServlet(name = "UserOrderController", urlPatterns = "/order")
@Authorization(role = "USER", isPublic = false)
public class UserOrderController extends BaseController {
    private OrderRepository orderRepository;
    private UserRepository userRepository;
    private ProductRepository productRepository;
    private InternalTransactionRepository internalTransactionRepository;
    private Gson gson;

    @Override
    public void init() throws ServletException {
        this.orderRepository = new OrderRepository();
        this.userRepository = new UserRepository();
        this.productRepository = new ProductRepository();
        this.internalTransactionRepository = new InternalTransactionRepository();
        this.gson = new Gson();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher dispatcher = req.getRequestDispatcher("/pages/order/user_order.jsp");

        String currentPage = req.getParameter("current");
        String pageSize = req.getParameter("size");
        String pageRange = req.getParameter("range");

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

            long orderCount = orderRepository.countOrderByUserId(userId, startDateConvert, endDateConvert, status);

            Pagination pagination
                    = new Pagination(orderCount, Integer.parseInt(currentPage), Integer.parseInt(pageRange), Integer.parseInt(pageSize));

            int startPage = (pagination.getCurrentPage() - 1) * pagination.getPageSize();
            int endPage = pagination.getPageSize();

            List<OrderEntity> orderEntityList = orderRepository
                    .getOrderByUserIdWithFilter(userId, startPage, endPage, startDateConvert, endDateConvert, status);

            req.setAttribute("FILTER_STATUS", filterStatus);
            req.setAttribute("FILTER_STARTDATE", startDate);
            req.setAttribute("FILTER_ENDDATE", endDate);
            req.setAttribute("VIEW_PAGING", new ViewPaging<>(orderEntityList, pagination));
        } catch (Exception e) {
            e.printStackTrace();
            Pagination pagination
                    = new Pagination(0, 1, 5, 10);
            req.setAttribute("VIEW_PAGING", new ViewPaging<UserEntity>(new ArrayList<>(), pagination));
        }

        dispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        JsonObject jsonObject = new JsonObject();

        String pId = req.getParameter("pid");
        try {
            if (StringValidator.isNullOrBlank(pId) || !StringValidator.isUUID(pId)) {
                resp.setStatus(400);
                jsonObject.addProperty("code", 400);
                jsonObject.addProperty("message", "Không tìm thấy người dùng!");
                printJson(resp, gson.toJson(jsonObject));
                return;
            }

            UUID productId = UUID.fromString(pId);
            Optional<ProductEntity> productEntity = productRepository
                    .getProductById(productId);

            if (productEntity.isEmpty()) {
                resp.setStatus(400);
                jsonObject.addProperty("code", 400);
                jsonObject.addProperty("message", "Tham số không hợp lệ!");
                printJson(resp, gson.toJson(jsonObject));
                return;
            }
            ProductEntity product = productEntity.get();

            if(product.getStatus() == ProductConstant.PRODUCT_STATUS_STOP_TRADING){
                resp.setStatus(400);
                jsonObject.addProperty("code", 400);
                jsonObject.addProperty("message", "Không thể giao dịch đơn trung gian này!");
                printJson(resp, gson.toJson(jsonObject));
                return;
            }

            HttpSession httpSession = req.getSession(false);
            UUID userId = (UUID) httpSession.getAttribute(UserConstant.SESSION_USERID);

            Optional<UserEntity> userEntity = userRepository
                    .getUserById(userId);
            if (userEntity.isEmpty()) {
                resp.setStatus(400);
                jsonObject.addProperty("code", 400);
                jsonObject.addProperty("message", "Không tìm thấy người dùng!");
                printJson(resp, gson.toJson(jsonObject));
                return;
            }

            BigInteger productPrice = product.getPrice();
            BigInteger userBalance = userEntity.get().getBalance();

            BigInteger fee;
            if (productPrice.compareTo(BigInteger.valueOf(10000)) > 0) {
                fee = (productPrice.multiply(BigInteger.valueOf(5)))
                        .divide(BigInteger.valueOf(100));
            } else {
                fee = BigInteger.valueOf(100);
            }

            BigInteger needToPay = productPrice;
            if(!product.isSeller()){
                needToPay = needToPay.add(fee);
            }

            //Check user có đủ tiền ko và người trả tiền trung gian là người mua
            if (userBalance.compareTo(needToPay) < 0) {
                resp.setStatus(400);
                jsonObject.addProperty("code", 400);
                jsonObject.addProperty("message", "NOT_ENOUGH_MONEY");
                printJson(resp, gson.toJson(jsonObject));
                return;
            }

            ServletContext context = getServletContext();
            Queue<TransactionQueueDto> transactionQueue = (Queue<TransactionQueueDto>) context
                    .getAttribute("transaction_queue");

            InternalTransactionEntity internalTransaction = InternalTransactionEntity.builder()
                    .id(UUID.randomUUID())
                    .from(userId)
                    .amount(needToPay)
                    .description("Tạm thu phí giao dịch trung gian cho giao dịch đơn hàng: " + productId)
                    .status(TransactionConstant.INTERNAL_SUB)
                    .build();

            internalTransactionRepository.add(internalTransaction);
            //Đưa vào queue trừ tiền
            transactionQueue.add(new TransactionQueueDto(userId, "SUB_AM", needToPay));

            product.setStatus(ProductConstant.PRODUCT_STATUS_STOP_TRADING);
            product.setUpdatable(false);
            productRepository.update(product);

            OrderEntity orderEntity = OrderEntity.builder()
                    .id(UUID.randomUUID())
                    .userId(userId)
                    .productId(productId)
                    .amount(productPrice)
                    .fee(fee)
                    .status(OrderConstant.ORDER_CHECKING)
                    .build();
            boolean isOrderSuccess = orderRepository.add(orderEntity);

            if (!isOrderSuccess) {
                resp.setStatus(400);
                jsonObject.addProperty("code", 400);
                jsonObject.addProperty("message", "Giao dịch không thành công!");
                printJson(resp, gson.toJson(jsonObject));
                return;
            }

            resp.setStatus(200);
            jsonObject.addProperty("code", 200);
            jsonObject.addProperty("message", req.getContextPath()+"/order");
            printJson(resp, gson.toJson(jsonObject));
        } catch (Exception e) {
            resp.setStatus(500);
            jsonObject.addProperty("code", 500);
            jsonObject.addProperty("message", "Đã có lỗi xảy ra!");
            printJson(resp, gson.toJson(jsonObject));
        }
    }
}
