package com.example.retailsupporter.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.retailsupporter.Model.CalanderTextView;
import com.example.retailsupporter.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import static com.example.retailsupporter.Activity.LoginActivity.USER;

public class CalenderAdapter extends ArrayAdapter<Date> {
    private LayoutInflater layoutInflater;
    private ArrayList<Date> selectDates;

    public CalenderAdapter(@NonNull Context context, ArrayList<Date> dates, ArrayList<Date> selectDates) {
        super(context, R.layout.cell_layout, dates);
        layoutInflater = LayoutInflater.from(context);
        this.selectDates = selectDates;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final Date date = getItem(position);
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.cell_layout, parent, false);
        }
        if (date != null) {
            int day = date.getDate();
            ((TextView) convertView).setText(String.valueOf(day));

            //click the work day and show the work description
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String dateStr = new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(date);
                    Log.e("dateStr", dateStr);
                    HashMap<String, Object> wd = USER.getWorkDays();
                    for (String key : USER.getWorkDays().keySet()) {
                        if(key.equals(dateStr)){
                            AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                            dialog.setTitle("Work Description");
                            dialog.setMessage(USER.getWorkDays().get(key).toString());
                            dialog.show();
                        }
                    }
                }
            });
        }
        Date now = new Date();

        boolean isSameMonth = false;
        if (date.getMonth() == now.getMonth()) {
            isSameMonth = true;
        }

        //mark today be red, current month  black and other month gray
        if (isSameMonth) {
            ((CalanderTextView) convertView).setTextColor(Color.WHITE);
        } else {
            ((CalanderTextView) convertView).setTextColor(Color.BLACK);
        }
        if (date.getDate() == now.getDate() && date.getMonth() == now.getMonth() && date.getYear() == now.getYear()) {
            ((CalanderTextView) convertView).setTextColor(Color.RED);
            ((CalanderTextView) convertView).setTypeface(Typeface.DEFAULT_BOLD);
        }

        //Mark the day which be selected (means the work day)
        for (int i = 0; i < selectDates.size(); i++) {
            if (String.valueOf(date.getDate()).equals(String.valueOf(selectDates.get(i).getDate())) &&
                    String.valueOf(date.getMonth()).equals(String.valueOf(selectDates.get(i).getMonth())) &&
                    String.valueOf(date.getYear()).equals(String.valueOf(selectDates.get(i).getYear()))) {
                Log.e("Select Date", selectDates.get(i).toString());
                ((CalanderTextView) convertView).isSelectday = true;
            }
        }
        return convertView;
    }
}
