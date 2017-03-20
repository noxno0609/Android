package com.example.thienpham.time_remider;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

public class ShowActivity extends AppCompatActivity {
    private Button btHuy,btLichtrinh,btMoc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
        btHuy=(Button) findViewById(R.id.btHuy);
        btLichtrinh=(Button) findViewById(R.id.btLichtrinh);
        btMoc=(Button) findViewById(R.id.btMoc);

        btLichtrinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowActivity.this, LichtrinhActivity.class);
                startActivity(intent);
            }
        });

    }
}
