package object;

import object.dao.timeeventdao;
import object.dto.periodeventdto;
import object.dto.timeeventdto;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by BenX on 30/03/2017.
 */
public class util {
    public static void deleteDto(periodeventdto dto)
    {
        dto = new periodeventdto();
    }
    public static void deleteDto(timeeventdto dto)
    {
        dto = new timeeventdto();
    }

    public static List<Date> getSelectDate(periodeventdto dto)
    {
        List<Date> result = new ArrayList<>();
        List<Integer> listsday = getSelectDay(dto.dayselect);

        for (Date date = dto.datestart; date.before(addTime(dto.dateend, 1, define.DAY));
             date = addTime(date, 1, define.DAY))
        {
            int dow = date.getDay();
            if(listsday.contains(dow))
                result.add(date);
        }
        return result;
    }

    public static List<Integer> getSelectDay(String dayselect)
    {
        List<Integer> result = new ArrayList<>();
        for(int i=0;i<dayselect.length();i++)
        {
            if(dayselect.charAt(i) == '1')
                result.add(i);
        }
        return result;
    }

    public static Date addTime(Date date, int number, int type)
    {
        Calendar c = Calendar.getInstance();
        c.setTime(date);

        switch(type)
        {
            case define.YEAR:
            {
                c.add(Calendar.YEAR, number);
                break;
            }
            case define.MONTH:
            {
                c.add(Calendar.MONTH, number);
                break;
            }
            case define.DAY:
            {
                c.add(Calendar.DATE, number);
                break;
            }
            case define.HOUR:
            {
                c.add(Calendar.HOUR, number);
                break;
            }
            case define.MINUTE:
            {
                c.add(Calendar.MINUTE, number);
                break;
            }
            case define.SECOND:
            {
                c.add(Calendar.SECOND, number);
                break;
            }
        }
        // convert calendar to date
        return c.getTime();
    }
}
