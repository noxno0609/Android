package com.example.thienpham.time_remider;

import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.*;
import object.dao.periodeventdao;
import object.dao.timeeventdao;
import object.database;
import object.dto.periodeventdto;
import object.dto.timeeventdto;
import object.util;

import java.util.*;

import static android.view.ViewGroup.LayoutParams.FILL_PARENT;

public class MainLichTrinhActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //onCreate chỉ xử lý trên giao diện
        //Lấy thông tin trong Asynctask
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_lichtrinh);
    }
    public void showsessionDTO()
    {
        class LichTrinhWork extends AsyncTask<Void, Void, List<Integer>>
        {
            Hashtable btMap = new Hashtable();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

            }

            @Override
            protected List<Integer> doInBackground(Void... params) {
                List<periodeventdto> listdto = periodeventdao.getall();
                List<String> liststring = new ArrayList<>();

                for(periodeventdto dto : listdto)
                {
                    if(dto.userid == database.sessionuser.id)
                    {
                        //Lấy thông tin
                    }
                }

                return null;
            }

            @Override
            protected void onPostExecute(List<Integer> listinsindex) {
                super.onPostExecute(listinsindex);

            }
        }
        new LichTrinhWork().execute();
    }

}
//Lấy sứ liệu Periodevent
