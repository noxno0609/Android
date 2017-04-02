package com.example.thienpham.time_remider;

import android.app.Activity;
import android.content.Intent;
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

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.*;

import static android.view.ViewGroup.LayoutParams.FILL_PARENT;

public class MainLichTrinhActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //onCreate chỉ xử lý trên giao diện
        //Lấy thông tin trong Asynctask
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_lichtrinh);
        showsessionDTO();

        Button btback = (Button) findViewById(R.id.btBack);
        btback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainLichTrinhActivity.this, WeekViewAcitivity.class);
                startActivity(intent);
            }
        });
        Button btadd = (Button) findViewById(R.id.btltAdd);
        btadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainLichTrinhActivity.this, LichtrinhActivity.class);
                startActivity(intent);
            }
        });
    }

    public void showsessionDTO()
    {
        class LichTrinhWork extends AsyncTask<Void, Void, List<String>>
        {
            public List<periodeventdto> dtohasinserted = new ArrayList<>();

            @Override
            protected List<String> doInBackground(Void... params) {
                List<periodeventdto> listdto = periodeventdao.getall();

                List<String> listsource = new ArrayList<>();

                for(periodeventdto dto : listdto)
                {
                    if(dto.userid == database.sessionuser.id)
                    {
                        listsource.add("Lịch trình: " + dto.note + "\n" + util.readDate(dto.datestart) + " - " + util.readDate(dto.dateend));
                        dtohasinserted.add(dto);
                    }
                }
                return listsource;
            }

            @Override
            protected void onPostExecute(List<String> listsource) {
                super.onPostExecute(listsource);

                ArrayAdapter<String> adapter = new ArrayAdapter<String>
                        (MainLichTrinhActivity.this, android.R.layout.simple_list_item_1, listsource);

                ListView lv = (ListView) findViewById(R.id.lvlichtrinh);
                lv.setAdapter(adapter);

                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        periodeventdto dto = dtohasinserted.get(position);

                        Intent intent = new Intent(MainLichTrinhActivity.this, LichtrinhActivity.class);
                        intent.putExtra("dto", dto);
                        startActivity(intent);
                    }
                });
            }
        }
        new LichTrinhWork().execute();
    }
}
//Lấy sứ liệu Periodevent
