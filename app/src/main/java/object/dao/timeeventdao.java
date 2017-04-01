package object.dao;

import android.util.Log;
import android.widget.Toast;
import object.database;
import object.define;
import object.dto.timeeventdto;
import object.format;
import object.util;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by BenX on 18/03/2017.
 */
public class timeeventdao {
    public static timeeventdto get(int id) {
        String jsonstr = database.getMethod(define.DTO.TimeEvent, id);
        timeeventdto resutldto = new timeeventdto();

        try {
            JSONObject json = new JSONObject(jsonstr);
            resutldto.id = json.getInt("id");
            resutldto.userid = json.getInt("UserID");
            resutldto.timestart = new SimpleDateFormat("HH:mm:ss").parse(json.getString("TimeStart"));
            resutldto.timeend = new SimpleDateFormat("HH:mm:ss").parse(json.getString("TimeEnd"));
            resutldto.dayselect = new SimpleDateFormat("yyyy-MM-dd").parse(json.getString("DaySelect"));
            resutldto.note = json.getString("Note");
            resutldto.pe_id = json.getInt("PE_ID");
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return resutldto;
    }

    public static List<timeeventdto> getall() {
        String jsonstr = database.getMethod(define.DTO.TimeEvent);
        List<timeeventdto> listresutldto = new ArrayList<timeeventdto>();

        try {
            JSONArray json = new JSONArray(jsonstr);
            for (int i = 0; i < json.length(); i++) {
                timeeventdto dto = new timeeventdto();
                dto.id = json.getJSONObject(i).getInt("id");
                dto.userid = json.getJSONObject(i).getInt("UserID");
                dto.timestart = new SimpleDateFormat("HH:mm:ss").parse(json.getJSONObject(i).getString("TimeStart"));
                dto.timeend = new SimpleDateFormat("HH:mm:ss").parse(json.getJSONObject(i).getString("TimeEnd"));
                dto.dayselect = new SimpleDateFormat("yyyy-MM-dd").parse(json.getJSONObject(i).getString("DaySelect"));
                dto.pe_id = json.getJSONObject(i).getInt("PE_ID");
                listresutldto.add(dto);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return listresutldto;
    }

    public static int insert (timeeventdto dto)
    {
        List nameValuePair = new ArrayList(6);
        nameValuePair.add(new BasicNameValuePair("TE-TimeStart", format.addSQLTime(dto.timestart)));
        nameValuePair.add(new BasicNameValuePair("TE-TimeEnd", format.addSQLTime(dto.timeend)));
        nameValuePair.add(new BasicNameValuePair("TE-DaySelect", format.addSQLDate(dto.dayselect)));
        nameValuePair.add(new BasicNameValuePair("TE-Note", dto.note.toString()));
        nameValuePair.add(new BasicNameValuePair("TE-UserID", Integer.toString(dto.userid)));
        nameValuePair.add(new BasicNameValuePair("TE-PE_ID", Integer.toString(dto.pe_id)));

        int newid = database.postMethod(define.DTO.TimeEvent, nameValuePair);
        if (newid > 0)
             dto.id = newid;

        return newid;
    }

    public static  boolean update(timeeventdto dto)
    {
        List nameValuePair = new ArrayList(6);
        nameValuePair.add(new BasicNameValuePair("TE-TimeStart", format.addSQLTime(dto.timestart)));
        nameValuePair.add(new BasicNameValuePair("TE-TimeEnd", format.addSQLTime(dto.timeend)));
        nameValuePair.add(new BasicNameValuePair("TE-DaySelect", format.addSQLDate(dto.dayselect)));
        nameValuePair.add(new BasicNameValuePair("TE-Note", dto.note.toString()));
        nameValuePair.add(new BasicNameValuePair("TE-UserID", Integer.toString(dto.userid)));
        nameValuePair.add(new BasicNameValuePair("TE-PE_ID", Integer.toString(dto.pe_id)));

        int result = database.postMethod(define.DTO.TimeEvent, dto.id, nameValuePair,"PUT");

        return (result == 1) ? true : false;
    }

    public static boolean delete(timeeventdto dto)
    {
        int result = database.postMethod(define.DTO.TimeEvent, dto.id, new ArrayList(1), "DELETE");
        util.deleteDto(dto);
        return (result == 1) ? true : false;
    }
}