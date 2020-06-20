package com.example.retailsupporter.Adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.retailsupporter.Activity.FragmentPages.DailySalesFragment;
import com.example.retailsupporter.Activity.FragmentPages.RegisterUserFragment;
import com.example.retailsupporter.Activity.FragmentPages.ScheduleFragment;
import com.example.retailsupporter.Activity.FragmentPages.TimeSheetFragment;

public class FragmentPageAdapter extends FragmentPagerAdapter {

    private String[] title = new String[]{"TimeSheet", "Register", "Sales", "Schedule"};

    public FragmentPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 1) {
            return new RegisterUserFragment();
        } else if (position == 2) {
            return new DailySalesFragment();
        } else if (position == 3) {
            return new ScheduleFragment();
        }
        return new TimeSheetFragment();
    }

    @Override
    public int getCount() {
        return title.length;
    }

    /**
     * use this to control we show title on the tab
     */
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return title[position];
    }
}
