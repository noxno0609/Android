package com.example.thienpham.time_remider;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.*;
import object.util;

import static android.view.ViewGroup.LayoutParams.FILL_PARENT;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public class WeekViewAcitivity extends AppCompatActivity {
    public LinearLayout timeViewLayout;
    public GridLayout weekview;
    public int screenWidth, screenHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week_view_acitivity);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;

        timeViewLayout = (LinearLayout) findViewById(R.id.timeviewlayout);
        timeViewLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                timeViewLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                int width = timeViewLayout.getWidth();

                //Create Header
                LinearLayout weekdaylayout = (LinearLayout) findViewById(R.id.weekdaylayout);
                for(int i=0;i<7;i++)
                {
                    TextView dayView = new TextView(WeekViewAcitivity.this);
                    dayView.setText(util.numberDay(i));
                    dayView.setLayoutParams(new LinearLayout.LayoutParams((screenWidth-width)/7, FILL_PARENT));//Thay doi size
                    dayView.setBackgroundColor(Color.CYAN);
                    dayView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    dayView.setGravity(Gravity.CENTER);
                    weekdaylayout.addView(dayView);
                }

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

                //Create Cell In Week View
                int numborder = 0;
                int hour = 0;
                int abc =0;
                for(int r=0;r<weekview.getRowCount();r++)
                {
                    if(r==276)
                        abc=hour;
                    //Create TimeView
                    for(int c=1;c<8;c++)
                    {
                        GridLayout.Spec colPos = GridLayout.spec(c, 1);
                        GridLayout.Spec rowPos = GridLayout.spec(r, 1);

                        Button btcell = new Button(WeekViewAcitivity.this);
                        GridLayout.LayoutParams cellLayoutParams = new GridLayout.LayoutParams(rowPos, colPos);
                        cellLayoutParams.width = (screenWidth-width)/7;
                        cellLayoutParams.height = 10;
                        btcell.setLayoutParams(cellLayoutParams);//Thay doi size
                        if(r == numborder && numborder != 0)
                            btcell.setBackgroundResource(R.drawable.cell_border);
                        weekview.addView(btcell, cellLayoutParams);
                    }
                    if(r == numborder)
                    {
                        GridLayout.Spec colPos = GridLayout.spec(0, 1);
                        GridLayout.Spec rowPos = GridLayout.spec(numborder, 12);

                        TextView hourView = new TextView(WeekViewAcitivity.this);
                        hourView.setText(String.valueOf(hour));
                        GridLayout.LayoutParams cellLayoutParams = new GridLayout.LayoutParams(rowPos, colPos);
                        cellLayoutParams.width = width;
                        cellLayoutParams.height = 10*12;
                        hourView.setLayoutParams(cellLayoutParams);
                        hourView.setBackgroundColor(Color.CYAN);
                        hourView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        hourView.setGravity(Gravity.CENTER);

                        weekview.addView(hourView, cellLayoutParams);
                        hour++;
                        numborder+=12;
                    }
                }
            }
        });
    }
}
