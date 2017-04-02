package com.example.thienpham.time_remider;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.*;
import event.OnSwipeTouchListener;
import object.dao.timeeventdao;
import object.dao.userdao;
import object.database;
import object.define;
import object.dto.timeeventdto;
import object.dto.userdto;
import object.format;
import object.util;

import java.text.SimpleDateFormat;
import java.util.*;

import static android.view.ViewGroup.LayoutParams.FILL_PARENT;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public class WeekViewAcitivity extends AppCompatActivity {
    public static LinearLayout timeViewLayout;
    public static GridLayout weekview;
    public static RelativeLayout boneweekview;
    public static LinearLayout weekdaylayout;
    public static List<Date> weekdates;
    public static int screenWidth, screenHeight, viewWidth;
    public static Date nowDate = new Date();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week_view_acitivity);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;

        Button logout = (Button) findViewById(R.id.btLogOut);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database.sessionuser = null;
                Intent intent = new Intent(WeekViewAcitivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        boneweekview = (RelativeLayout) findViewById(R.id.boneweekview);
        boneweekview.setOnTouchListener(new OnSwipeTouchListener(WeekViewAcitivity.this)
        {
            public void onSwipeRight() {
                nowDate = util.addTime(nowDate, -7, define.DAY);
                showsessionDTO();
            }
            public void onSwipeLeft() {
                nowDate = util.addTime(nowDate, 7, define.DAY);
                showsessionDTO();
            }
        });
        timeViewLayout = (LinearLayout) findViewById(R.id.timeviewlayout);
        timeViewLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                timeViewLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                viewWidth = timeViewLayout.getWidth();

                weekdaylayout = (LinearLayout) findViewById(R.id.weekdaylayout);
                weekview = (GridLayout) findViewById(R.id.weekView);
                showsessionDTO();
            }
        });
    }


    public static Date formatDate(Date date)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public void showsessionDTO()
    {
        class weekViewWork extends AsyncTask<Void, Void, List<Integer>>
        {
            Hashtable btMap = new Hashtable();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                weekview.removeAllViews();
                createWeekViewBone();
                //Create Header
                weekdates = getWeekDates(formatDate(nowDate));
                weekdaylayout.removeAllViews();
                for(int i=0;i<7;i++)
                {
                    TextView dayView = new TextView(WeekViewAcitivity.this);
                    dayView.setText(util.numberDay(i) + "\n" + readDate(weekdates.get(i)) + "");
                    dayView.setLayoutParams(new LinearLayout.LayoutParams((screenWidth-viewWidth)/7, FILL_PARENT));//Thay doi size
                    dayView.setBackgroundColor(Color.CYAN);
                    dayView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    dayView.setGravity(Gravity.CENTER);
                    weekdaylayout.addView(dayView);
                }
            }

            @Override
            protected List<Integer> doInBackground(Void... params) {
                List<Integer> listcellinserted = new ArrayList<>();

                List<timeeventdto> listdto = timeeventdao.getall();
                for(timeeventdto dto : listdto)
                {
                    if(dto.userid == database.sessionuser.id)
                    {
                        if(weekdates.contains(dto.dayselect))
                        {
                            int column = dto.dayselect.getDay();
                            int rowstart = getRowPos(dto.timestart);
                            int rowend = getRowPos(dto.timeend);
                            int rowlength = rowend - rowstart;

                            GridLayout.Spec colPos = GridLayout.spec(column, 1);
                            GridLayout.Spec rowPos = GridLayout.spec(rowstart, rowlength);

                            for(int i=rowstart;i<rowend;i++)
                            {
                                int cellindex = getCellIndex(i, column);
                                listcellinserted.add(cellindex);
                            }

                            Button btcell = new Button(WeekViewAcitivity.this);
                            GridLayout.LayoutParams cellLayoutParams = new GridLayout.LayoutParams(rowPos, colPos);
                            cellLayoutParams.width = (screenWidth-viewWidth)/7;
                            cellLayoutParams.height = 10*rowlength;
                            btcell.setText(dto.note);
                            Hashtable btInfo = new Hashtable();
                            btInfo.put("params", cellLayoutParams);
                            btcell.setBackgroundColor(Color.GREEN);
                            btMap.put(btcell, btInfo);
                        }
                    }
                }
                return listcellinserted;
            }

            @Override
            protected void onPostExecute(List<Integer> listinsindex) {
                super.onPostExecute(listinsindex);
                Set<Button> keys = btMap.keySet();
                Iterator<Button> itr = keys.iterator();
                while (itr.hasNext()) {
                    Button bt = itr.next();
                    Hashtable btinfo = (Hashtable) btMap.get(bt);
                    GridLayout.LayoutParams cellLayoutParams = (GridLayout.LayoutParams) btinfo.get("params");
                    weekview.addView(bt, cellLayoutParams);
                }

                //Create Cell
                int numborder = 12;
                for(int r=0;r<weekview.getRowCount();r++)
                {
                    //Create TimeView
                    for(int c=1;c<8;c++)
                    {
                        GridLayout.Spec colPos = GridLayout.spec(c, 1);
                        GridLayout.Spec rowPos = GridLayout.spec(r, 1);

                        int cellindex = getCellIndex(r,c);
                        if(listinsindex.contains(cellindex))
                            continue;
                        Button btCell = new Button(WeekViewAcitivity.this);
                        GridLayout.LayoutParams cellLayoutParams = new GridLayout.LayoutParams(rowPos, colPos);
                        cellLayoutParams.width = (screenWidth-viewWidth)/7;
                        cellLayoutParams.height = 10;
                        btCell.setLayoutParams(cellLayoutParams);//Thay doi size
                        if(r == numborder) {
                            btCell.setBackgroundResource(R.drawable.cell_border);
                        }
                        weekview.addView(btCell, cellLayoutParams);
                    }
                    if(r == numborder)
                        numborder+=12;
                }
            }
        }
        new weekViewWork().execute();
    }

    public static int getCellIndex(int r, int c)
    {
        return (r*7+c)-1;
    }
    public static int getRowPos(Date time)
    {
        return (time.getHours()*12 + time.getMinutes()/5) -1;
    }
    public void createWeekViewBone()
    {
        LinearLayout weekdaylayout = (LinearLayout) findViewById(R.id.weekdaylayout);
        //Create Intersection (Điểm giao Header và Time View)
        TextView interView = new TextView(WeekViewAcitivity.this);
        interView.setText("");
        interView.setLayoutParams(new LinearLayout.LayoutParams(FILL_PARENT, weekdaylayout.getHeight()));//Thay doi size
        interView.setBackgroundColor(Color.CYAN);
        interView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        interView.setGravity(Gravity.CENTER);
        timeViewLayout.addView(interView);

        //Code For Week View
        weekview = (GridLayout) findViewById(R.id.weekView);
        weekview.removeAllViews();
        weekview.setColumnCount(8);
        weekview.setRowCount(288);

        //Create Time View
        int hour = 0;
        for(int r=0;r<weekview.getRowCount();r+=12)
        {
            GridLayout.Spec colPos = GridLayout.spec(0, 1);
            GridLayout.Spec rowPos = GridLayout.spec(r, 12);

            TextView hourView = new TextView(WeekViewAcitivity.this);
            hourView.setText(String.valueOf(hour));
            GridLayout.LayoutParams cellLayoutParams = new GridLayout.LayoutParams(rowPos, colPos);
            cellLayoutParams.width = viewWidth;
            cellLayoutParams.height = 10*12;
            hourView.setLayoutParams(cellLayoutParams);
            hourView.setBackgroundColor(Color.CYAN);
            hourView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            hourView.setGravity(Gravity.CENTER);

            weekview.addView(hourView, cellLayoutParams);
            hour++;
        }
    }
    public static List<Date> getWeekDates(Date date)
    {
        List<Date> result = new ArrayList<>();
        while(date.getDay() != define.MONDAY)
        {
            date = util.addTime(date, -1, define.DAY);
        }
        for(int i=0;i<7;i++)
        {
            result.add(util.addTime(date, i, define.DAY));
        }
        return result;
    }

    public static String readDate(Date date)
    {
        int month = date.getMonth() + 1;
        int day = date.getDate();
        return String.format("%02d", day) + "-" + String.format("%02d", month);
    }
}

