package Utils.Validation;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class TimeValidator {

    public static boolean isWithinDay(LocalDateTime input){
        LocalTime time = input.toLocalTime();

        LocalTime startOfDay = LocalTime.of(0, 0, 0);
        LocalTime endOfDay = LocalTime.of(23, 59, 59);

        return !time.isBefore(startOfDay) && !time.isAfter(endOfDay);
    }
}
