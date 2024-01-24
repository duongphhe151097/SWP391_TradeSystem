package Utils.Convert;

import Utils.Validation.StringValidator;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateTimeConvertor {

    public static Timestamp toSqlTimestamp(LocalDateTime localDateTime) {
        java.sql.Timestamp timestamp = java.sql.Timestamp.valueOf(localDateTime);
        return timestamp;
    }

    public static LocalDateTime toLocalDateTime(java.sql.Timestamp timestamp){
        return timestamp.toLocalDateTime();
    }

    public static Date toDate(LocalDateTime input){
        return Date.from(input.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static String formatLocalDateTime(LocalDateTime localDateTime, String pattern) {
        return localDateTime.format(DateTimeFormatter.ofPattern(pattern));
    }

    public static LocalDateTime toLocalDateTime(String input){
        input = input.trim();
        String pattern = "yyyy-MM-dd HH:mm:ss";
        if(!StringValidator.isValidDateFormat(input)){
            input = input+" 00:00:00";
        }

        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);

        return LocalDateTime.parse(input, formatter);
    }

}
