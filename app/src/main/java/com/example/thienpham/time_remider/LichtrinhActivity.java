package com.example.thienpham.time_remider;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import object.dao.periodeventdao;
import object.dto.periodeventdto;
import object.util;

import java.sql.Time;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class LichtrinhActivity extends Activity {
    Button btDaystart, btDayend, btTimestart, btTimeend,btCancel, btConfirm;
    EditText etnote;
    Calendar cal = Calendar.getInstance();
    CheckBox cbT2,cbT3,cbT4,cbT5,cbT6,cbT7,cbCN,cbMoingay;
    int checkconfirm = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_lichtrinh);
        btCancel =(Button) findViewById(R.id.btCancel);
        btConfirm = (Button) findViewById(R.id.btConfirm);
        btTimestart = (Button) findViewById(R.id.btTimestart);
        btTimeend = (Button) findViewById(R.id.btTimeend);
        btDaystart = (Button) findViewById(R.id.btDaystart);
        btDayend = (Button) findViewById(R.id.btDayend);
        cbMoingay = (CheckBox) findViewById(R.id.cbMoingay);
        etnote = (EditText) findViewById(R.id.etltNote);
        cbT2 = (CheckBox) findViewById(R.id.cbT2);
        cbT3 = (CheckBox) findViewById(R.id.cbT3);
        cbT4 = (CheckBox) findViewById(R.id.cbT4);
        cbT5 = (CheckBox) findViewById(R.id.cbT5);
        cbT6 = (CheckBox) findViewById(R.id.cbT6);
        cbT7 = (CheckBox) findViewById(R.id.cbT7);
        cbCN = (CheckBox) findViewById(R.id.cbCN);

        btConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                if(checkconfirm == 0)
                    //periodeventdao.insert();
                else
                {
                    //periodeventdao.update();
                }
                */
            }
        });
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LichtrinhActivity.this, MainLichTrinhActivity.class);
                startActivity(intent);
            }
        });
        btTimestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timestart();
            }
        });
        btTimeend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeend();
            }
        });
        btDaystart.setOnClickListener(new View.OnClickListener(){
            @Override
            public  void  onClick(View v){
                daystart();
            }
        });
        btDayend.setOnClickListener(new View.OnClickListener(){
            @Override
            public  void  onClick(View v){
                dayend();
            }
        });
        cbMoingay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkrb();
            }
        });

        periodeventdto dto = (periodeventdto) getIntent().getSerializableExtra("dto");
        if(dto != null) {
            setupLoadDTO(dto);
            checkconfirm = 1;
        }
    }

    public String readDate(Date date)
    {
        int month = date.getMonth() + 1;
        int day = date.getDate();
        return String.format("%02d", day) + "/" + String.format("%02d", month);
    }

    public String readTime(Date time)
    {
        int hour = time.getHours();
        int minute = time.getMinutes();
        return String.format("%02d", hour) + ":" + String.format("%02d", minute);
    }

    public void setupLoadDTO(periodeventdto dto)
    {
        btTimestart.setText(readTime(dto.timestart));
        btTimeend.setText(readTime(dto.timeend));
        btDaystart.setText(readDate(dto.datestart));
        btDayend.setText(readDate(dto.dateend));
        etnote.setText(dto.note);

        //0000000
        boolean ischecked = (dto.dayselect.charAt(0) == '1') ? true : false;
        cbT2.setChecked(ischecked);
        ischecked = (dto.dayselect.charAt(1) == '1') ? true : false;
        cbT3.setChecked(ischecked);
        ischecked = (dto.dayselect.charAt(2) == '1') ? true : false;
        cbT4.setChecked(ischecked);
        ischecked = (dto.dayselect.charAt(3) == '1') ? true : false;
        cbT5.setChecked(ischecked);
        ischecked = (dto.dayselect.charAt(4) == '1') ? true : false;
        cbT6.setChecked(ischecked);
        ischecked = (dto.dayselect.charAt(5) == '1') ? true : false;
        cbT7.setChecked(ischecked);
        ischecked = (dto.dayselect.charAt(6) == '1') ? true : false;
        cbCN.setChecked(ischecked);

        btConfirm.setText("Lưu");
    }

    void timestart()
    {
        TimePickerDialog tpd = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                btTimestart.setText(String.format("%02d", hourOfDay)+":"+String.format("%02d", minute));
            }
        },
            cal.get(Calendar.HOUR_OF_DAY),
            cal.get(Calendar.MINUTE),
            true);
        tpd.show();
    }
    void timeend()
    {
        TimePickerDialog tpd = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                btTimeend.setText(String.format("%02d",hourOfDay)+":"+String.format("%02d",minute));
            }
        },
            cal.get(Calendar.HOUR_OF_DAY),
            cal.get(Calendar.MINUTE),
            true);
        tpd.show();
    }
    void daystart()
    {
        DatePickerDialog dpd = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                btDaystart.setText(dayOfMonth+"/"+String.format("%02d",month+1)+"/"+year);
            }
        },
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH));
        dpd.show();
    }
    void dayend()
    {
        DatePickerDialog dpd = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                btDayend.setText(dayOfMonth+"/"+String.format("%02d",month+1)+"/"+year);
            }
        },
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH));
        dpd.show();
    }
    void checkrb()
    {
        if (cbMoingay.isChecked())
        {
            cbT2.setChecked(true);
            cbT3.setChecked(true);
            cbT4.setChecked(true);
            cbT5.setChecked(true);
            cbT6.setChecked(true);
            cbT7.setChecked(true);
            cbCN.setChecked(true);
        }
        else
        {
            cbT2.setChecked(false);
            cbT3.setChecked(false);
            cbT4.setChecked(false);
            cbT5.setChecked(false);
            cbT6.setChecked(false);
            cbT7.setChecked(false);
            cbCN.setChecked(false);
        }
    }

}
