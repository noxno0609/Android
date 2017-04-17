package com.example.thienpham.time_remider;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import object.dao.periodeventdao;
import object.dao.timeeventdao;
import object.database;
import object.dto.periodeventdto;
import object.dto.timeeventdto;
import object.format;
import object.util;

import java.util.*;
import java.util.concurrent.ExecutionException;

public class LichtrinhActivity extends Activity {
    static Button btDaystart, btDayend, btTimestart, btTimeend,btCancel, btConfirm;
    static EditText etnote, etName;
    static Calendar cal = Calendar.getInstance();
    static CheckBox cbT2,cbT3,cbT4,cbT5,cbT6,cbT7,cbCN,cbMoingay;
    static periodeventdto loaddto;
    static int checkconfirm = 0;
    static public int work = 0;
    public int timeInterval = 5;
    boolean deny = false;
    boolean overwrite = false;

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
        etName = (EditText) findViewById(R.id.etNameLT);
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
                if(checkconfirm == 1)
                {
                    work = 2;
                    String content = "";
                    try {
                        content = workDTO();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if(showsplitalert(content) == false) {
                        Toast.makeText(LichtrinhActivity.this, "Thêm lịch trình thành công!", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    work = 1;
                    String content = "";
                    try {
                        content = workDTO();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if(showsplitalert(content) == false) {
                        Toast.makeText(LichtrinhActivity.this, "Thêm lịch trình thành công!", Toast.LENGTH_SHORT).show();
                    }
                }
                if(deny == false) {
                    Intent intent = new Intent(LichtrinhActivity.this, MainLichTrinhActivity.class);
                    startActivity(intent);
                }
            }
        });
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkconfirm == 1)
                {
                    work = 0;
                    String content = "";
                    try {
                        content = workDTO();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if(!content.isEmpty())
                        Toast.makeText(LichtrinhActivity.this, content, Toast.LENGTH_SHORT).show();
                }
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

        loaddto = (periodeventdto) getIntent().getSerializableExtra("dto");
        if(loaddto != null) {
            setupLoadDTO(loaddto);
            checkconfirm = 1;
        }
    }

    public void processConditionWork() throws ExecutionException, InterruptedException {
        class dtoWork extends AsyncTask<Void, Void, String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected String doInBackground(Void... params) {
                //0 DELETE 1 INSERT 2 UPDATE
                if (work == 1) {
                    if(overwrite == true)
                        periodeventdao.insertcondition(loaddto, true);
                    else periodeventdao.insertcondition(loaddto, false);
                }
                else {
                    if(overwrite == true)
                        periodeventdao.updatecondition(loaddto, true);
                    else periodeventdao.updatecondition(loaddto, false);
                }
                return "";
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
            }
        }
        new dtoWork().execute();
    }

    public String workDTO() throws ExecutionException, InterruptedException {
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
                if(work == 0) {
                    periodeventdao.delete(loaddto);
                    return "Xóa lịch trình thành công";
                }
                else if(work == 1) {
                    loaddto = setDataListforDto(getData);
                    if(checkdto(loaddto) == true)
                        periodeventdao.insert(loaddto);
                    else {
                        deny = true;
                        return "Lịch trình này trùng với một số sự kiện, bạn có muốn ghi lịch đè lên sự kiện cũ?";
                    }
                }
                else if(work == 2) {
                    int id = loaddto.id;
                    loaddto = setDataListforDto(getData);
                    loaddto.id = id;
                    if(checkdto(loaddto) == true)
                        periodeventdao.update(loaddto);
                    else {
                        deny = true;
                        return "Lịch trình này trùng với một số sự kiện, bạn có muốn ghi lịch đè lên sự kiện cũ?";
                    }
                }
                return "";
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
            }
        }
        return new dtoWork().execute().get();
    }

    public boolean showsplitalert(String str)
    {
        if(deny == true)
        {
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case DialogInterface.BUTTON_POSITIVE: {
                            overwrite = true;
                            try {
                                processConditionWork();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            Intent intent = new Intent(LichtrinhActivity.this, MainLichTrinhActivity.class);
                            startActivity(intent);
                            break;
                        }
                        case DialogInterface.BUTTON_NEGATIVE:
                            overwrite = false;
                            try {
                                processConditionWork();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            Intent intent = new Intent(LichtrinhActivity.this, MainLichTrinhActivity.class);
                            startActivity(intent);
                            break;
                    }
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(LichtrinhActivity.this);
            builder.setMessage(str).setPositiveButton("Có", dialogClickListener)
                    .setNegativeButton("Không", dialogClickListener).show();

            return true;
        }
        return false;
    }

    public static boolean checkdto(periodeventdto dto)
    {
        List<Date> listsdate = util.getSelectDate(dto);
        List<timeeventdto> listdto = timeeventdao.getall();

        for (timeeventdto tdto : listdto)
        {
            if(tdto.userid == dto.userid)
            {
                if(listsdate.contains(tdto.dayselect))
                {
                    return false;
                }
            }
        }
        return true;
    }

    public static periodeventdto setDataListforDto(List<String> getData)
    {
        periodeventdto dto = new periodeventdto();
        dto.note = getData.get(0);
        dto.timestart = format.parseTime(getData.get(1));
        dto.timeend =  format.parseTime(getData.get(2));
        dto.datestart = MocAcitivty.parseTextDate(getData.get(3));
        dto.dateend = MocAcitivty.parseTextDate(getData.get(4));
        dto.dayselect = getData.get(5);
        dto.userid = database.sessionuser.id;
        dto.name = getData.get(6);
        dto.textcolor = "0xffff0000";
        dto.bgcolor = "0xffffffff";
        return dto;
    }

    public static List<String> getDataList()  {
        List<String> result = new ArrayList<>();

        result.add(etnote.getText().toString());
        result.add(btTimestart.getText().toString());
        result.add(btTimeend.getText().toString());
        result.add(btDaystart.getText().toString());
        result.add(btDayend.getText().toString());
        String dselect = "";
        dselect += (cbT2.isChecked() == true) ? "1" : "0";
        dselect += (cbT3.isChecked() == true) ? "1" : "0";
        dselect += (cbT4.isChecked() == true) ? "1" : "0";
        dselect += (cbT5.isChecked() == true) ? "1" : "0";
        dselect += (cbT6.isChecked() == true) ? "1" : "0";
        dselect += (cbT7.isChecked() == true) ? "1" : "0";
        dselect += (cbCN.isChecked() == true) ? "1" : "0";
        result.add(dselect);
        result.add(etName.getText().toString());

        return result;
    }

    public void setupLoadDTO(periodeventdto dto)
    {
        btTimestart.setText(readTime(dto.timestart));
        btTimeend.setText(readTime(dto.timeend));
        btDaystart.setText(readDate(dto.datestart));
        btDayend.setText(readDate(dto.dateend));
        etnote.setText(dto.note);
        etName.setText(dto.name);

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
                    view.setCurrentMinute(minute);
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
                    view.setCurrentMinute(minute);
                }
                btTimeend.setText(String.format("%02d", hourOfDay)+":"+String.format("%02d", minute));
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
                btDaystart.setText(String.format("%02d",dayOfMonth)+"/"+String.format("%02d",month+1)+"/"+year);
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
                btDayend.setText(String.format("%02d",dayOfMonth)+"/"+String.format("%02d",month+1)+"/"+year);
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
