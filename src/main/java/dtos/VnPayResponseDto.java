package dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VnPayResponseDto {
    private String responseId;
    private String command;
    private String responseCode;
    private String message;
    private String tmnCode;
    private String txnRef;
    private String amount;
    private String orderInfo;
    private String bankCode;
    private String payDate;
    private String transactionNo;
    private String transactionType;
    private String transactionStatus;
    private String secureHash;
}
