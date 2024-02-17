package Controllers;

import DataAccess.ExternalTransactionRepository;
import DataAccess.UserRepository;
import DataAccess.VnPayTransactionRepository;
import Models.ExternalTransactionEntity;
import Models.UserEntity;
import Models.VnPayTransactionEntity;
import Services.VnPayService;
import Utils.Annotations.Authorization;
import Utils.Constants.TransactionConstant;
import Utils.Constants.VnPayConstant;
import Utils.Convert.StringConvertor;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

@WebServlet(name = "VnPayIpnReturnController", urlPatterns = {"/payment/vnpay/ipn"})
@Authorization(role = "", isPublic = true)
public class VnPayIpnController extends BaseController {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String vnpRespCode = req.getParameter(VnPayConstant.vnp_ResponseCode);

        Map<String, String> fields = new HashMap<>();
        for (Enumeration<String> params = req.getParameterNames(); params.hasMoreElements(); ) {
            String fieldName = URLEncoder.encode(params.nextElement(), StandardCharsets.US_ASCII);
            String fieldValue = URLEncoder.encode(req.getParameter(fieldName), StandardCharsets.US_ASCII);
            if ((fieldValue != null) && (!fieldValue.isEmpty())) {
                fields.put(fieldName, fieldValue);
            }
        }

        String vnp_SecureHash = req.getParameter(VnPayConstant.vnp_SecureHash);
        fields.remove(VnPayConstant.vnp_SecureHashType);
        fields.remove(VnPayConstant.vnp_SecureHash);

        RequestDispatcher dispatcher = req.getRequestDispatcher("/pages/payment/ipn-return.jsp");

        String signValue = VnPayService.hashAllFields(fields);
        if (!signValue.equals(vnp_SecureHash)) {
            //Code: 97, Message: Invalid Checksum
            req.setAttribute("VAR_IpnCode", "97");
            req.setAttribute("VAR_IpnMessage", "Invalid Checksum");
            dispatcher.forward(req, resp);
            return;
        }

        String vnpTxnRef = fields.getOrDefault(VnPayConstant.vnp_TxnRef, "00000000-0000-0000-0000-000000000000");
        if (vnpTxnRef.equals("00000000-0000-0000-0000-000000000000")) return;
        UUID transactionId = StringConvertor.convertToUUID(vnpTxnRef);
        ExternalTransactionRepository transactionRepository = new ExternalTransactionRepository();
        Optional<ExternalTransactionEntity> optionalExternalTransactionEntity = transactionRepository
                .getExternalTransactionByIdType(transactionId, TransactionConstant.VNPAY);


        //TODO: Check transactionId in db
        boolean checkOrderId = optionalExternalTransactionEntity.isPresent(); // Giá trị của vnp_TxnRef tồn tại trong CSDL của merchant
        if (!checkOrderId) {
            //Code: 01, Message: Order not found
            req.setAttribute("VAR_IpnCode", "01");
            req.setAttribute("VAR_IpnMessage", "Order not found");
            dispatcher.forward(req, resp);
            return;
        }
        ExternalTransactionEntity externalTransactionEntity = optionalExternalTransactionEntity.get();

        String vnpAmount = fields.getOrDefault(VnPayConstant.vnp_Amount, "0");
        long amount = Long.parseLong(vnpAmount) / 100L;
        //TODO: Check amount of transactionI

        //Kiểm tra số tiền thanh toán do VNPAY phản hồi(vnp_Amount/100) với số tiền của đơn hàng merchant tạo thanh toán: giả sử số tiền kiểm tra là đúng.
        boolean checkAmount = Objects.equals(externalTransactionEntity.getAmount(), BigDecimal.valueOf(amount));

        if (!checkAmount) {
            //Code: 04, Message: Invalid amount
            req.setAttribute("VAR_IpnCode", "04");
            req.setAttribute("VAR_IpnMessage", "Invalid amount");
            dispatcher.forward(req, resp);
            return;
        }

        //TODO: Check status of transactionId
        boolean checkOrderStatus = externalTransactionEntity.getStatus() == TransactionConstant.STATUS_PROCESSING;
        if (!checkOrderStatus) {
            //Code: 02, Message: Order already confirmed
            req.setAttribute("VAR_IpnCode", "02");
            req.setAttribute("VAR_IpnMessage", "Order already confirmed");
            dispatcher.forward(req, resp);
            return;
        }

        VnPayTransactionRepository vnPayTransactionRepository = new VnPayTransactionRepository();
        Optional<VnPayTransactionEntity> optionalVnPayTransactionEntity = vnPayTransactionRepository
                .getByTransactionId(optionalExternalTransactionEntity.get().getId());

        if (optionalVnPayTransactionEntity.isEmpty()) {
            System.out.println("Không thành công!");
            return;
        }

        VnPayTransactionEntity vnPayTransactionEntity = optionalVnPayTransactionEntity.get();
        vnPayTransactionEntity.setTransactionNo(fields.get("vnp_TransactionNo"));
        vnPayTransactionEntity.setPayDate(fields.get("vnp_PayDate"));
        vnPayTransactionEntity.setCardType(fields.get("vnp_CardType"));
        vnPayTransactionEntity.setBankTransNo(fields.get("vnp_BankTranNo"));

        if (!VnPayConstant.Success_Code.equals(vnpRespCode)) {
            //GD ko thành công
            externalTransactionEntity.setStatus(TransactionConstant.STATUS_FAILED);
            System.out.println("Không thành công");
            transactionRepository.update(externalTransactionEntity);
            dispatcher.forward(req, resp);
            return;
        }

        UserRepository userRepository = new UserRepository();
        Optional<UserEntity> optionalUserEntity = userRepository
                .getUserById(externalTransactionEntity.getUserId());
        if (optionalUserEntity.isEmpty()) {
            System.out.println("Không thành công");
            externalTransactionEntity.setStatus(TransactionConstant.STATUS_FAILED);
            transactionRepository.update(externalTransactionEntity);
            return;
        }
        UserEntity userEntity = optionalUserEntity.get();
        BigDecimal newBalance = userEntity.getBalance()
                .add(externalTransactionEntity.getAmount());

        externalTransactionEntity.setStatus(TransactionConstant.STATUS_SUCCESSED);
        transactionRepository.update(externalTransactionEntity);
        vnPayTransactionRepository.update(vnPayTransactionEntity);
        userRepository.updateUserBalance(userEntity.getId(), newBalance);
        System.out.println("Thành công");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}
