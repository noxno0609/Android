package object;

import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import object.dto.periodeventdto;
import object.dto.timeeventdto;
import object.dto.userdto;
import org.apache.commons.httpclient.util.HttpURLConnection;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by BenX on 18/03/2017.
 */
public class database {

    public static final String connIP = "192.168.56.1";
    public static final String connPort = "85";
    public static final String connString = connIP + ":" + connPort;
    public static userdto sessionuser = null;

    public static boolean checkServerAvailable() {
        Socket s = new Socket();

        try {
            s.connect(new InetSocketAddress(connIP, Integer.parseInt(connPort)), 2000);
        } catch (IOException e) {
            return false;
        }

        try {
            if (s.isConnected())
            { s.close();
            }
            else
            {
                s=null;
                return false;
            }
        }
        catch (UnknownHostException e)
        { // unknown host
            s = null;
            return false;
        }
        catch (IOException e) { // io exception, service probably not running
            s = null;
            return false;
        }
        catch (NullPointerException e) {
            s = null;
            return false;
        }
        return true;
    }

    public static String getMethod(int type) {

        String url = "";
        if(type == define.DTO.TimeEvent)
            url = "http://" + database.connString + "/TimeReminderWS/public/TimeEvent";
        else if(type == define.DTO.PeriodEvent)
            url = "http://" + database.connString + "/TimeReminderWS/public/PeriodEvent";
        else if(type == define.DTO.User)
            url = "http://" + database.connString + "/TimeReminderWS/public/User";

        StringBuffer result = new StringBuffer();
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet request = new HttpGet(url);

            HttpResponse response = httpclient.execute(request);

            BufferedReader rd = new BufferedReader(
                    new InputStreamReader(response.getEntity().getContent()));

            String line;
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            rd.close();

        }
        catch(IOException ex)
        {
            Log.e("Bug", ex.getMessage());
            ex.printStackTrace();
        }
        return result.toString();
    }

    public static String getMethod(int type, int id) {

        String url = "";
        if(type == define.DTO.TimeEvent)
            url = "http://" + database.connString + "/TimeReminderWS/public/TimeEvent/" + id;
        else if(type == define.DTO.PeriodEvent)
            url = "http://" + database.connString + "/TimeReminderWS/public/PeriodEvent/" + id;
        else if(type == define.DTO.User)
            url = "http://" + database.connString + "/TimeReminderWS/public/User";

        StringBuffer result = new StringBuffer();
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet request = new HttpGet(url);

            HttpResponse response = httpclient.execute(request);

            BufferedReader rd = new BufferedReader(
                    new InputStreamReader(response.getEntity().getContent()));

            String line;
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            rd.close();
        }
        catch(IOException ex)
        {
            Log.e("Bug", ex.getMessage());
            ex.printStackTrace();
        }
        return result.toString();
    }

    public static int postMethod(int type, List nameValuePair)
    {
        String url = "";
        if(type == define.DTO.TimeEvent)
            url = "http://" + database.connString + "/TimeReminderWS/public/TimeEvent";
        else if(type == define.DTO.PeriodEvent)
            url = "http://" + database.connString + "/TimeReminderWS/public/PeriodEvent";
        else if(type == define.DTO.User)
            url = "http://" + database.connString + "/TimeReminderWS/public/User";

        //khởi tạo giao thức http phái Client
        HttpClient httpClient = new DefaultHttpClient();

        //Giao thức Post bằng việc lấy URL để nhận Request
        HttpPost httpPost = new HttpPost(url);

        //Encoding dữ liệu khi POST
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String result = "";
        try {
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            result = EntityUtils.toString(entity);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            return 0;
        }
        return 1;
    }

    public static int postMethod(int type, int id, List nameValuePair, String posttype)
    {
        String url = "";
        if(type == define.DTO.TimeEvent)
            url = "http://" + database.connString + "/TimeReminderWS/public/TimeEvent/" + id;
        else if(type == define.DTO.PeriodEvent)
            url = "http://" + database.connString + "/TimeReminderWS/public/PeriodEvent/" + id;
        else if(type == define.DTO.User)
            url = "http://" + database.connString + "/TimeReminderWS/public/User/" + id;

        //khởi tạo giao thức http phái Client
        HttpClient httpClient = new DefaultHttpClient();

        //Giao thức Post bằng việc lấy URL để nhận Request
        HttpPost httpPost = new HttpPost(url);
        nameValuePair.add(new BasicNameValuePair("_method", posttype));

        //Encoding dữ liệu khi POST
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String result = "";
        try {
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            result = EntityUtils.toString(entity);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            return 0;
        }
        return 1;
    }
}
