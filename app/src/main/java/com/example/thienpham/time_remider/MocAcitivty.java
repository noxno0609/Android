package com.example.thienpham.time_remider;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import object.dao.timeeventdao;
import object.database;
import object.dto.periodeventdto;
import object.dto.timeeventdto;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MocAcitivty extends Activity {
    static Button btTimestart;
    static Button btTimeend;
    static Button btDayselect;
    static Button btCancel;
    static Button btConfirm;
    static EditText etNote;
    Calendar cal = Calendar.getInstance();
    timeeventdto loaddto;
    int checkconfirm = 0;
    int work = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moc);

        btTimestart =(Button) findViewById(R.id.btmocTimestart);
        btTimeend =(Button) findViewById(R.id.btmocTimeend);
        btDayselect =(Button) findViewById(R.id.btmocDayselect);
        btConfirm =(Button) findViewById(R.id.btmocConfirm);
        btCancel =(Button) findViewById(R.id.btmocCancel);
        etNote = (EditText) findViewById(R.id.etmocNote);

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
        btDayselect.setOnClickListener(new View.OnClickListener(){
            @Override
            public  void  onClick(View v){
                dayselect();
            }
        });
        btConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkconfirm == 1)
                {
                    work = 2;
                    workDTO();
                    Toast.makeText(MocAcitivty.this, "Lưu sự kiện thành công!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    work = 1;
                    workDTO();
                    Toast.makeText(MocAcitivty.this, "Thêm sự kiện thành công!", Toast.LENGTH_SHORT).show();
                }
                Intent intent = new Intent(MocAcitivty.this, WeekViewAcitivity.class);
                startActivity(intent);
            }
        });
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkconfirm == 1)
                {
                    work = 0;
                    workDTO();
                    Toast.makeText(MocAcitivty.this, "Xóa sự kiện thành công!", Toast.LENGTH_SHORT).show();
                }
                Intent intent = new Intent(MocAcitivty.this, WeekViewAcitivity.class);
                startActivity(intent);
            }
        });

        loaddto = (timeeventdto) getIntent().getSerializableExtra("dto");
        if(loaddto != null) {
            setupLoadDTO(loaddto);
            checkconfirm = 1;
        }
    }

    public void workDTO() {
        class dtoWork extends AsyncTask<Void, Void, List<String>> {
            private List<String> getData = new ArrayList<>();
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                getData = getDataList();
            }

            @Override
            protected List<String> doInBackground(Void... params) {
                //0 DELETE 1 INSERT 2 UPDATE
                if(work == 0)
                    timeeventdao.delete(loaddto);
                else if(work == 1) {
                    loaddto = setDataListforDto(getData);
                    timeeventdao.insert(loaddto);
                }
                else if(work == 2) {
                    int id = loaddto.id;
                    loaddto = setDataListforDto(getData);
                    loaddto.id = id;
                    timeeventdao.update(loaddto);
                }
                return null;
            }
        }
        new dtoWork().execute();
    }

    public static timeeventdto setDataListforDto(List<String> getData)
    {
        timeeventdto dto = new timeeventdto();
        dto.note = getData.get(0);
        SimpleDateFormat sf = new SimpleDateFormat("HH:mm");
        try {
            dto.timestart = sf.parse(getData.get(1));
            dto.timeend = sf.parse(getData.get(2));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        sf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            dto.dayselect = sf.parse(getData.get(3));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        dto.userid = database.sessionuser.id;
        return dto;
    }

    public static List<String> getDataList()  {
        List<String> result = new ArrayList<>();

        result.add(etNote.getText().toString());
        result.add(btTimestart.getText().toString());
        result.add(btTimeend.getText().toString());
        result.add(btDayselect.getText().toString());

        return result;
    }

    public void setupLoadDTO(timeeventdto dto)
    {
        btTimestart.setText(readTime(dto.timestart));
        btTimeend.setText(readTime(dto.timeend));
        btDayselect.setText(readDate(dto.dayselect));
        etNote.setText(dto.note);
        btConfirm.setText("Lưu");
    }

    public String readDate(Date date)
    {
        int year = date.getYear() + 1900;
        int month = date.getMonth() + 1;
        int day = date.getDate();
        return String.format("%02d", day) + "/" + String.format("%02d", month) + "/" + year;
    }

    public String readTime(Date time)
    {
        int hour = time.getHours();
        int minute = time.getMinutes();
        return String.format("%02d", hour) + ":" + String.format("%02d", minute);
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
                btTimeend.setText(String.format("%02d", hourOfDay)+":"+String.format("%02d", minute));
            }
        },
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE),
                true);

        tpd.show();
    }
    void dayselect()
    {
        DatePickerDialog dpd = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                btDayselect.setText(String.format("%02d",dayOfMonth)+"/"+String.format("%02d",month+1)+"/"+year);
            }
        },
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH));
        dpd.show();
    }
}
