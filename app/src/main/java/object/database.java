package object;

import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by BenX on 18/03/2017.
 */
public class database {

    public static final String connIP = "192.168.1.40";

    public static String getMethod() {

        String url = "http://" + database.connIP + "/TimeReminderWS/public/TimeEvent";

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

    public static String getMethod(int id) {

        String url = "http://" + database.connIP + "/TimeReminderWS/public/TimeEvent/" + id;

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

    public static String postMethod_insert(EditText a, EditText b, EditText c, EditText d)
    {
        //tạo URL dẫn tới Database
        String url = "http://" + database.connIP + "/TimeReminderWS/public/TimeEvent";

        //khởi tạo giao thức http phái Client
        HttpClient httpClient = new DefaultHttpClient();

        //Giao thức Post bằng việc lấy URL để nhận Request
        HttpPost httpPost = new HttpPost(url);

        //Các tham số cần truyền
        List nameValuePair = new ArrayList(4);
        nameValuePair.add(new BasicNameValuePair("timestart",a.getText().toString()));
        nameValuePair.add(new BasicNameValuePair("timeend",b.getText().toString()));
        nameValuePair.add(new BasicNameValuePair("dayselect",c.getText().toString()));
        nameValuePair.add(new BasicNameValuePair("note",d.getText().toString()));

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
            e.printStackTrace();
        }
        return result;
    }
     /*
    URL weburl = new URL(url);
    URLConnection urlConnection = weburl.openConnection();

    BufferedReader rd = new BufferedReader(
            new InputStreamReader(urlConnection.getInputStream()));
    */
}
