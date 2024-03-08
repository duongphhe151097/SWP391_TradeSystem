package utils.gsoncustom;

import com.google.gson.*;
import dtos.VnPayResponseDto;
import utils.convert.StringConvertor;

import java.lang.reflect.Type;

public class VnPayResponseDeserializer implements JsonDeserializer<VnPayResponseDto> {
    @Override
    public VnPayResponseDto deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        String responseId = StringConvertor.getStringOrNull(jsonObject, "vnp_ResponseId");
        String command = StringConvertor.getStringOrNull(jsonObject, "vnp_Command");
        String responseCode = StringConvertor.getStringOrNull(jsonObject,"vnp_ResponseCode");
        String message = StringConvertor.getStringOrNull(jsonObject,"vnp_Message");
        String tmnCode = StringConvertor.getStringOrNull(jsonObject,"vnp_TmnCode");
        String txnRef = StringConvertor.getStringOrNull(jsonObject,"vnp_TxnRef");
        String amount = StringConvertor.getStringOrNull(jsonObject,"vnp_Amount");
        String orderInfo = StringConvertor.getStringOrNull(jsonObject,"vnp_OrderInfo");
        String bankCode = StringConvertor.getStringOrNull(jsonObject,"vnp_BankCode");
        String payDate = StringConvertor.getStringOrNull(jsonObject,"vnp_PayDate");
        String transactionNo = StringConvertor.getStringOrNull(jsonObject,"vnp_TransactionNo");
        String transactionType = StringConvertor.getStringOrNull(jsonObject,"vnp_TransactionType");
        String transactionStatus = StringConvertor.getStringOrNull(jsonObject,"vnp_TransactionStatus");
        String secureHash = StringConvertor.getStringOrNull(jsonObject,"vnp_SecureHash");

        return VnPayResponseDto.builder()
                .responseId(responseId)
                .command(command)
                .responseCode(responseCode)
                .message(message)
                .tmnCode(tmnCode)
                .txnRef(txnRef)
                .amount(amount)
                .orderInfo(orderInfo)
                .bankCode(bankCode)
                .payDate(payDate)
                .transactionNo(transactionNo)
                .transactionType(transactionType)
                .transactionStatus(transactionStatus)
                .secureHash(secureHash)
                .build();
    }
}
