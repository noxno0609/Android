package com.example.thienpham.time_remider;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import static android.R.id.edit;

public class MainActivity extends AppCompatActivity {
    //Khai bao gia tri
    private Button btDangnhap,btDangkiMain;

    @Override
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
                Intent intent = new Intent(MainActivity.this, ShowActivity.class);
                startActivity(intent);
                Toast.makeText(MainActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
            }
        });

    }
}

