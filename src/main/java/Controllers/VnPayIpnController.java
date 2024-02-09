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

@WebServlet(name = "VnPayIpnReturnController", urlPatterns = {"/payment/vnpay/ipn"})
@Authorization(role = "", isPublic = true)
public class VnPayIpnController extends BaseController {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("Gọi nè!");
        Map<String, String> fields = new HashMap<>();
        for (Enumeration<String> params = req.getParameterNames(); params.hasMoreElements(); ) {
            String fieldName = URLEncoder.encode((String) params.nextElement(), StandardCharsets.US_ASCII);
            String fieldValue = URLEncoder.encode(req.getParameter(fieldName), StandardCharsets.US_ASCII);
            if ((fieldValue != null) && (!fieldValue.isEmpty())) {
                fields.put(fieldName, fieldValue);
            }
        }

        String vnp_SecureHash = req.getParameter("vnp_SecureHash");
        fields.remove("vnp_SecureHashType");
        fields.remove("vnp_SecureHash");

        RequestDispatcher dispatcher = req.getRequestDispatcher("/pages/payment/ipn-return.jsp");

        String signValue = VnPayService.hashAllFields(fields);
        if (!signValue.equals(vnp_SecureHash)) {
            //Code: 97, Message: Invalid Checksum
            req.setAttribute("VAR_IpnCode", "97");
            req.setAttribute("VAR_IpnMessage", "Invalid Checksum");
            dispatcher.forward(req, resp);
            return;
        }

        boolean checkOrderId = true; // Giá trị của vnp_TxnRef tồn tại trong CSDL của merchant
        boolean checkAmount = true; //Kiểm tra số tiền thanh toán do VNPAY phản hồi(vnp_Amount/100) với số tiền của đơn hàng merchant tạo thanh toán: giả sử số tiền kiểm tra là đúng.
        boolean checkOrderStatus = true;


        if (!checkOrderId) {
            //Code: 01, Message: Order not found
            req.setAttribute("VAR_IpnCode", "01");
            req.setAttribute("VAR_IpnMessage", "Order not found");
            dispatcher.forward(req, resp);
            return;
        }

        if (!checkAmount) {
            //Code: 04, Message: Invalid amount
            req.setAttribute("VAR_IpnCode", "04");
            req.setAttribute("VAR_IpnMessage", "Invalid amount");
            dispatcher.forward(req, resp);
            return;
        }

        if (!checkOrderStatus) {
            //Code: 02, Message: Order already confirmed
            req.setAttribute("VAR_IpnCode", "02");
            req.setAttribute("VAR_IpnMessage", "Order already confirmed");
            dispatcher.forward(req, resp);
            return;
        }

        String vnpRespCode = req.getParameter("vnp_ResponseCode");
        if (!VnPayConstant.Success_Code.equals(vnpRespCode)) {
            //GD ko thành công
            System.out.println("Không thành công");
            dispatcher.forward(req, resp);
            return;
        }
        System.out.println("Thành công");
        dispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}
