package controllers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dataAccess.*;
import dtos.TransactionQueueDto;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.*;
import models.common.Pagination;
import models.common.ViewPaging;
import utils.annotations.Authorization;
import utils.constants.OrderConstant;
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
    private ProductRepository productRepository;
    private OrderRepository orderRepository;
    private Gson gson;

    @Override
    public void init() throws ServletException {
        this.userReportRepository = new UserReportRepository();
        this.userRepository = new UserRepository();
        this.internalTransactionRepository = new InternalTransactionRepository();
        this.productRepository = new ProductRepository();
        this.orderRepository = new OrderRepository();
        this.gson = new Gson();
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
            if (!StringValidator.isNullOrBlank(filterStatus)) {
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
        resp.setContentType("application/json");
        JsonObject jsonObject = new JsonObject();

        String pid = req.getParameter("pid");
        String reportContent = req.getParameter("r_content");
        String reportTitle = req.getParameter("r_title");

        try {
            HttpSession httpSession = req.getSession(false);
            UUID userId = (UUID) httpSession.getAttribute(UserConstant.SESSION_USERID);

            if(StringValidator.isNullOrBlank(pid) || !StringValidator.isUUID(pid)){
                resp.setStatus(400);
                jsonObject.addProperty("code", 400);
                jsonObject.addProperty("message", "Mã sản phẩm không hợp lệ!");
                printJson(resp, gson.toJson(jsonObject));
                return;
            }

            if(StringValidator.isNullOrBlank(reportContent)){
                resp.setStatus(400);
                jsonObject.addProperty("code", 400);
                jsonObject.addProperty("message", "Không được để trống nội dung!");
                printJson(resp, gson.toJson(jsonObject));
                return;
            }

            if(StringValidator.isNullOrBlank(reportTitle)){
                resp.setStatus(400);
                jsonObject.addProperty("code", 400);
                jsonObject.addProperty("message", "Không được để trống tiêu đề!");
                printJson(resp, gson.toJson(jsonObject));
                return;
            }

            UUID productId = UUID.fromString(pid);
            Optional<ProductEntity> optionalProductEntity = productRepository
                    .getProductById(productId);
            if(optionalProductEntity.isEmpty()){
                resp.setStatus(400);
                jsonObject.addProperty("code", 400);
                jsonObject.addProperty("message", "Không tìm thấy đơn trung gian!");
                printJson(resp, gson.toJson(jsonObject));
                return;
            }

            Optional<OrderEntity> optionalOrderEntity = orderRepository
                    .getOrderByUserId(userId, productId);
            if(optionalOrderEntity.isEmpty()){
                resp.setStatus(400);
                jsonObject.addProperty("code", 400);
                jsonObject.addProperty("message", "Bạn không sở hữu sản phẩm này!");
                printJson(resp, gson.toJson(jsonObject));
                return;
            }

            ProductEntity product = optionalProductEntity.get();
            //Get, set new status and update order
            OrderEntity order = optionalOrderEntity.get();
            order.setStatus(OrderConstant.ORDER_REPORTED);
            orderRepository.update(order);

            //Add report to db
            reportContent = reportContent.replace("&nbsp;", "");
            UserReportEntity userReportEntity = UserReportEntity.builder()
                    .id(UUID.randomUUID())
                    .userId(userId)
                    .productTarget(productId)
                    .userTarget(product.getUserId())
                    .type(ReportConstant.USER_REPORT)
                    .title(reportTitle)
                    .description(reportContent)
                    .status(ReportConstant.REPORT_BUYER_CREATED)
                    .build();
            boolean isReportSuccess = userReportRepository.add(userReportEntity);

            if(!isReportSuccess){
                resp.setStatus(400);
                jsonObject.addProperty("code", 400);
                jsonObject.addProperty("message", "Tạo khiếu nại thất bại!");
                printJson(resp, gson.toJson(jsonObject));
                return;
            }

            resp.setStatus(200);
            jsonObject.addProperty("code", 200);
            jsonObject.addProperty("message", "Tạo khiếu nại thành công!");
            printJson(resp, gson.toJson(jsonObject));
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(500);
            jsonObject.addProperty("code", 500);
            jsonObject.addProperty("message", "Lỗi server!");
            printJson(resp, gson.toJson(jsonObject));
        }
    }
}
