package utils.constants;

public class TransactionConstant {
    public static short CASH_IN = 1;
    public static short CASH_OUT = 2;
    public static short REFUND = 3;

    public static short INTERNAL_SUB = 1;
    public static short INTERNAL_ADD = 2;

    public static short STATUS_PROCESSING = 1;
    public static short STATUS_SUCCESSED = 2;
    public static short STATUS_FAILED = 3;

    public static String VNPAY = "VNPAY";
    public static String INTERNAL = "ITN";

    public static long MIN_AMOUNT = 10000L;
    public static long MAX_AMOUNT = 10000000L;

}
