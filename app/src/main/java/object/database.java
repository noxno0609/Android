package object;

import android.util.Log;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

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

     /*
    URL weburl = new URL(url);
    URLConnection urlConnection = weburl.openConnection();

    BufferedReader rd = new BufferedReader(
            new InputStreamReader(urlConnection.getInputStream()));
    */
}

