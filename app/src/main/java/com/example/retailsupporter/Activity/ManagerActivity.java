package com.example.retailsupporter.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toolbar;

import com.example.retailsupporter.Activity.FragmentPages.DailySalesFragment;
import com.example.retailsupporter.Activity.FragmentPages.RegisterUserFragment;
import com.example.retailsupporter.Activity.FragmentPages.ScheduleFragment;
import com.example.retailsupporter.Activity.FragmentPages.TimeSheetFragment;
import com.example.retailsupporter.Adapter.FragmentEmployeePageAdapter;
import com.example.retailsupporter.Adapter.FragmentPageAdapter;
import com.example.retailsupporter.Model.Users;
import com.example.retailsupporter.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.example.retailsupporter.Activity.LoginActivity.USER;
import static com.example.retailsupporter.Activity.LoginActivity.db;
import static com.example.retailsupporter.Activity.LoginActivity.mAuth;
import static com.example.retailsupporter.Constant.USER_MANAGER;
import static com.example.retailsupporter.Constant.USER_POSITION;
import static com.example.retailsupporter.Constant.USER_STAFF;


public class ManagerActivity extends AppCompatActivity implements TimeSheetFragment.OnFragmentInteractionListener,
        DailySalesFragment.OnFragmentInteractionListener, RegisterUserFragment.OnFragmentInteractionListener,
        ScheduleFragment.OnFragmentInteractionListener {

    AlertDialog builder;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FragmentPageAdapter fragmentPageAdapter;
    private FragmentEmployeePageAdapter fragmentEmployeePageAdapter;
    private TabLayout.Tab dailySalesFragment;
    private TabLayout.Tab registerUserFragment;
    private TabLayout.Tab setUpScheduleFragment;
    private TabLayout.Tab timeSheetFragment;
    private int[] managerIconResID = {R.drawable.select_one, R.drawable.select_two,
            R.drawable.select_three, R.drawable.select_four};
    private int[] staffIconResID = {R.drawable.select_one, R.drawable.select_three, R.drawable.select_four};
    private String[] actionbarManagerTitle = {"Time Sheet", "Register New User", "Daily Sales", "Work Schedule"};
    private String[] actionbarStaffTitle = {"Time Sheet", "Daily Sales", "Work Schedule"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseUser currentUser = mAuth.getCurrentUser();
        Log.e("USER-manager activity", USER.getUserEmail());
        setTitle(actionbarManagerTitle[0]);
        //check the user position
        db.collection(currentUser.getEmail())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document.getId().equals(USER_POSITION)) {
                                    Log.e("Position", document.getString(USER_POSITION));
                                    if (document.getString(USER_POSITION).equals(USER_MANAGER)) {
                                        initViews();
                                        setManagerTabLayoutIcon();
                                        //set up actionbar title to current page name
                                        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                                            @Override
                                            public void onPageScrolled(int position, float positionOffset,
                                                                       int positionOffsetPixels) {

                                            }
                                            @Override
                                            public void onPageSelected(int position) {
                                                setTitle(actionbarManagerTitle[position]);
                                            }
                                            @Override
                                            public void onPageScrollStateChanged(int state) {

                                            }
                                        });
                                    } else if (document.getString(USER_POSITION).equals(USER_STAFF)) {
                                        initEmpViews();
                                        setStaffTabLayoutIcon();
                                        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                                            @Override
                                            public void onPageScrolled(int position, float positionOffset,
                                                                       int positionOffsetPixels) {

                                            }
                                            @Override
                                            public void onPageSelected(int position) {
                                                setTitle(actionbarStaffTitle[position]);
                                            }
                                            @Override
                                            public void onPageScrollStateChanged(int state) {

                                            }
                                        });
                                    }
                                }
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

    }

    /**
     * if user position is manager, the fragment shows 4 pages
     */
    private void initViews() {
        //use adapter to connect ViewPager and Fragment
        viewPager = findViewById(R.id.viewPager);
        fragmentPageAdapter = new FragmentPageAdapter(getSupportFragmentManager());
        viewPager.setAdapter(fragmentPageAdapter);


        //TabLayout connect to ViewPager
        tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);

        //set up tab
        timeSheetFragment = tabLayout.getTabAt(0);
        registerUserFragment = tabLayout.getTabAt(1);
        dailySalesFragment = tabLayout.getTabAt(2);
        setUpScheduleFragment = tabLayout.getTabAt(3);
    }

    /**
     * if user position is staff, shows 3 pages (without register new user)
     */
    private void initEmpViews() {
        //use adapter to connect ViewPager and Fragment
        viewPager = findViewById(R.id.viewPager);
        fragmentEmployeePageAdapter = new FragmentEmployeePageAdapter(getSupportFragmentManager());
        viewPager.setAdapter(fragmentEmployeePageAdapter);

        //TabLayout connect to ViewPager
        tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);

        //set up tab
        timeSheetFragment = tabLayout.getTabAt(0);
        dailySalesFragment = tabLayout.getTabAt(1);
        setUpScheduleFragment = tabLayout.getTabAt(2);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.signout) {

            builder = new AlertDialog.Builder(this)
                    .setMessage("Are you sure you want to sign out?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mAuth.signOut();
                            USER = new Users();
                            Intent intent = new Intent(ManagerActivity.this, LoginActivity.class);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("No",null)
                    .show();
            builder.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
            builder.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);

        } else {
            return super.onContextItemSelected(item);
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    public void setManagerTabLayoutIcon() {
        for (int i = 0; i < managerIconResID.length; i++) {
            tabLayout.getTabAt(i).setIcon(managerIconResID[i]);
        }
    }

    public void setStaffTabLayoutIcon() {
        for (int i = 0; i < staffIconResID.length; i++) {
            tabLayout.getTabAt(i).setIcon(staffIconResID[i]);
        }
    }
}
