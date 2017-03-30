package object;

import java.util.Date;

/**
 * Created by BenX on 27/03/2017.
 */
public class format {
    public static String addSQLDate(Date date)
    {
        int year = date.getYear() + 1900;
        int month = date.getMonth() + 1;
        int day = date.getDate();
        return  String.format("%04d",year) + "-" + String.format("%02d", month) + "-" + String.format("%02d", day);
    }
    public static String addSQLTime(Date time)
    {
        int hour = time.getHours();
        int minute = time.getMinutes();
        int second = time.getSeconds();
        return  String.format("%02d", hour) + ":" + String.format("%02d", minute) + ":" + String.format("%02d", second);
    }
}
