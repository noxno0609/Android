package object;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

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

    public static Date parseTime(String time)
    {
        SimpleDateFormat sf = new SimpleDateFormat("HH:mm");

        Date tmp = null;
        try {
            tmp = sf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return tmp;
    }

    public static Date parseDate(String date)
    {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");

        Date tmp = null;
        try {
            tmp = sf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(tmp);
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        tmp = cal.getTime();

        return tmp;
    }
}
