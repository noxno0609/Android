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

import static android.R.id.edit;

public class MainActivity extends AppCompatActivity {
    //Khai bao gia tri
    public TextView tv;
    private EditText editName,editPass;
    private Button btDangnhap,btDangkiMain;
    private CheckBox cb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Anh xa acctitivy_main.xml
        tv=(TextView) findViewById(R.id.tvName);
        editName=(EditText) findViewById(R.id.etUserLogin);
        editPass=(EditText) findViewById(R.id.etPassLogin);
        btDangnhap=(Button) findViewById(R.id.btDangnhap);
        cb=(CheckBox) findViewById(R.id.cbRememberLogin);
        btDangkiMain=(Button) findViewById(R.id.btDangkiMain) ;

        btDangnhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, xemlichtrinh.class);
                startActivity(intent);
            }
        });
        btDangkiMain.setOnClickListener(new View.OnClickListener(){
            @Override
            public  void onClick(View v){
                Intent intent = new Intent(MainActivity.this, SignUPActivity.class);
                startActivity(intent);
            }
        });
    }
}

