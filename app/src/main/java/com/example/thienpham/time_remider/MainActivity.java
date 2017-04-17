package com.example.thienpham.time_remider;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import object.dao.userdao;
import object.database;
import object.dto.userdto;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    //Khai bao gia tri
    private Button btDangnhap,btDangkiMain;
    private TextView tvBack;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Anh xa actitivy_main.xml
        btDangnhap=(Button) findViewById(R.id.btDangnhap);

        tvBack = (TextView) findViewById(R.id.tvBackmain);
        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, IntroActivity.class);
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

            if(database.checkServerAvailable() != true)
            {
                return "Không thể kết nối tới server!";
            }

            EditText etuser = (EditText) findViewById(R.id.etUserLogin);
            EditText etpassword = (EditText) findViewById(R.id.etPassLogin);

            if(etuser.getText().toString().equals(""))
                return "Chưa nhập tên đăng nhập";
            if (etpassword.getText().toString().equals(""))
                return "Chưa nhập mật khẩu";

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

