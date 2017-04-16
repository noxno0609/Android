package object.dao;

import object.database;
import object.define;
import object.dto.periodeventdto;
import object.dto.timeeventdto;
import object.dto.userdto;
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
import java.util.List;

/**
 * Created by BenX on 01/04/2017.
 */
public class userdao {
    public static userdto get(int id)
    {
        String jsonstr = database.getMethod(define.DTO.User, id);
        userdto resultdto = new userdto();

        try {
            JSONObject json = new JSONObject (jsonstr);
            resultdto.id = json.getInt("ID");
            resultdto.Name = json.getString("Name");
            resultdto.Password = json.getString("Password");
            resultdto.Email = json.getString("Email");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resultdto;
    }

    public static List<userdto> getall()
    {
        String jsonstr = database.getMethod(define.DTO.User);
        List<userdto> listresultdto = new ArrayList<userdto>();
        try {
            JSONArray json = new JSONArray(jsonstr);
            for(int i=0;i< jsonstr.length();i++)
            {
                userdto dto = new userdto();
                dto.id = json.getJSONObject(i).getInt("id");
                dto.Name = json.getJSONObject(i).getString("Name");
                dto.Password = json.getJSONObject(i).getString("Password");
                dto.Email = json.getJSONObject(i).getString("Email");
                dto.Phone = json.getJSONObject(i).getString("Phone");
                listresultdto.add(dto);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return listresultdto;
    }

    public static int insert(userdto dto)
    {
        List nameValuePair = new ArrayList(7);
        nameValuePair.add(new BasicNameValuePair("US-Name", dto.Name.toString()));
        nameValuePair.add(new BasicNameValuePair("US-Password", dto.Password.toString()));
        nameValuePair.add(new BasicNameValuePair("US-Email", dto.Email.toString()));;
        nameValuePair.add(new BasicNameValuePair("US-Phone", dto.Phone.toString()));;

        int newid = database.postMethod(define.DTO.User, nameValuePair);
        if(newid > 0)
            dto.id = newid;

        return newid;
    }
}
