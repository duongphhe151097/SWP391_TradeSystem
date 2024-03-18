package utils.constants;

public class ReportConstant {
    public static short USER_REPORT = 1; // báo cáo người dùng
    public static short POST_REPORT = 2; // báo cáo đơn hàng

    public static short REPORT_BUYER_CREATED = 1; //Buyer tạo report
    public static short REPORT_BUYER_ABORT = 2; //Buyer hủy report
    public static short REPORT_BUYER_ACCEPT_SELLER_RESPONSE = 9; //Buyer đồng ý với phản hồi của seller

    public static short REPORT_SELLER_ACCEPT = 3; //Seller đồng ý với report
    public static short REPORT_SELLER_DENIED = 4; //Seller không chấp nhận report

    public static short REPORT_ADMIN_REQUEST = 5; //Yêu cầu admin check
    public static short REPORT_ADMIN_CHECKING = 6; //Admin đang thực hiện check
    public static short REPORT_ADMIN_RESPONSE_BUYER_RIGHT = 7; //Admin phản hồi, buyer report đúng
    public static short REPORT_ADMIN_RESPONSE_BUYER_WRONG = 8; //Admin phản hồi, buyer report sai
}
