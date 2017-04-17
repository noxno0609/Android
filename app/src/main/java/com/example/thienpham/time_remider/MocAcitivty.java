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
import object.define;
import object.dto.periodeventdto;
import object.dto.timeeventdto;
import object.format;
import object.util;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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
    public int timeInterval = 5;
    static boolean deny = false;

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
                if(deny == false) {
                    Intent weekviewintent = new Intent(MocAcitivty.this, WeekViewAcitivity.class);
                    startActivity(weekviewintent);
                }
                else deny = false;
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
                Intent weekviewintent = new Intent(MocAcitivty.this, WeekViewAcitivity.class);
                startActivity(weekviewintent);
            }
        });
        loaddto = (timeeventdto) getIntent().getSerializableExtra("dto");
        if(loaddto != null) {
            setupLoadDTO(loaddto);
            checkconfirm = 1;
        }
    }

    public void workDTO() {
        class dtoWork extends AsyncTask<Void, Void, String> {
            private List<String> getData = new ArrayList<>();
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                getData = getDataList();
            }

            @Override
            protected String doInBackground(Void... params) {
                //0 DELETE 1 INSERT 2 UPDATE
                if(work == 0)
                    timeeventdao.delete(loaddto);
                else if(work == 1)
                {
                    loaddto = setDataListforDto(getData);
                    if(checkdto(loaddto) == true)
                    {
                        timeeventdao.insert(loaddto);
                    }
                    else
                    {
                        deny = true;
                        return "Sự kiện bị trùng thời gian, không thể thêm.";
                    }
                }
                else if(work == 2)
                {
                    int id = loaddto.id;
                    loaddto = setDataListforDto(getData);
                    loaddto.id = id;
                    if(checkdto(loaddto) == true)
                    {
                        timeeventdao.update(loaddto);
                    }
                    else
                    {
                        deny = true;
                        return "Sự kiện bị trùng thời gian, không thể sửa.";
                    }
                }
                return "";
            }

            @Override
            protected void onPostExecute(String strings) {
                super.onPostExecute(strings);
                if(!strings.equals(""))
                    Toast.makeText(MocAcitivty.this, strings, Toast.LENGTH_SHORT).show();
            }
        }
        new dtoWork().execute();
    }

    public static boolean checkdto(timeeventdto checkdto)
    {
        List<timeeventdto> listdto = timeeventdao.getall();
        for(timeeventdto dto : listdto)
        {
            if(dto.dayselect.equals((checkdto.dayselect)) && dto.id != checkdto.id && dto.userid == database.sessionuser.id)
            {
                int checkcase = checkDupplicateDate(checkdto.timestart, checkdto.timeend, dto.timestart, dto.timeend);

                if(checkcase  == define.COMPDATE.SAME)
                    return false;
                else if(checkcase == define.COMPDATE.INSIDEA)
                    splitdto(checkdto, dto, 1, true);
                else if(checkcase == define.COMPDATE.INSIDEB)
                    splitdto(checkdto, dto, 1, false);
                else if(checkcase == define.COMPDATE.BEFOREA)
                    splitdto(checkdto, dto, 2, true);
                else if(checkcase == define.COMPDATE.BEFOREB)
                    splitdto(checkdto, dto, 2, false);
                else if(checkcase == define.COMPDATE.SMEETA)
                    splitdto(checkdto, dto, 3, false);
                else if(checkcase == define.COMPDATE.SMEETB)
                    splitdto(checkdto, dto, 3, true);
                else if(checkcase == define.COMPDATE.EMEETA)
                    splitdto(checkdto, dto, 4, false);
                else if(checkcase == define.COMPDATE.EMEETB)
                    splitdto(checkdto, dto, 4, true);
            }
        }
        return true;
    }

    public static int checkDupplicateDate(Date timesa, Date timeea, Date timesb, Date timeeb)
    {
        if(timesa.equals(timesb) && timeea.equals(timeeb))
            return define.COMPDATE.SAME;
        else if(timesa.before(timesb) && timeea.after(timeeb))
            return define.COMPDATE.INSIDEA;
        else if(timesb.before(timesa) && timeeb.after(timeea))
            return define.COMPDATE.INSIDEB;
        else if(timesa.before(timesb) && timeea.before(timeeb) && timesb.before(timeea))
            return define.COMPDATE.BEFOREA;
        else if(timesb.before(timesa) && timeeb.before(timeea) && timesa.before(timeeb))
            return define.COMPDATE.BEFOREB;
        else if(timesa.equals(timesb) && timeea.before(timeeb))
            return define.COMPDATE.SMEETA;
        else if(timesa.equals(timesb) && timeeb.before(timeea))
            return define.COMPDATE.SMEETB;
        else if(timesb.before(timesa) && timeea.equals(timeeb))
            return define.COMPDATE.EMEETA;
        else if(timesa.before(timesb) && timeea.equals(timeeb))
            return define.COMPDATE.EMEETB;
        return define.COMPDATE.OUT;
    }

    public static void splitdto(timeeventdto childdto, timeeventdto dto, int scase, boolean bigger)
    {
        if(scase == 1) {
            if(bigger == true)
            {
                timeeventdto splitdto = new timeeventdto(childdto);
                splitdto.timestart = dto.timeend;
                timeeventdao.insert(splitdto);
                childdto.timeend = dto.timestart;
            }
            else
            {
                timeeventdto splitdto = new timeeventdto(dto);
                dto.timeend = childdto.timestart;
                timeeventdao.update(dto);
                splitdto.timestart = childdto.timeend;
                timeeventdao.insert(splitdto);
            }
        }
        else if(scase == 2)
        {
            if(bigger == true) {
                dto.timestart = childdto.timeend;
                timeeventdao.update(dto);
            }
            else
            {
                dto.timeend = childdto.timestart;
                timeeventdao.update(dto);
            }
        }
        else if(scase == 3)
        {
            if(bigger == false)
            {
                dto.timestart = childdto.timeend;
                timeeventdao.update(dto);
            }
            else
            {
                childdto.timestart = dto.timeend;
            }
        }
        else if(scase == 4)
        {
            if(bigger == false)
            {
                dto.timeend = childdto.timestart;
                timeeventdao.update(dto);
            }
            else
            {
                childdto.timeend = dto.timestart;
            }
        }
    }
    public static timeeventdto setDataListforDto(List<String> getData)
    {
        timeeventdto dto = new timeeventdto();
        dto.note = getData.get(0);
        dto.timestart = format.parseTime(getData.get(1));
        dto.timeend = format.parseTime(getData.get(2));
        dto.dayselect = parseTextDate(getData.get(3));
        dto.userid = database.sessionuser.id;
        dto.textcolor = "0xffff0000";
        dto.bgcolor = "0xffffffff";
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
        btCancel.setText("Xóa");
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
                if(minute % timeInterval != 0)
                {
                    int minuteFloor=minute-(minute%timeInterval);
                    minute=minuteFloor + (minute==minuteFloor+1 ? timeInterval : 0);
                    if (minute==60)
                        minute=0;
                    //view.setCurrentMinute(minute);
                }
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
                if(minute % timeInterval != 0)
                {
                    int minuteFloor=minute-(minute%timeInterval);
                    minute=minuteFloor + (minute==minuteFloor+1 ? timeInterval : 0);
                    if (minute==60)
                        minute=0;
                    //view.setCurrentMinute(minute);
                }
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

    public static Date parseTextDate(String date)
    {
        SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy");

        Date tmp = null;
        try {
            tmp = sf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(tmp);
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        tmp = cal.getTime();

        return tmp;
    }
}
