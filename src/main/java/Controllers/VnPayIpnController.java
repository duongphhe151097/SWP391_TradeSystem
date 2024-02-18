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
import com.google.gson.Gson;
import com.google.gson.JsonObject;
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
    private UserRepository userRepository;
    private ExternalTransactionRepository transactionRepository;
    private VnPayTransactionRepository vnPayTransactionRepository;

    @Override
    public void init() throws ServletException {
        super.init();
        this.userRepository = new UserRepository();
        this.transactionRepository = new ExternalTransactionRepository();
        this.vnPayTransactionRepository = new VnPayTransactionRepository();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");

        String vnpRespCode = req.getParameter(VnPayConstant.vnp_ResponseCode);
        String vnp_SecureHash = req.getParameter(VnPayConstant.vnp_SecureHash);
        JsonObject jsonObject = new JsonObject();

        Gson gson = new Gson();

        Map<String, String> fields = new HashMap<>();
        for (Enumeration<String> params = req.getParameterNames(); params.hasMoreElements(); ) {
            String fieldName = URLEncoder.encode(params.nextElement(), StandardCharsets.US_ASCII);
            String fieldValue = URLEncoder.encode(req.getParameter(fieldName), StandardCharsets.US_ASCII);
            if ((fieldValue != null) && (!fieldValue.isEmpty())) {
                fields.put(fieldName, fieldValue);
            }
        }

        fields.remove(VnPayConstant.vnp_SecureHashType);
        fields.remove(VnPayConstant.vnp_SecureHash);

        String signValue = VnPayService.hashAllFields(fields);
        if (!signValue.equals(vnp_SecureHash)) {
            //Code: 97, Message: Invalid Checksum
            jsonObject.addProperty("RspCode", "97");
            jsonObject.addProperty("Message", "Invalid Checksum");
            resp.getWriter().write(gson.toJson(jsonObject));
            return;
        }

        String vnpTxnRef = fields.getOrDefault(VnPayConstant.vnp_TxnRef, "");
        if (vnpTxnRef.isBlank() || vnpTxnRef.length() > 32) {
            jsonObject.addProperty("RspCode", "01");
            jsonObject.addProperty("Message", "Order not found");
            resp.getWriter().write(gson.toJson(jsonObject));
            return;
        };
        UUID transactionId = StringConvertor.convertToUUID(vnpTxnRef);

        Optional<ExternalTransactionEntity> optionalExternalTransactionEntity = transactionRepository
                .getExternalTransactionByIdType(transactionId, TransactionConstant.VNPAY);

        //TODO: Check transactionId in db
        boolean checkOrderId = optionalExternalTransactionEntity.isPresent(); // Giá trị của vnp_TxnRef tồn tại trong CSDL của merchant
        if (!checkOrderId) {
            //Code: 01, Message: Order not found
            jsonObject.addProperty("RspCode", "01");
            jsonObject.addProperty("Message", "Order not found");
            resp.getWriter().write(gson.toJson(jsonObject));
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
            jsonObject.addProperty("RspCode", "04");
            jsonObject.addProperty("Message", "Invalid amount");
            resp.getWriter().write(gson.toJson(jsonObject));
            return;
        }

        //TODO: Check status of transactionId
        boolean checkOrderStatus = externalTransactionEntity.getStatus() == TransactionConstant.STATUS_PROCESSING;
        if (!checkOrderStatus) {
            //Code: 02, Message: Order already confirmed
            jsonObject.addProperty("RspCode", "02");
            jsonObject.addProperty("Message", "Order already confirmed");
            resp.getWriter().write(gson.toJson(jsonObject));
            return;
        }

        Optional<VnPayTransactionEntity> optionalVnPayTransactionEntity = vnPayTransactionRepository
                .getByTransactionId(optionalExternalTransactionEntity.get().getId());

        if (optionalVnPayTransactionEntity.isEmpty()) {
            jsonObject.addProperty("RspCode", "00");
            jsonObject.addProperty("Message", "Confirm Success");
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
            transactionRepository.update(externalTransactionEntity);
            jsonObject.addProperty("RspCode", "00");
            jsonObject.addProperty("Message", "Confirm Success");
            resp.getWriter().write(gson.toJson(jsonObject));
            return;
        }

        Optional<UserEntity> optionalUserEntity = userRepository
                .getUserById(externalTransactionEntity.getUserId());
        if (optionalUserEntity.isEmpty()) {
            externalTransactionEntity.setStatus(TransactionConstant.STATUS_FAILED);
            transactionRepository.update(externalTransactionEntity);
            jsonObject.addProperty("RspCode", "00");
            jsonObject.addProperty("Message", "Confirm Success");
            resp.getWriter().write(gson.toJson(jsonObject));
            return;
        }
        UserEntity userEntity = optionalUserEntity.get();
        BigDecimal newBalance = userEntity.getBalance()
                .add(externalTransactionEntity.getAmount());

        externalTransactionEntity.setStatus(TransactionConstant.STATUS_SUCCESSED);
        transactionRepository.update(externalTransactionEntity);
        vnPayTransactionRepository.update(vnPayTransactionEntity);
        userRepository.updateUserBalance(userEntity.getId(), newBalance);

        jsonObject.addProperty("RspCode", "00");
        jsonObject.addProperty("Message", "Confirm Success");
        resp.getWriter().write(gson.toJson(jsonObject));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}
