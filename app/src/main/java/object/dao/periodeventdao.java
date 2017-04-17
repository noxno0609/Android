package object.dao;

import com.example.thienpham.time_remider.MocAcitivty;
import object.database;
import object.define;
import object.dto.periodeventdto;
import object.dto.timeeventdto;
import object.format;
import object.util;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import static com.example.thienpham.time_remider.MocAcitivty.checkDupplicateDate;
import static com.example.thienpham.time_remider.MocAcitivty.splitdto;

/**
 * Created by BenX on 27/03/2017.
 */
public class periodeventdao {
    public static periodeventdto get(int id)
    {
        String jsonstr = database.getMethod(define.DTO.PeriodEvent, id);
        periodeventdto resultdto = new periodeventdto();

        try {
            JSONObject json = new JSONObject (jsonstr);
            resultdto.id = json.getInt("id");
            resultdto.userid = json.getInt("UserID");
            resultdto.dayselect = json.getString("DaySelect");
            resultdto.timestart = format.parseTime(json.getString("TimeStart"));
            resultdto.timeend = format.parseTime(json.getString("TimeEnd"));
            resultdto.datestart = format.parseDate(json.getString("DateStart"));
            resultdto.dateend = format.parseDate(json.getString("DateEnd"));
            resultdto.note = json.getString("Note");
            resultdto.name = json.getString("Name");
            resultdto.textcolor = json.getString("TextColor");
            resultdto.bgcolor = json.getString("BGColor");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resultdto;
    }

    public static List<periodeventdto> getall()
    {
        String jsonstr = database.getMethod(define.DTO.PeriodEvent);
        List<periodeventdto> listresultdto = new ArrayList<periodeventdto>();

        try {
            JSONArray json = new JSONArray(jsonstr);
            for(int i=0;i< jsonstr.length();i++)
            {
                periodeventdto dto = new periodeventdto();
                dto.id = json.getJSONObject(i).getInt("id");
                dto.userid = json.getJSONObject(i).getInt("UserID");
                dto.dayselect = json.getJSONObject(i).getString("DaySelect");
                dto.timestart = format.parseTime(json.getJSONObject(i).getString("TimeStart"));
                dto.timeend = format.parseTime(json.getJSONObject(i).getString("TimeEnd"));
                dto.datestart = format.parseDate(json.getJSONObject(i).getString("DateStart"));
                dto.dateend = format.parseDate(json.getJSONObject(i).getString("DateEnd"));
                dto.note = json.getJSONObject(i).getString("Note");
                dto.name = json.getJSONObject(i).getString("Name");
                dto.textcolor = json.getJSONObject(i).getString("TextColor");
                dto.bgcolor = json.getJSONObject(i).getString("BGColor");
                listresultdto.add(dto);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return listresultdto;
    }

    public static int insertcondition(periodeventdto dto, boolean overwrite)
    {
        List nameValuePair = new ArrayList(7);
        nameValuePair.add(new BasicNameValuePair("PE-TimeStart", format.addSQLTime(dto.timestart)));
        nameValuePair.add(new BasicNameValuePair("PE-TimeEnd", format.addSQLTime(dto.timeend)));
        nameValuePair.add(new BasicNameValuePair("PE-DateStart", format.addSQLDate(dto.datestart)));
        nameValuePair.add(new BasicNameValuePair("PE-DateEnd", format.addSQLDate(dto.dateend)));
        nameValuePair.add(new BasicNameValuePair("PE-DaySelect",dto.dayselect.toString().trim()));
        nameValuePair.add(new BasicNameValuePair("PE-Note",dto.note.toString()));
        nameValuePair.add(new BasicNameValuePair("PE-Name", dto.name.toString()));
        nameValuePair.add(new BasicNameValuePair("PE-UserID",Integer.toString(dto.userid)));
        nameValuePair.add(new BasicNameValuePair("PE-TextColor", dto.textcolor.toString()));
        nameValuePair.add(new BasicNameValuePair("PE-BGColor", dto.bgcolor.toString()));

        int newid = database.postMethod(define.DTO.PeriodEvent, nameValuePair);
        if(newid > 0)
            dto.id = newid;

        List<timeeventdto> listdto = timeeventdao.getall();
        List<Date> listsdate = util.getSelectDate(dto);
        for(Date sdate : listsdate)
        {
            timeeventdto ndto = new timeeventdto();
            ndto.bgcolor = dto.bgcolor;
            ndto.textcolor = dto.textcolor;
            ndto.note = dto.note;
            ndto.timestart = dto.timestart;
            ndto.timeend = dto.timeend;
            ndto.userid = dto.userid;
            ndto.pe_id = dto.id;
            ndto.dayselect = sdate;
            timeeventdao.insert(ndto);

            for(timeeventdto tedto : listdto)
            {
                if(ndto.dayselect.equals((tedto.dayselect)))
                {
                    int checkcase = checkDupplicateDate(ndto.timestart, ndto.timeend, tedto.timestart, tedto.timeend);

                    if(checkcase  == define.COMPDATE.SAME)
                    {
                        if(overwrite == true) timeeventdao.delete(tedto);
                        else continue;
                    }
                    else if(checkcase == define.COMPDATE.INSIDEA)
                        splitdto(ndto, tedto, 1, true);
                    else if(checkcase == define.COMPDATE.INSIDEB)
                        splitdto(ndto, tedto, 1, false);
                    else if(checkcase == define.COMPDATE.BEFOREA)
                        splitdto(ndto, tedto, 2, true);
                    else if(checkcase == define.COMPDATE.BEFOREB)
                        splitdto(ndto, tedto, 2, false);
                    else if(checkcase == define.COMPDATE.SMEETA)
                        splitdto(ndto, tedto, 3, false);
                    else if(checkcase == define.COMPDATE.SMEETB)
                        splitdto(ndto, tedto, 3, true);
                    else if(checkcase == define.COMPDATE.EMEETA)
                        splitdto(ndto, tedto, 4, false);
                    else if(checkcase == define.COMPDATE.EMEETB)
                        splitdto(ndto, tedto, 4, true);
                    else if(checkcase == define.COMPDATE.OUT) continue;
                    timeeventdao.update(ndto);
                }
            }
        }

        return newid;
    }

    public static int insert(periodeventdto dto)
    {
        List nameValuePair = new ArrayList(7);
        nameValuePair.add(new BasicNameValuePair("PE-TimeStart", format.addSQLTime(dto.timestart)));
        nameValuePair.add(new BasicNameValuePair("PE-TimeEnd", format.addSQLTime(dto.timeend)));
        nameValuePair.add(new BasicNameValuePair("PE-DateStart", format.addSQLDate(dto.datestart)));
        nameValuePair.add(new BasicNameValuePair("PE-DateEnd", format.addSQLDate(dto.dateend)));
        nameValuePair.add(new BasicNameValuePair("PE-DaySelect",dto.dayselect.toString().trim()));
        nameValuePair.add(new BasicNameValuePair("PE-Note",dto.note.toString()));
        nameValuePair.add(new BasicNameValuePair("PE-Name", dto.name.toString()));
        nameValuePair.add(new BasicNameValuePair("PE-UserID",Integer.toString(dto.userid)));
        nameValuePair.add(new BasicNameValuePair("PE-TextColor", dto.textcolor.toString()));
        nameValuePair.add(new BasicNameValuePair("PE-BGColor", dto.bgcolor.toString()));

        int newid = database.postMethod(define.DTO.PeriodEvent, nameValuePair);
        if(newid > 0)
            dto.id = newid;

        List<Date> listsdate = util.getSelectDate(dto);
        for(Date sdate : listsdate)
        {
            timeeventdto ndto = new timeeventdto();
            ndto.bgcolor = dto.bgcolor;
            ndto.textcolor = dto.textcolor;
            ndto.note = dto.note;
            ndto.timestart = dto.timestart;
            ndto.timeend = dto.timeend;
            ndto.userid = dto.userid;
            ndto.pe_id = dto.id;
            ndto.dayselect = sdate;
            timeeventdao.insert(ndto);
        }

        return newid;
    }

    public static boolean updatecondition(periodeventdto dto, boolean overwrite)
    {
        List nameValuePair = new ArrayList(10);
        nameValuePair.add(new BasicNameValuePair("PE-TimeStart",format.addSQLTime(dto.timestart)));
        nameValuePair.add(new BasicNameValuePair("PE-TimeEnd",format.addSQLTime(dto.timeend)));
        nameValuePair.add(new BasicNameValuePair("PE-DateStart", format.addSQLDate(dto.datestart)));
        nameValuePair.add(new BasicNameValuePair("PE-DateEnd", format.addSQLDate(dto.dateend)));
        nameValuePair.add(new BasicNameValuePair("PE-DaySelect", dto.dayselect.toString()));
        nameValuePair.add(new BasicNameValuePair("PE-Note", dto.note.toString()));
        nameValuePair.add(new BasicNameValuePair("PE-Name", dto.name.toString()));
        nameValuePair.add(new BasicNameValuePair("PE-UserID", Integer.toString(dto.userid)));
        nameValuePair.add(new BasicNameValuePair("PE-TextColor", dto.textcolor.toString()));
        nameValuePair.add(new BasicNameValuePair("PE-BGColor", dto.bgcolor.toString()));

        int result = database.postMethod(define.DTO.PeriodEvent, dto.id, nameValuePair, "PUT");

        List<timeeventdto> listdto = timeeventdao.getall();
        List<Date> listsdate = util.getSelectDate(dto);
        List<Date> listinsdate = new ArrayList<>();

        for (timeeventdto tdto : listdto)
        {
            if(tdto.pe_id == dto.id)
            {
                if(listsdate.contains(tdto.dayselect))
                {
                    tdto.timestart = dto.timestart;
                    tdto.timeend = dto.timeend;
                    tdto.note = dto.note;
                    timeeventdao.update(tdto);
                    listinsdate.add(tdto.dayselect);
                }
                else timeeventdao.delete(tdto);
            }
        }

        for(Date sdate : listsdate)
        {
            if(!listinsdate.contains(sdate))
            {
                timeeventdto ndto = new timeeventdto();
                ndto.note = dto.note;
                ndto.timestart = dto.timestart;
                ndto.timeend = dto.timeend;
                ndto.userid = dto.userid;
                ndto.pe_id = dto.id;
                ndto.dayselect = sdate;
                ndto.bgcolor = dto.bgcolor;
                ndto.textcolor = dto.textcolor;
                timeeventdao.insert(ndto);

                for(timeeventdto tedto : listdto)
                {
                    if(ndto.dayselect.equals((tedto.dayselect)))
                    {
                        int checkcase = checkDupplicateDate(ndto.timestart, ndto.timeend, tedto.timestart, tedto.timeend);

                        if(checkcase  == define.COMPDATE.SAME)
                        {
                            if(overwrite == true) timeeventdao.delete(tedto);
                            else continue;
                        }
                        else if(checkcase == define.COMPDATE.INSIDEA)
                            splitdto(ndto, tedto, 1, true);
                        else if(checkcase == define.COMPDATE.INSIDEB)
                            splitdto(ndto, tedto, 1, false);
                        else if(checkcase == define.COMPDATE.BEFOREA)
                            splitdto(ndto, tedto, 2, true);
                        else if(checkcase == define.COMPDATE.BEFOREB)
                            splitdto(ndto, tedto, 2, false);
                        else if(checkcase == define.COMPDATE.SMEETA)
                            splitdto(ndto, tedto, 3, false);
                        else if(checkcase == define.COMPDATE.SMEETB)
                            splitdto(ndto, tedto, 3, true);
                        else if(checkcase == define.COMPDATE.EMEETA)
                            splitdto(ndto, tedto, 4, false);
                        else if(checkcase == define.COMPDATE.EMEETB)
                            splitdto(ndto, tedto, 4, true);
                        else if(checkcase == define.COMPDATE.OUT) continue;
                        timeeventdao.update(ndto);
                    }
                }
            }
        }
        return (result == 1) ? true : false;
    }

    public static boolean update(periodeventdto dto)
    {
        List nameValuePair = new ArrayList(10);
        nameValuePair.add(new BasicNameValuePair("PE-TimeStart",format.addSQLTime(dto.timestart)));
        nameValuePair.add(new BasicNameValuePair("PE-TimeEnd",format.addSQLTime(dto.timeend)));
        nameValuePair.add(new BasicNameValuePair("PE-DateStart", format.addSQLDate(dto.datestart)));
        nameValuePair.add(new BasicNameValuePair("PE-DateEnd", format.addSQLDate(dto.dateend)));
        nameValuePair.add(new BasicNameValuePair("PE-DaySelect", dto.dayselect.toString()));
        nameValuePair.add(new BasicNameValuePair("PE-Note", dto.note.toString()));
        nameValuePair.add(new BasicNameValuePair("PE-Name", dto.name.toString()));
        nameValuePair.add(new BasicNameValuePair("PE-UserID", Integer.toString(dto.userid)));
        nameValuePair.add(new BasicNameValuePair("PE-TextColor", dto.textcolor.toString()));
        nameValuePair.add(new BasicNameValuePair("PE-BGColor", dto.bgcolor.toString()));

        int result = database.postMethod(define.DTO.PeriodEvent, dto.id, nameValuePair, "PUT");

        updatetimeevent(dto);
        return (result == 1) ? true : false;
    }

    public static void updatetimeevent(periodeventdto dto)
    {
        List<timeeventdto> listdto = timeeventdao.getall();
        List<Date> listsdate = util.getSelectDate(dto);
        List<Date> listinsdate = new ArrayList<>();

        for (timeeventdto tdto : listdto)
        {
            if(tdto.pe_id == dto.id)
            {
                if(listsdate.contains(tdto.dayselect))
                {
                    tdto.timestart = dto.timestart;
                    tdto.timeend = dto.timeend;
                    tdto.note = dto.note;
                    timeeventdao.update(tdto);
                    listinsdate.add(tdto.dayselect);
                }
                else timeeventdao.delete(tdto);
            }
        }

        for(Date sdate : listsdate)
        {
            if(!listinsdate.contains(sdate))
            {
                timeeventdto ndto = new timeeventdto();
                ndto.note = dto.note;
                ndto.timestart = dto.timestart;
                ndto.timeend = dto.timeend;
                ndto.userid = dto.userid;
                ndto.pe_id = dto.id;
                ndto.dayselect = sdate;
                ndto.bgcolor = dto.bgcolor;
                ndto.textcolor = dto.textcolor;
                timeeventdao.insert(ndto);
            }
        }
    }

    public static boolean delete(periodeventdto dto)
    {
        int result = database.postMethod(define.DTO.PeriodEvent, dto.id, new ArrayList(1), "DELETE");

        List<timeeventdto> listdto = timeeventdao.getall();
        for(timeeventdto tdto : listdto)
        {
            if(tdto.pe_id == dto.id)
                timeeventdao.delete(tdto);
        }

        util.deleteDto(dto);
        return (result == 1) ? true : false;
    }
}
