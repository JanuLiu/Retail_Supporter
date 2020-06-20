package com.example.retailsupporter.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import com.example.retailsupporter.Constant;
import com.example.retailsupporter.Controller.Authentication;
import com.example.retailsupporter.Firebase.DataCollection;
import com.example.retailsupporter.Model.Users;
import com.example.retailsupporter.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import java.util.Calendar;

import static com.example.retailsupporter.Activity.FragmentPages.RegisterUserFragment.isValidEmail;
import static com.example.retailsupporter.Activity.FragmentPages.TimeSheetFragment.getDate;
import static com.example.retailsupporter.Activity.FragmentPages.TimeSheetFragment.getMonthDay;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    public static FirebaseAuth mAuth;
    public static FirebaseFirestore db;
    Dialog dialog;
    ProgressDialog progressDialog;
    BroadcastReceiver response;
    EditText logInEmail, logInPassword;
    Button loginBtn;
    public static Users USER;
    AlertDialog warning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        USER = new Users();

        loginBtn = findViewById(R.id.loginBtn);
        logInEmail = findViewById(R.id.email);
        logInPassword = findViewById(R.id.password);
        dialog = new Dialog(this);
        progressDialog = new ProgressDialog(this);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings
                .Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        db.setFirestoreSettings(settings);

        loginBtn.setOnClickListener(this);

        response = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

            }
        };
    }

    @Override
    public void onClick(View v) {
        String email = logInEmail.getText().toString();
        String password = logInPassword.getText().toString();
        if (!isValidEmail(email)) {
            alertDialogMethodwithTitle(warning,this,
                    "Please enter a user email and with correct format","User Email");
        }
        //check the password length is over 6 to match Google request
        else if (password.length() < 6) {
            alertDialogMethodwithTitle(warning,this,
                    "Please enter a user password with at least 6 character.","User Password");
        } else {
            progressDialog.setMessage("Login . . .");
            progressDialog.show();
            Authentication.signIn(this, db, mAuth, progressDialog, email, password,warning);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user has signed in (non-null) and update UI accordingly.
        //Set up the USER data and read the necessary data from firebase
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Log.e("Current User", currentUser.getEmail());
            Log.e("Current User", currentUser.getUid());
            USER.setUserEmail(currentUser.getEmail());
            USER.setDocDate(getDate());
            schedulePurpose();
            dailySalesPurpose();
            changeToMainMenu();
        }
    }

    final public void changeToMainMenu() {
        Intent intent = new Intent(LoginActivity.this, ManagerActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        // Unregister since the activity is paused.
        super.onPause();
        unregisterReceiver(response);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // An IntentFilter can match against actions, categories, and data
        IntentFilter filter = new IntentFilter(Constant.LOAD_USER_DATA_DONE);
        // register broadcast
        registerReceiver(response, filter);
    }

    /**
     * update USER info for schedule fragment
     * dont know why it doesnt work when I call this in ScheduleFragment
     * if I call this in ScheduleFragment, it wont update the USER.
     * Ive set up USER as a public static, it should be able to use in ScheduleFragment
     * NEED TO CHECK WITH Stephen.
     */
    public static void schedulePurpose() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, 0);
        DataCollection.readScheduleData(getMonthDay(calendar));
    }

    /**
     * update USER info for dailySales fragment
     * NEED TO CHECK WITH Stephen.
     */
    public static void dailySalesPurpose() {
        DataCollection.readDailySalesData();
        Log.e("dailySalesPurpose", "dailySalesPurpose");
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    public static void alertDialogMethodwithTitle(AlertDialog builder, Context context, String message, String title) {
        builder = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OK", null)
                .show();
        builder.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
        builder.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
    }
}
