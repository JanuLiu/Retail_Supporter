package com.example.retailsupporter.Adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.retailsupporter.Activity.FragmentPages.DailySalesFragment;
import com.example.retailsupporter.Activity.FragmentPages.ScheduleFragment;
import com.example.retailsupporter.Activity.FragmentPages.TimeSheetFragment;

public class FragmentEmployeePageAdapter extends FragmentPagerAdapter {

    private String[] title = new String[]{"TimeSheet", "Sales", "Schedule"};

    public FragmentEmployeePageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        if (position == 1) {
            return new DailySalesFragment();
        } else if (position == 2) {
            return new ScheduleFragment();
        }
        return new TimeSheetFragment();
    }

    @Override
    public int getCount() {
        return title.length;
    }
}
