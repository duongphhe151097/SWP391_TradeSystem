package controllers;

import dataAccess.ProductRepository;
import dataAccess.UserRepository;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.ProductEntity;
import models.UserEntity;
import models.common.Pagination;
import models.common.ViewPaging;
import utils.constants.UserConstant;
import utils.validation.StringValidator;

import java.io.IOException;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@WebServlet(name = "SalesOrdersController", urlPatterns = "/sale")
public class SalesOrdersController extends HttpServlet {

    private UserRepository userRepository;

    @Override
    public void init() throws ServletException {
        this.userRepository = new UserRepository();
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ProductRepository productRepository = new ProductRepository();

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
                        pageSize = "5";
                        pageRange = "5";
                    }

                    long userCount = productRepository.countAllByUser(userId);
                    Pagination pagination = new Pagination(userCount, Integer.parseInt(currentPage), Integer.parseInt(pageRange), Integer.parseInt(pageSize));

                    int startPage = (pagination.getCurrentPage() - 1) * pagination.getPageSize();
                    int endPage = pagination.getPageSize();
                    List<ProductEntity> product = productRepository.getUserProductWithPaging(startPage, endPage, userId);

                    req.setAttribute("VIEW_PAGING", new ViewPaging<>(product, pagination));

                    req.getRequestDispatcher("/pages/salesorders.jsp").forward(req, resp);
                } catch (Exception e) {
                    e.printStackTrace();
                    Pagination pagination = new Pagination(0, 1, 5, 10);
                    req.setAttribute("VIEW_PAGING", new ViewPaging<ProductEntity>(new ArrayList<>(), pagination));
                }
            } else {
                req.getRequestDispatcher("/pages/salesorders.jsp").forward(req, resp);
            }
        }
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ProductRepository productRepository = new ProductRepository();
doGet(req,resp);
        try {
            String title = req.getParameter("title");
            String priceString = req.getParameter("price");
            String role = req.getParameter("role");
            String description = req.getParameter("description");
            String contact = req.getParameter("contact");
            String secret = req.getParameter("secret");
            String isPublic = req.getParameter("public");

            // Kiểm tra định dạng giá tiền


            BigInteger price = new BigInteger(priceString);

            HttpSession session = req.getSession(false);
            UUID userId = (UUID) session.getAttribute(UserConstant.SESSION_USERID);
            Optional<UserEntity> userEntity = userRepository.getUserById(userId);

            if (userId == null) {
                req.setAttribute("resultMessage", "Phiên người dùng không hợp lệ!");
                return;
            }

            BigInteger balance = userEntity.get().getBalance();

            BigInteger feeAmount = new BigInteger("500");
            if (balance.compareTo(feeAmount) >= 0) {
                BigInteger newBalance = balance.subtract(feeAmount);


                productRepository.updateUserBalance(userId,newBalance);



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
                        .updatable(false)
                        .quantity(0)
                        .status((short) 1)
                        .isSeller(true)
                        .build();


                Optional<ProductEntity> addedProduct = productRepository.addProduct(productEntity);

                if (addedProduct.isPresent()) {

                    req.getRequestDispatcher("/pages/salesorders.jsp").forward(req, resp);
                } else {

                    req.setAttribute( "resultMessage","Lỗi khi thêm sản phẩm vào cơ sở dữ liệu!");

                }
            } else {

                req.setAttribute("resultMessage", "Số dư trong tài khoản không đủ để đăng bài!");
            }
        } catch (Exception e) {
            e.printStackTrace();

            req.setAttribute("resultMessage", "Đã xảy ra lỗi khi thêm sản phẩm: ");

        }
        RequestDispatcher dispatcher = req.getRequestDispatcher("/pages/salesorders.jsp");
        dispatcher.forward(req, resp);
    }

}