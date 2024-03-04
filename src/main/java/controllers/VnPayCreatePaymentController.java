package controllers;

import dataAccess.ExternalTransactionRepository;
import dataAccess.VnPayTransactionRepository;
import models.ExternalTransactionEntity;
import models.VnPayTransactionEntity;
import services.VnPayService;
import utils.annotations.Authorization;
import utils.constants.TransactionConstant;
import utils.constants.UserConstant;
import utils.constants.VnPayConstant;
import utils.validation.TimeValidator;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@WebServlet(name = "VnPayCreatePaymentController", urlPatterns = "/payment/vnpay/create")
@Authorization(role = "", isPublic = false)
public class VnPayCreatePaymentController extends BaseController {
    private VnPayTransactionRepository vnPayTransactionRepository;
    private ExternalTransactionRepository externalTransactionRepository;

    @Override
    public void init() throws ServletException {
        super.init();
        this.vnPayTransactionRepository = new VnPayTransactionRepository();
        this.externalTransactionRepository = new ExternalTransactionRepository();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/pages/payment/vnpay-createpayment.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher dispatcher = req.getRequestDispatcher("/pages/payment/vnpay-createpayment.jsp");

        try {
            String vnp_Version = "2.1.0";
            String vnp_Command = "pay";
            String orderType = "other";
            long amount = Long.parseLong(req.getParameter("amount")) * 100L;

            if(amount < TransactionConstant.MIN_AMOUNT || amount > TransactionConstant.MAX_AMOUNT){
                req.setAttribute("ERROR_AMOUNT", "Số tiền phải lớn hơn 10,000đ và nhỏ hơn 10,000,000đ");
                dispatcher.forward(req, resp);
                return;
            }
            String bankCode = req.getParameter("bankCode");

            //String vnp_TxnRef = StringGenerator.generateRandomString(10);
            UUID transactionId = UUID.randomUUID();
            String vnp_TxnRef = transactionId.toString()
                    .replace("-", "");

            // Check only one id unique in a day
            Optional<ExternalTransactionEntity> externalTransactionEntity = externalTransactionRepository
                    .getExternalTransactionByIdType(transactionId, TransactionConstant.VNPAY);

            if (externalTransactionEntity.isPresent()) {
                ExternalTransactionEntity extTransEntity = externalTransactionEntity.get();
                boolean isWithinDay = TimeValidator.isWithinDay(extTransEntity.getCreateAt());
                if (isWithinDay) {
                    transactionId = UUID.randomUUID();
                    vnp_TxnRef = transactionId.toString().replace("-", "");
                }
            }

            String vnp_IpAddr = VnPayService.getIpAddress(req);
            String vnp_TmnCode = VnPayService.vnp_TmnCode;

            Map<String, String> vnp_Params = new HashMap<>();
            vnp_Params.put(VnPayConstant.vnp_Version, vnp_Version);
            vnp_Params.put(VnPayConstant.vnp_Command, vnp_Command);
            vnp_Params.put(VnPayConstant.vnp_TmnCode, vnp_TmnCode);
            vnp_Params.put(VnPayConstant.vnp_Amount, String.valueOf(amount));
            vnp_Params.put(VnPayConstant.vnp_CurrCode, "VND");

            if (bankCode != null && !bankCode.isEmpty()) {
                vnp_Params.put(VnPayConstant.vnp_BankCode, bankCode);
            }
            vnp_Params.put(VnPayConstant.vnp_TxnRef, vnp_TxnRef);
            String orderInfo = "Xu ly yeu cau:" + vnp_TxnRef;
            vnp_Params.put(VnPayConstant.vnp_OrderInfo, orderInfo);
            vnp_Params.put(VnPayConstant.vnp_OrderType, orderType);

            String locate = req.getParameter("language");
            if (locate != null && !locate.isEmpty()) {
                vnp_Params.put(VnPayConstant.vnp_Locale, locate);
            } else {
                locate = "vn";
                vnp_Params.put(VnPayConstant.vnp_Locale, "vn");
            }

            vnp_Params.put(VnPayConstant.vnp_ReturnUrl, getBaseURL(req) + VnPayService.vnp_ReturnUrl);
            vnp_Params.put(VnPayConstant.vnp_IpAddr, vnp_IpAddr);

            LocalDateTime currentDate = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

            String vnp_CreateDate = currentDate.format(formatter);
            vnp_Params.put(VnPayConstant.vnp_CreateDate, vnp_CreateDate);

            LocalDateTime expireDate = currentDate.plusMinutes(15);
            String vnp_ExpireDate = expireDate.format(formatter);
            vnp_Params.put(VnPayConstant.vnp_ExpireDate, vnp_ExpireDate);

            List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
            Collections.sort(fieldNames);
            StringBuilder hashData = new StringBuilder();
            StringBuilder query = new StringBuilder();
            Iterator<String> itr = fieldNames.iterator();
            while (itr.hasNext()) {
                String fieldName = itr.next();
                String fieldValue = vnp_Params.get(fieldName);
                if ((fieldValue != null) && (!fieldValue.isEmpty())) {
                    //Build hash data
                    hashData.append(fieldName);
                    hashData.append('=');
                    hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                    //Build query
                    query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII));
                    query.append('=');
                    query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                    if (itr.hasNext()) {
                        query.append('&');
                        hashData.append('&');
                    }
                }
            }
            String vnp_SecureHash = VnPayService.hmacSHA512(VnPayService.secretKey, hashData.toString());
            query.append("&vnp_SecureHash=").append(vnp_SecureHash);
            String queryUrl = query.toString();
            String paymentUrl = VnPayService.vnp_PayUrl + "?" + queryUrl;

            HttpSession httpSession = req.getSession(false);
            UUID userId = (UUID) httpSession.getAttribute(UserConstant.SESSION_USERID);

            //TODO: Check user still have processing order or not
            Optional<ExternalTransactionEntity> optionalExternalTransactionEntity = externalTransactionRepository
                    .getExternalTransactionByUid(userId);

            if(optionalExternalTransactionEntity.isPresent()){
                ExternalTransactionEntity externalTransaction = optionalExternalTransactionEntity.get();
                if(externalTransaction.getStatus() == TransactionConstant.STATUS_SUCCESSED
                        && externalTransaction.getCreateAt().plusMinutes(15).isAfter(LocalDateTime.now())){
                    req.setAttribute("ERROR_MESSAGE", "Bạn đang có giao dịch chưa hoàn thành, " +
                            "nếu bạn không thể tạo giao dịch vui lòng thử lại sau 15 phút!");
                    dispatcher.forward(req, resp);
                }
            }

            // TODO: Add insert to db
            ExternalTransactionEntity insertEntity = ExternalTransactionEntity
                    .builder()
                    .id(transactionId)
                    .type(TransactionConstant.VNPAY)
                    .amount(BigInteger.valueOf(amount / 100L))
                    .command(TransactionConstant.CASH_IN)
                    .status(TransactionConstant.STATUS_PROCESSING)
                    .userId(userId)
                    .build();
            externalTransactionRepository.add(insertEntity);

            VnPayTransactionEntity insertVnPayTransEntity = VnPayTransactionEntity
                    .builder()
                    .id(UUID.randomUUID())
                    .transactionId(transactionId)
                    .type(TransactionConstant.VNPAY)
                    .version(vnp_Version)
                    .command(vnp_Command)
                    .amount(BigInteger.valueOf(amount / 100L))
                    .currentCode("VND")
                    .bankCode(bankCode)
                    .locale(locate)
                    .ipAddr(vnp_IpAddr)
                    .orderInfo(orderInfo)
                    .orderType(orderType)
                    .createDate(vnp_CreateDate)
                    .expireDate(vnp_ExpireDate)
                    .secureHash(vnp_SecureHash)
                    .transactionNo("")
                    .build();
            vnPayTransactionRepository.add(insertVnPayTransEntity);

            resp.sendRedirect(paymentUrl);
        } catch (Exception e) {
            req.setAttribute("ERROR_MESSAGE", "Tạo giao dịch không thành công!");
            dispatcher.forward(req, resp);
        }
    }
}
