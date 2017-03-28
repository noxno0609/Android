package object.dao;

import object.database;
import object.define;
import object.dto.periodeventdto;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
            JSONArray json = new JSONArray(jsonstr);
            for(int i=0;i< jsonstr.length();i++)
            {
                resultdto.id = json.getJSONObject(i).getInt("id");
                resultdto.userid = json.getJSONObject(i).getInt("UserID");
                resultdto.dayselect = json.getJSONObject(i).getString("DaySelect");
                resultdto.timestart = new SimpleDateFormat().parse(json.getJSONObject(i).getString("TimeStart"));
                resultdto.timeend = new SimpleDateFormat().parse(json.getJSONObject(i).getString("TimeEnd"));
                resultdto.datestart = new SimpleDateFormat().parse(json.getJSONObject(i).getString("DateStart"));
                resultdto.dateend = new SimpleDateFormat().parse(json.getJSONObject(i).getString("DateEnd"));
                resultdto.note = json.getJSONObject(i).getString("Note");
            }
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
                dto.timestart = new SimpleDateFormat().parse(json.getJSONObject(i).getString("TimeStart"));
                dto.timeend = new SimpleDateFormat().parse(json.getJSONObject(i).getString("TimeEnd"));
                dto.datestart = new SimpleDateFormat().parse(json.getJSONObject(i).getString("DateStart"));
                dto.dateend = new SimpleDateFormat().parse(json.getJSONObject(i).getString("DateEnd"));
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
        nameValuePair.add(new BasicNameValuePair("PE-TimeStart",dto.timestart.toString()));
        nameValuePair.add(new BasicNameValuePair("PE-TimeEnd",dto.timeend.toString()));
        nameValuePair.add(new BasicNameValuePair("PE-DateStart",dto.timestart.toString()));
        nameValuePair.add(new BasicNameValuePair("PE-DateEnd",dto.timeend.toString()));
        nameValuePair.add(new BasicNameValuePair("PE-DaySelect",dto.dayselect.toString()));
        nameValuePair.add(new BasicNameValuePair("PE-Note",dto.note.toString()));
        nameValuePair.add(new BasicNameValuePair("PE-UserID",Integer.toString(dto.userid)));

        int newid = database.postMethod_insert(define.DTO.PeriodEvent, nameValuePair);

        return newid;
    }
}
