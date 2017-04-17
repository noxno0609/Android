package com.example.thienpham.time_remider;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by THIEN PHAM on 04/16/2017.
 */
public class IntroActivity extends AppCompatActivity {
    //Khai bao gia tri
    private Button btDangnhap;
    private Button btDangki;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        btDangnhap = (Button) findViewById(R.id.btDangNhap);
        btDangki = (Button) findViewById(R.id.btDangKy);

        btDangnhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IntroActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        btDangki.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IntroActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });
    }
}

