package Models;

import Models.Common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

@Entity(name = "vnPayTrans")
@Table(name = "vnpay_transaction")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class VnPayTransactionEntity extends BaseEntity implements Serializable {
    @Id
    @Column(name = "`id`", columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "`transaction_id`", columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID transactionId;

    @Column(name = "`type`", updatable = false, nullable = false, length = 20)
    private String type;

    @Column(name = "`version`", updatable = false, nullable = false, length = 8)
    private String version;

    @Column(name = "`command`", updatable = false, nullable = false, length = 16)
    private String command;

    @Column(name = "`amount`", updatable = false, nullable = false)
    private BigDecimal amount;

    @Column(name = "`current_code`", updatable = false, nullable = false, length = 3)
    private String currentCode;

    @Column(name = "`bank_code`", updatable = false, length = 10)
    private String bankCode;

    @Column(name = "`locale`", updatable = false, nullable = false, length = 5)
    private String locale;

    @Column(name = "`ip_addr`", updatable = false, nullable = false, length = 45)
    private String ipAddr;

    @Column(name = "`order_info`", updatable = false, nullable = false)
    private String orderInfo;

    @Column(name = "`order_type`", updatable = false, length = 100)
    private String orderType;

    @Column(name = "`create_date`", updatable = false, nullable = false, length = 14)
    private String createDate;

    @Column(name = "`expire_date`", updatable = false, length = 14)
    private String expireDate;

    @Column(name = "`pay_date`", length = 14)
    private String payDate;

    @Column(name = "`card_type`", length = 20)
    private String cardType;

    @Column(name = "`banktrans_no`", length = 20)
    private String bankTransNo;

    @Column(name = "`secure_hash`", updatable = false, nullable = false, length = 256)
    private String secureHash;

    @Column(name = "`transaction_no`", nullable = false, length = 15)
    private String transactionNo;
}
