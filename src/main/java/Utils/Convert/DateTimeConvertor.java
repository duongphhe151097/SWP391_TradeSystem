package Utils.Convert;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class DateTimeConvertor {

    public static Timestamp toSqlTimestamp(LocalDateTime localDateTime) {
        java.sql.Timestamp timestamp = java.sql.Timestamp.valueOf(localDateTime);
        return timestamp;
    }

    public static LocalDateTime toLocalDateTime(java.sql.Timestamp timestamp){
        return timestamp.toLocalDateTime();
    }
}
