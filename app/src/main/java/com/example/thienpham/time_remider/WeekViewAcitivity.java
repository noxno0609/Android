package com.example.thienpham.time_remider;

import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.ViewGroup;
import android.widget.*;

import static android.view.ViewGroup.LayoutParams.FILL_PARENT;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public class WeekViewAcitivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week_view_acitivity);

        Point size = new Point();
        getWindowManager().getDefaultDisplay().getSize(size);
        int screenWidth = size.x;
        int screenHeight = size.y;

        for(int i=0;i<7;i++)
        {
            TextView dayView = new TextView(this);
            
            dayView.setLayoutParams(new LinearLayout.LayoutParams(screenWidth/7, 40));//Thay doi size
        }

        GridLayout weekview = (GridLayout) findViewById(R.id.weekView);
        weekview.removeAllViews();

        weekview.setColumnCount(7);
        weekview.setRowCount(288);

        for(int r=0;r<288;r++)
        {
            for(int c=0;c<7;c++)
            {
                GridLayout.Spec colPos = GridLayout.spec(c, 1);
                GridLayout.Spec rowPos = GridLayout.spec(r, 1);

                GridLayout.LayoutParams gridParam = new GridLayout.LayoutParams(
                        rowPos, colPos);

                LinearLayout abc = new LinearLayout(this);
                LinearLayout.LayoutParams layoutparam = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        1.0f

                );

                GradientDrawable gd = new GradientDrawable();
                gd.setColor(0xFFFFFF); // Changes this drawbale to use a single color instead of a gradient
                gd.setCornerRadius(5);
                gd.setStroke(1, 0xFF000000);

                Button bttest = new Button(this);
                bttest.setLayoutParams(new LinearLayout.LayoutParams(screenWidth/7,layoutparam.height));//Thay doi size
                bttest.setBackground(gd);
                abc.addView(bttest);
                abc.setLayoutParams(layoutparam);
                weekview.addView(abc, gridParam);

            }
        }

        /*
        TextView test = new TextView(this);
        test.setText("ABCDXYZ");
        test.setBackgroundColor(Color.RED);

        GridLayout.Spec colPos = GridLayout.spec(2, 1);
        GridLayout.Spec rowPos = GridLayout.spec(25, 1);

        GridLayout.LayoutParams gridParam = new GridLayout.LayoutParams(
                rowPos, colPos);
        */
    }
}
