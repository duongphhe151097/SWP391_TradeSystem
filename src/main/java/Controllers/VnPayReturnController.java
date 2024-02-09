package Controllers;

import Services.VnPayService;
import Utils.Annotations.Authorization;
import Utils.Constants.VnPayConstant;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "VnPayReturnController", urlPatterns = "/payment/vnpay/return")
@Authorization(role = "", isPublic = true)
public class VnPayReturnController extends BaseController {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, String> fields = new HashMap<>();
        for (Enumeration<String> params = req.getParameterNames(); params.hasMoreElements(); ) {
            String fieldName = URLEncoder.encode(params.nextElement(), StandardCharsets.US_ASCII);
            String fieldValue = URLEncoder.encode(req.getParameter(fieldName), StandardCharsets.US_ASCII);
            if ((fieldValue != null) && (!fieldValue.isEmpty())) {
                fields.put(fieldName, fieldValue);
            }
        }

        String vnp_SecureHash = req.getParameter("vnp_SecureHash");
        fields.remove("vnp_SecureHashType");
        fields.remove("vnp_SecureHash");
        String signValue = VnPayService.hashAllFields(fields);

        String vnpTxnRef = req.getParameter("vnp_TxnRef");
        String vnpAmount = req.getParameter("vnp_Amount");
        String vnpOrderInfo = req.getParameter("vnp_OrderInfo");
        String vnpResponseCode = req.getParameter("vnp_ResponseCode");
        String vnpTransactionNo = req.getParameter("vnp_TransactionNo");
        String vnpBankCode = req.getParameter("vnp_BankCode");
        String vnpPayDate = req.getParameter("vnp_PayDate");
        String vnpTransactionStatus = req.getParameter("vnp_TransactionStatus");

        req.setAttribute("VAR_TxnRef", vnpTxnRef);
        req.setAttribute("VAR_Amount", vnpAmount);
        req.setAttribute("VAR_OrderInfo", vnpOrderInfo);
        req.setAttribute("VAR_RespCode", vnpResponseCode);
        req.setAttribute("VAR_TransNo", vnpTransactionNo);
        req.setAttribute("VAR_BankCode", vnpBankCode);
        req.setAttribute("VAR_PayDate", vnpPayDate);

        RequestDispatcher dispatcher = req.getRequestDispatcher("/pages/payment/vnpay-return.jsp");

        if (!signValue.equals(vnp_SecureHash)) {
            req.setAttribute("MESSAGE_NOTI", "Invalid signature!");
            dispatcher.forward(req, resp);
            return;
        }

        if (!VnPayConstant.Payment_Status_Success.equals(vnpTransactionStatus)) {
            req.setAttribute("MESSAGE_NOTI", "Không thành công!");
            dispatcher.forward(req, resp);
            return;
        }

        req.setAttribute("MESSAGE_NOTI", "Thành công!");
        dispatcher.forward(req, resp);
        return;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}
