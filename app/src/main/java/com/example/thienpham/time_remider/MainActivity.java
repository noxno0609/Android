package com.example.thienpham.time_remider;


import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import object.dao.timeeventdao;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button bt = (Button) findViewById(R.id.button);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        new Xuly().execute();
                    }
                });
            }
        });
    }

    class Xuly extends AsyncTask<String, String, String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showUI("Kiem tra du lieu...");
        }

        @Override
        protected String doInBackground(String... params) {
            String abc = timeeventdao.test();
            return abc;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            showUI(s);
        }

    }
    public void showUI(String s)
    {
        /*
        Intent activitymoi= new Intent(this, Main2Activity.class);
        startActivity(activitymoi);
        */
        setContentView(R.layout.activity_main2);
        EditText edittext = (EditText) findViewById(R.id.ettest);
        edittext.setText(s, TextView.BufferType.EDITABLE);
    }
}
