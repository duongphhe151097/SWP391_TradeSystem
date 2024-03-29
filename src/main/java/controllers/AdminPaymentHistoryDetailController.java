package controllers;

import dataAccess.ExternalTransactionRepository;
import dataAccess.VnPayTransactionRepository;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.ExternalTransactionEntity;
import models.VnPayTransactionEntity;
import utils.annotations.Authorization;
import utils.constants.TransactionConstant;
import utils.validation.StringValidator;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@WebServlet(name = "AdminPaymentHistoryDetailController", urlPatterns = "/admin/payment/history/detail")
@Authorization(role = "ADMIN", isPublic = false)
public class AdminPaymentHistoryDetailController extends BaseController{
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
            req.setAttribute("ERR_MESSAGE", "Tham số không hợp lệ!");
            dispatcher.forward(req, resp);
            return;
        }

        if(!type.equals(TransactionConstant.VNPAY) && !type.equals(TransactionConstant.INTERNAL)){
            req.setAttribute("ERR_MESSAGE", "Kiểu thanh toán không hợp lệ!");
            dispatcher.forward(req, resp);
            return;
        }

        if(!StringValidator.isUUID(id)){
            req.setAttribute("ERR_MESSAGE", "Id thanh toán không hợp lệ!");
            dispatcher.forward(req, resp);
            return;
        }

        UUID tId = UUID.fromString(id);
        Optional<VnPayTransactionEntity> optionalVnPayTransactionEntity = vnPayTransactionRepository
                .getByTransactionId(tId);

        Optional<ExternalTransactionEntity> optionalExternalTransactionEntity = externalTransactionRepository
                .getExternalTransactionByIdType(tId, TransactionConstant.VNPAY);

        if(optionalVnPayTransactionEntity.isEmpty() || optionalExternalTransactionEntity.isEmpty()){
            req.setAttribute("ERR_NOTFOUND", "Không tìm thấy id!");
            dispatcher.forward(req, resp);
            return;
        }

        req.setAttribute("VAR_TRANSDETAIL", optionalVnPayTransactionEntity.get());
        req.setAttribute("VAR_TRANSEXT", optionalExternalTransactionEntity.get());
        req.setAttribute("VAR_TYPE", type);

        dispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }
}
