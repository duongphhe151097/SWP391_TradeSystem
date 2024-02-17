package Controllers;

import DataAccess.ExternalTransactionRepository;
import DataAccess.VnPayTransactionRepository;
import Models.ExternalTransactionEntity;
import Models.VnPayTransactionEntity;
import Services.VnPayService;
import Utils.Annotations.Authorization;
import Utils.Constants.TransactionConstant;
import Utils.Constants.UserConstant;
import Utils.Constants.VnPayConstant;
import Utils.Validation.TimeValidator;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@WebServlet(name = "VnPayCreatePaymentController", urlPatterns = "/payment/vnpay/create")
@Authorization(role = "", isPublic = false)
public class VnPayCreatePaymentController extends BaseController {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/pages/payment/vnpay-createpayment.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String vnp_Version = "2.1.0";
            String vnp_Command = "pay";
            String orderType = "other";
            long amount = Long.parseLong(req.getParameter("amount")) * 100L;
            String bankCode = req.getParameter("bankCode");

//            String vnp_TxnRef = StringGenerator.generateRandomString(10);
            UUID transactionId = UUID.randomUUID();
            String vnp_TxnRef = transactionId.toString().replace("-", "");

            // Check only one id unique in a day
            ExternalTransactionRepository externalTransactionRepository = new ExternalTransactionRepository();
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

            // TODO: Add insert to db
            HttpSession httpSession = req.getSession(false);
            UUID userId = (UUID) httpSession.getAttribute(UserConstant.SESSION_USERID);
            ExternalTransactionEntity insertEntity = ExternalTransactionEntity
                    .builder()
                    .id(transactionId)
                    .type(TransactionConstant.VNPAY)
                    .amount(BigDecimal.valueOf(amount / 100L))
                    .command(TransactionConstant.CASH_IN)
                    .status(TransactionConstant.STATUS_PROCESSING)
                    .userId(userId)
                    .build();
            externalTransactionRepository.add(insertEntity);

            VnPayTransactionRepository vnPayTransactionRepository = new VnPayTransactionRepository();

            VnPayTransactionEntity insertVnPayTransEntity = VnPayTransactionEntity
                    .builder()
                    .id(UUID.randomUUID())
                    .transactionId(transactionId)
                    .type(TransactionConstant.VNPAY)
                    .version(vnp_Version)
                    .command(vnp_Command)
                    .amount(BigDecimal.valueOf(amount / 100L))
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
            req.setAttribute("ERROR_MESSAGE", "Tạo giao dịch không thành công");
            req.getRequestDispatcher("/pages/payment/vnpay-createpayment.jsp").forward(req, resp);
        }
    }
}
