package youness.automotive.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Youness Teimouri
 * www.youness-teimouri.com
 */
public class DateUtils {
    private static final String DATE_PATTERN = "yyyy-MM-dd";

    public static String format(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_PATTERN);
        return dateFormat.format(date);
    }

    public static Date parse(String datetimeString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_PATTERN);
        try {
            return dateFormat.parse(datetimeString);
        } catch (ParseException e) {
            e.printStackTrace();// TODO log
        }
        return null;
    }
}
