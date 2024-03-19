package controllers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dataAccess.InternalTransactionRepository;
import dataAccess.ProductRepository;
import dataAccess.UserRepository;
import dtos.TransactionQueueDto;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.InternalTransactionEntity;
import models.ProductEntity;
import models.UserEntity;
import models.common.Pagination;
import models.common.ViewPaging;
import utils.annotations.Authorization;
import utils.constants.TransactionConstant;
import utils.constants.UserConstant;
import utils.validation.StringValidator;

import java.io.IOException;

import java.math.BigInteger;
import java.util.*;

@WebServlet(name = "ProductSaleOrderController", urlPatterns = {"/product/sale", "/sale"})
@Authorization(role = "USER", isPublic = false)
public class ProductSaleOrderController extends BaseController {

    private UserRepository userRepository;
    private ProductRepository productRepository;
    private InternalTransactionRepository internalTransactionRepository;
    private Gson gson;

    @Override
    public void init() throws ServletException {
        this.userRepository = new UserRepository();
        this.productRepository = new ProductRepository();
        this.internalTransactionRepository = new InternalTransactionRepository();
        this.gson = new Gson();
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session != null) {
            UUID userId = (UUID) session.getAttribute(UserConstant.SESSION_USERID);


            boolean isSeller = productRepository.isUserSeller(userId);

            if (isSeller) {
                String currentPage = req.getParameter("current");
                String pageSize = req.getParameter("size");
                String pageRange = req.getParameter("range");

                try {
                    if (StringValidator.isNullOrBlank(currentPage)
                            || StringValidator.isNullOrBlank(pageSize)
                            || StringValidator.isNullOrBlank(pageRange)) {
                        currentPage = "1";
                        pageSize = "10";
                        pageRange = "5";
                    }

                    long userCount = productRepository.countAllByUser(userId);
                    Pagination pagination = new Pagination(userCount, Integer.parseInt(currentPage), Integer.parseInt(pageRange), Integer.parseInt(pageSize));

                    int startPage = (pagination.getCurrentPage() - 1) * pagination.getPageSize();
                    int endPage = pagination.getPageSize();
                    List<ProductEntity> product = productRepository.getUserProductWithPaging(startPage, endPage, userId);

                    req.setAttribute("VIEW_PAGING", new ViewPaging<>(product, pagination));

                    req.getRequestDispatcher("/pages/product/product-sale.jsp").forward(req, resp);
                } catch (Exception e) {
                    e.printStackTrace();
                    Pagination pagination = new Pagination(0, 1, 5, 10);
                    req.setAttribute("VIEW_PAGING", new ViewPaging<ProductEntity>(new ArrayList<>(), pagination));
                }
            } else {
                req.getRequestDispatcher("/pages/product/product-sale.jsp").forward(req, resp);
            }
        }
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        JsonObject jsonObject = new JsonObject();
        try {
            String title = req.getParameter("title");
            String priceString = req.getParameter("price");
            String description = req.getParameter("description");
            String contact = req.getParameter("contact");
            String secret = req.getParameter("secret");
            String isPublic = req.getParameter("public");

            // Kiểm tra định dạng giá tiền
            BigInteger price = new BigInteger(priceString);

            HttpSession session = req.getSession(false);
            UUID userId = (UUID) session.getAttribute(UserConstant.SESSION_USERID);
            Optional<UserEntity> userEntity = userRepository.getUserById(userId);

            if(userEntity.isEmpty()){
                resp.setStatus(400);
                jsonObject.addProperty("code", 400);
                jsonObject.addProperty("message", "Không tìm thấy người dùng!");
                printJson(resp, gson.toJson(jsonObject));
                return;
            }

            BigInteger balance = userEntity.get().getBalance();
            BigInteger feeAmount = new BigInteger("500");
            if (balance.compareTo(feeAmount) < 0) {
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
                    .amount(feeAmount)
                    .description("Thu phí tạo đơn trung gian!")
                    .status(TransactionConstant.INTERNAL_SUB)
                    .build();

            internalTransactionRepository.add(internalTransaction);
            //Đưa vào queue trừ tiền
            transactionQueue.add(new TransactionQueueDto(userId, "SUB_AM", feeAmount));

            ProductEntity productEntity = ProductEntity.builder()
                    .id(UUID.randomUUID())
                    .userId(userId)
                    .title(title)
                    .categoryId(productRepository.getCategoryId())
                    .description(description)
                    .secret(secret)
                    .price(price)
                    .contact(contact)
                    .isPublic(isPublic != null && isPublic.equals("on"))
                    .updatable(true)
                    .quantity(0)
                    .status((short) 1)
                    .isSeller(true)
                    .build();

            Optional<ProductEntity> addedProduct = productRepository.addProduct(productEntity);
            if(addedProduct.isEmpty()){
                resp.setStatus(400);
                jsonObject.addProperty("code", 400);
                jsonObject.addProperty("message", "Lỗi khi thêm đơn trung gian!");
                printJson(resp, gson.toJson(jsonObject));
                return;
            }

            resp.setStatus(200);
            jsonObject.addProperty("code", 200);
            jsonObject.addProperty("message", "Thêm đơn trung gian thành công!");
            printJson(resp, gson.toJson(jsonObject));
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(500);
            jsonObject.addProperty("code", 500);
            jsonObject.addProperty("message", "Lỗi khi thêm đơn trung gian!");
            printJson(resp, gson.toJson(jsonObject));
        }
    }

}