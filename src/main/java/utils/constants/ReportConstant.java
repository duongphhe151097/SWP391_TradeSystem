package utils.constants;

public class ReportConstant {
    public static short USER_REPORT = 1;
    public static short POST_REPORT = 2;

    public static short REPORT_CREATED = 1;
    public static short REPORT_PROCESSING = 2;
    public static short REPORT_DONE_CLIENT_RIGHT = 3; //Client report đúng
    public static short REPORT_DONE_CLIENT_WRONG = 4; //Client report sai
    public static short REPORT_ABORT = 5; //Client hủy report
}
