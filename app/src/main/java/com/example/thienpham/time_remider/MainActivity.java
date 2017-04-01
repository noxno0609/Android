package com.example.thienpham.time_remider;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import object.dao.periodeventdao;
import object.dao.timeeventdao;
import object.dto.periodeventdto;
import object.dto.timeeventdto;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static android.R.id.edit;

public class MainActivity extends AppCompatActivity {
    //Khai bao gia tri
    private Button btDangnhap,btDangkiMain;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Anh xa actitivy_main.xml
        btDangnhap=(Button) findViewById(R.id.btDangnhap);
        btDangkiMain=(Button) findViewById(R.id.btDangkiMain) ;

        btDangkiMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, WeekViewAcitivity.class);
                startActivity(intent);
            }
        });
        btDangnhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        new LoadData().execute();
                    }
                });

                Intent intent = new Intent(MainActivity.this, ShowActivity.class);
                startActivity(intent);
                Toast.makeText(MainActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
            }
        });
    }
    class LoadData extends AsyncTask<String, String, String>{
        private ProgressDialog progress = new ProgressDialog(MainActivity.this);
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress.setTitle(("Loading"));
            progress.setMessage("Lấy dữ liệu...");
            progress.show();
        }

        @Override
        protected String doInBackground(String... params) {
            periodeventdto dto = new periodeventdto();
            dto.dateend = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            try {
                dto.datestart = sdf.parse("21/03/2017");
            } catch (ParseException e) {
                e.printStackTrace();
            }
            dto.timestart = new Date();
            dto.timeend = new Date();
            dto.userid = 99;
            dto.dayselect = "1010001";
            dto.note = "Ahihi";
            int newid = periodeventdao.insert(dto);

            dto.note = "Edited";
            dto.dayselect = "1100000";
            boolean result = periodeventdao.update(dto);

            boolean result2 = periodeventdao.delete(dto);

            progress.dismiss();

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }
}

