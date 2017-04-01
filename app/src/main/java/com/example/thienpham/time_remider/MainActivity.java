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
import object.dao.userdao;
import object.database;
import object.dto.periodeventdto;
import object.dto.timeeventdto;
import object.dto.userdto;

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
        btDangkiMain=(Button) findViewById(R.id.btDangkiMain);

        btDangkiMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(database.checkHostConnection() == true) {
                    Intent intent = new Intent(MainActivity.this, SignupActivity.class);
                    startActivity(intent);
                }
                else
                    Toast.makeText(MainActivity.this, "Không thể kết nối tới server!", Toast.LENGTH_SHORT).show();
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
            }
        });
    }

    class LoadData extends AsyncTask<String, String, String>{
        private ProgressDialog progress = new ProgressDialog(MainActivity.this);
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress.setTitle(("Loading"));
            progress.setMessage("Kiểm tra tài khoản...");
            progress.show();
        }

        @Override
        protected String doInBackground(String... params) {

            EditText etuser = (EditText) findViewById(R.id.etUserLogin);
            EditText etpassword = (EditText) findViewById(R.id.etPassLogin);

            List<userdto> listdto = userdao.getall();
            for(userdto dto : listdto)
            {
                if(dto.Name.equals(etuser.getText().toString()) && dto.Password.equals(etpassword.getText().toString()))
                {
                    database.sessionuser = dto;
                    return "Đăng nhập thành công";
                }
            }
            return "Sai tên đăng nhập hoặc mật khẩu";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progress.dismiss();
            Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
            if(database.sessionuser != null)
            {
                Intent intent = new Intent(MainActivity.this, WeekViewAcitivity.class);
                startActivity(intent);
            }
        }
    }
}

