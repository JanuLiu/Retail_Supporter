package com.example.retailsupporter.Calendar;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;

import com.example.retailsupporter.Adapter.CalenderAdapter;
import com.example.retailsupporter.Firebase.DataCollection;
import com.example.retailsupporter.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static com.example.retailsupporter.Activity.LoginActivity.USER;
import static com.example.retailsupporter.Activity.FragmentPages.TimeSheetFragment.getMonthDay;

public class MyCalendar extends LinearLayout implements View.OnClickListener {
    private LayoutInflater layoutInflater;
    private AppCompatImageButton preMonth, nextMonth;
    private static AppCompatTextView topDate;
    private static GridView gridView;
    private static Calendar mainCalendar;
    Context context = getContext();

    public MyCalendar(Context context) {
        super(context);
        init();
    }

    public MyCalendar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyCalendar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        //Log.e("USER work day", USER.getWorkDays().toString());
        initView();
        initListener();
        initCalenderCell(context);
    }

    private void initView() {
        layoutInflater = LayoutInflater.from(getContext());
        layoutInflater.inflate(R.layout.calender_view, this, true);
        preMonth = findViewById(R.id.preMonth);
        nextMonth = findViewById(R.id.nextMonth);
        topDate = findViewById(R.id.tv_date);
        gridView = findViewById(R.id.gv_calendar);

        mainCalendar = Calendar.getInstance();
    }

    private void initListener() {
        nextMonth.setOnClickListener(this);
        preMonth.setOnClickListener(this);
    }

    public static void initCalenderCell(Context context) {
        //set up the top to display current month and year
        SimpleDateFormat sdf = new SimpleDateFormat(" yyyy MM ", Locale.getDefault());
        topDate.setText(sdf.format(mainCalendar.getTime()));
        topDate.setTextColor(Color.WHITE);
        topDate.setTextSize(20);

        //calculate those dates to display
        ArrayList<Date> cells = new ArrayList<>();
        int count = 7 * 6;
        Calendar calendar = (Calendar) mainCalendar.clone();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int preDays = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        calendar.add(Calendar.DAY_OF_MONTH, -preDays);
        while (cells.size() < count) {
            cells.add(calendar.getTime());
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        //get the work data from firebase
        ArrayList<Date> dates = new ArrayList<>();
        if (USER.getWorkDays() != null) {
            for (String key : USER.getWorkDays().keySet()) {
                SimpleDateFormat fdate = new SimpleDateFormat("yyyyMMdd");
                try {
                    dates.add(fdate.parse(key));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        gridView.setAdapter(new CalenderAdapter(context, cells, dates));
    }

    /**
     * Change to next/pre month and reload the data from firebase then reflash the calender
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nextMonth:
                mainCalendar.add(Calendar.MONTH, 1);
                DataCollection.readScheduleDataUpdate(getMonthDay(mainCalendar), context);

                break;
            case R.id.preMonth:
                mainCalendar.add(Calendar.MONTH, -1);
                DataCollection.readScheduleDataUpdate(getMonthDay(mainCalendar), context);
                break;
            default:
                break;
        }
    }


}
