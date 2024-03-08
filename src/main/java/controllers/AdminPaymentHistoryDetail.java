package controllers;

import dataAccess.ExternalTransactionRepository;
import dataAccess.VnPayTransactionRepository;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.annotations.Authorization;
import utils.constants.TransactionConstant;
import utils.validation.StringValidator;

import java.io.IOException;

@WebServlet(name = "AdminPaymentHistoryDetail", urlPatterns = "/admin/payment/history/detail")
@Authorization(role = "ADMIN", isPublic = false)
public class AdminPaymentHistoryDetail extends BaseController{
    private ExternalTransactionRepository externalTransactionRepository;
    private VnPayTransactionRepository vnPayTransactionRepository;

    @Override
    public void init() throws ServletException {
        this.externalTransactionRepository = new ExternalTransactionRepository();
        this.vnPayTransactionRepository = new VnPayTransactionRepository();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher dispatcher = req.getRequestDispatcher("/pages/admin/admin_payment-detail.jsp");

        String id = req.getParameter("id");
        String type = req.getParameter("type");

        if(StringValidator.isNullOrBlank(id) || StringValidator.isNullOrBlank(type)){
            req.setAttribute("ERR_MESSAGE", "Tham số không hợp lệ");
            dispatcher.forward(req, resp);
            return;
        }

        if(!type.equals(TransactionConstant.VNPAY) || !type.equals(TransactionConstant.INTERNAL)){
            req.setAttribute("ERR_MESSAGE", "Kiểu thanh toán không hợp lệ");
            dispatcher.forward(req, resp);
            return;
        }

        if(!StringValidator.isUUID(id)){
            req.setAttribute("ERR_MESSAGE", "Id thanh toán không hợp lệ");
            dispatcher.forward(req, resp);
            return;
        }



        dispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }
}
