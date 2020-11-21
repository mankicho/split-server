package util;

import java.util.Calendar;
import java.util.Date;

public class TimeUtil {
    public static long timeUntil(int year, int month, int day, int hour, int minute) {
        Date now = new Date();
        Calendar until = Calendar.getInstance();
        until.set(Calendar.YEAR, year);
        until.set(Calendar.MONTH, month - 1);
        until.set(Calendar.DAY_OF_WEEK, day);
        until.set(Calendar.HOUR_OF_DAY, hour);
        until.set(Calendar.MINUTE, minute);
        until.set(Calendar.SECOND, 0);
        Date u = until.getTime();
        long sleep = u.getTime() - now.getTime();
        return sleep;

    }


}
