package controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dataAccess.ExternalTransactionRepository;
import dataAccess.VnPayTransactionRepository;
import dtos.TransactionQueueDto;
import dtos.VnPayResponseDto;
import jakarta.servlet.ServletContext;
import models.ExternalTransactionEntity;
import models.VnPayTransactionEntity;
import services.VnPayService;
import utils.annotations.Authorization;
import com.google.gson.JsonObject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.constants.TransactionConstant;
import utils.convert.StringConvertor;
import utils.gsoncustom.VnPayResponseDeserializer;
import utils.validation.StringValidator;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

@WebServlet(name = "VnPayQueryController", urlPatterns = {"/payment/vnpay/query"})
@Authorization(role = "ADMIN", isPublic = false)
public class VnPayQueryController extends BaseController {
    private Gson gson;
    private ExternalTransactionRepository externalTransactionRepository;
    private VnPayTransactionRepository vnPayTransactionRepository;

    @Override
    public void init() throws ServletException {
        final GsonBuilder gsonBuilder = new GsonBuilder().setPrettyPrinting();
        gsonBuilder.registerTypeAdapter(VnPayResponseDto.class, new VnPayResponseDeserializer());
        gsonBuilder.setPrettyPrinting();
        gson = gsonBuilder.create();
        externalTransactionRepository = new ExternalTransactionRepository();
        vnPayTransactionRepository = new VnPayTransactionRepository();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");

        UUID transactionId = UUID.randomUUID();
        String vnp_RequestId = transactionId.toString()
                .replace("-", "");
//        String vnp_RequestId = VnPayService.getRandomNumber(8);
        String vnp_Version = "2.1.0";
        String vnp_Command = "querydr";
        String vnp_TmnCode = VnPayService.vnp_TmnCode;
        String vnp_TxnRef = req.getParameter("txn"); // transaction id
        if (StringValidator.isUUID(vnp_TxnRef)) {
            vnp_TxnRef = vnp_TxnRef.replace("-", "");
        }
        String vnp_OrderInfo = "Kiem tra ket qua GD OrderId:" + vnp_TxnRef;
        //String vnp_TransactionNo = req.getParameter("transactionNo");
        String vnp_TransDate = req.getParameter("td"); //create date

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());

        String vnp_IpAddr = VnPayService.getIpAddress(req);

        JsonObject vnp_Params = new JsonObject();

        vnp_Params.addProperty("vnp_RequestId", vnp_RequestId);
        vnp_Params.addProperty("vnp_Version", vnp_Version);
        vnp_Params.addProperty("vnp_Command", vnp_Command);
        vnp_Params.addProperty("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.addProperty("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.addProperty("vnp_OrderInfo", vnp_OrderInfo);
        //vnp_Params.put("vnp_TransactionNo", vnp_TransactionNo);
        vnp_Params.addProperty("vnp_TransactionDate", vnp_TransDate);
        vnp_Params.addProperty("vnp_CreateDate", vnp_CreateDate);
        vnp_Params.addProperty("vnp_IpAddr", vnp_IpAddr);

        String hash_Data = String.join("|", vnp_RequestId, vnp_Version, vnp_Command, vnp_TmnCode, vnp_TxnRef, vnp_TransDate, vnp_CreateDate, vnp_IpAddr, vnp_OrderInfo);
        String vnp_SecureHash = VnPayService.hmacSHA512(VnPayService.secretKey, hash_Data);

        vnp_Params.addProperty("vnp_SecureHash", vnp_SecureHash);

        URL url = new URL(VnPayService.vnp_ApiUrl);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(vnp_Params.toString());
        wr.flush();
        wr.close();
        int responseCode = con.getResponseCode();
        System.out.println("nSending 'POST' request to URL : " + url);
        System.out.println("Post Data : " + vnp_Params);
        System.out.println("Response Code : " + responseCode);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String output;
        StringBuilder response = new StringBuilder();
        while ((output = in.readLine()) != null) {
            response.append(output);
        }
        in.close();
        VnPayResponseDto vnPayResponseDto = gson.fromJson(response.toString(), VnPayResponseDto.class);
        JsonObject jsonObject = new JsonObject();

        switch (vnPayResponseDto.getResponseCode()) {
            case "00":
                resp.setStatus(200);
                jsonObject.addProperty("code", "00");
                jsonObject.addProperty("message", "Truy vấn thành công!");
                jsonObject.addProperty("data", response.toString());
                break;

            case "91":
                resp.setStatus(400);
                jsonObject.addProperty("code", "91");
                jsonObject.addProperty("message", "Không tìm thấy giao dịch!");
                break;

            case "94":
                resp.setStatus(400);
                jsonObject.addProperty("code", "94");
                jsonObject.addProperty("message", "Yêu cầu bị trùng lặp trong thời gian giới hạn của API (Giới hạn trong 5 phút)!");
                break;

            case "97":
                resp.setStatus(400);
                jsonObject.addProperty("code", "97");
                jsonObject.addProperty("message", "Chữ ký không hợp lệ!");
                break;

            case "99":
                resp.setStatus(400);
                jsonObject.addProperty("code", "99");
                jsonObject.addProperty("message", "Lỗi không xác định từ hệ thống VNPAY!");
                break;
        }

        resp.getWriter().write(gson.toJson(jsonObject));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");

        String dataRequest = req.getParameter("data");
        JsonObject jsonObject = new JsonObject();

        try {
            if (StringValidator.isNullOrBlank(dataRequest)) {
                resp.setStatus(400);
                jsonObject.addProperty("code", 400);
                jsonObject.addProperty("message", "Dữ liệu không hợp lệ!");
                resp.getWriter().write(gson.toJson(jsonObject));
                return;
            }

            VnPayResponseDto vnPayResponseDto = gson.fromJson(dataRequest, VnPayResponseDto.class);

            UUID userId = StringConvertor.convertToUUID(vnPayResponseDto.getTxnRef());
            Optional<VnPayTransactionEntity> optionalVnPayTransactionEntity = vnPayTransactionRepository
                    .getByTransactionId(userId);

            Optional<ExternalTransactionEntity> optionalExternalTransactionEntity = externalTransactionRepository
                    .getExternalTransactionByIdType(userId, TransactionConstant.VNPAY);

            if(optionalExternalTransactionEntity.isEmpty() || optionalVnPayTransactionEntity.isEmpty()) {
                resp.setStatus(400);
                jsonObject.addProperty("code", 400);
                jsonObject.addProperty("message", "Không tìm thấy giao dịch!");
                resp.getWriter().write(gson.toJson(jsonObject));
                return;
            }

            ExternalTransactionEntity externalTransactionEntity = optionalExternalTransactionEntity.get();
            if(externalTransactionEntity.getStatus() == TransactionConstant.STATUS_SUCCESSED){
                resp.setStatus(400);
                jsonObject.addProperty("code", 400);
                jsonObject.addProperty("message", "Giao dịch đã thành công không cần cập nhật lại dữ liệu!");
                resp.getWriter().write(gson.toJson(jsonObject));
                return;
            }

            VnPayTransactionEntity vnPayTransactionEntity = optionalVnPayTransactionEntity.get();
            if (vnPayResponseDto.getTransactionStatus().equals("00")) {
                externalTransactionEntity.setStatus(TransactionConstant.STATUS_SUCCESSED);
                updateData(vnPayResponseDto, externalTransactionEntity, vnPayTransactionEntity);

                ServletContext context = getServletContext();
                @SuppressWarnings("unchecked")
                Queue<TransactionQueueDto> transactionQueue = (Queue<TransactionQueueDto>) context
                        .getAttribute("transaction_queue");

                transactionQueue.add(new TransactionQueueDto(externalTransactionEntity.getUserId(), "ADD_AM", externalTransactionEntity.getAmount()));
            } else {
                externalTransactionEntity.setStatus(TransactionConstant.STATUS_FAILED);
                updateData(vnPayResponseDto, externalTransactionEntity, vnPayTransactionEntity);
            }

            resp.setStatus(200);
            jsonObject.addProperty("code", 200);
            jsonObject.addProperty("message", "Cập nhật thành công!");
            resp.getWriter().write(gson.toJson(jsonObject));
        } catch (Exception e) {
            jsonObject.addProperty("code", 500);
            jsonObject.addProperty("message", "Lỗi server!");
            resp.getWriter().write(gson.toJson(jsonObject));
        }
    }

    private void updateData(VnPayResponseDto vnPayResponseDto, ExternalTransactionEntity externalTransactionEntity, VnPayTransactionEntity vnPayTransactionEntity){
        vnPayTransactionEntity.setPayDate(vnPayResponseDto.getPayDate());
        vnPayTransactionEntity.setTransactionNo(vnPayResponseDto.getTransactionNo());
        vnPayTransactionEntity.setBankTransNo(vnPayResponseDto.getTransactionNo());
        externalTransactionRepository.updateStatus(externalTransactionEntity);
        vnPayTransactionRepository.update(vnPayTransactionEntity);
    }
}
