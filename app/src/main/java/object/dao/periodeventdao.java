package object.dao;

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
            resultdto.timestart = new SimpleDateFormat("HH:mm:ss").parse(json.getString("TimeStart"));
            resultdto.timeend = new SimpleDateFormat("HH:mm:ss").parse(json.getString("TimeEnd"));
            resultdto.datestart = new SimpleDateFormat("yyyy-MM-dd").parse(json.getString("DateStart"));
            resultdto.dateend = new SimpleDateFormat("yyyy-MM-dd").parse(json.getString("DateEnd"));
            resultdto.note = json.getString("Note");

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
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
                dto.timestart = new SimpleDateFormat("HH:mm:ss").parse(json.getJSONObject(i).getString("TimeStart"));
                dto.timeend = new SimpleDateFormat("HH:mm:ss").parse(json.getJSONObject(i).getString("TimeEnd"));
                dto.datestart = new SimpleDateFormat("yyyy-MM-dd").parse(json.getJSONObject(i).getString("DateStart"));
                dto.dateend = new SimpleDateFormat("yyyy-MM-dd").parse(json.getJSONObject(i).getString("DateEnd"));
                dto.note = json.getJSONObject(i).getString("Note");
                listresultdto.add(dto);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return listresultdto;
    }

    public static int insert(periodeventdto dto)
    {
        List nameValuePair = new ArrayList(7);
        nameValuePair.add(new BasicNameValuePair("PE-TimeStart", format.addSQLTime(dto.timestart)));
        nameValuePair.add(new BasicNameValuePair("PE-TimeEnd", format.addSQLTime(dto.timeend)));
        nameValuePair.add(new BasicNameValuePair("PE-DateStart", format.addSQLDate(dto.datestart)));
        nameValuePair.add(new BasicNameValuePair("PE-DateEnd", format.addSQLDate(dto.dateend)));
        nameValuePair.add(new BasicNameValuePair("PE-DaySelect",dto.dayselect.toString()));
        nameValuePair.add(new BasicNameValuePair("PE-Note",dto.note.toString()));
        nameValuePair.add(new BasicNameValuePair("PE-UserID",Integer.toString(dto.userid)));

        int newid = database.postMethod(define.DTO.PeriodEvent, nameValuePair);
        if(newid > 0)
            dto.id = newid;

        List<Date> listsdate = util.getSelectDate(dto);
        for(Date sdate : listsdate)
        {
            timeeventdto ndto = new timeeventdto();
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

    public static boolean update(periodeventdto dto)
    {
        List nameValuePair = new ArrayList(8);
        nameValuePair.add(new BasicNameValuePair("PE-TimeStart",format.addSQLTime(dto.timestart)));
        nameValuePair.add(new BasicNameValuePair("PE-TimeEnd",format.addSQLTime(dto.timeend)));
        nameValuePair.add(new BasicNameValuePair("PE-DateStart", format.addSQLDate(dto.datestart)));
        nameValuePair.add(new BasicNameValuePair("PE-DateEnd", format.addSQLDate(dto.dateend)));
        nameValuePair.add(new BasicNameValuePair("PE-DaySelect", dto.dayselect.toString()));
        nameValuePair.add(new BasicNameValuePair("PE-Note", dto.note.toString()));
        nameValuePair.add(new BasicNameValuePair("PE-UserID", Integer.toString(dto.userid)));

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
