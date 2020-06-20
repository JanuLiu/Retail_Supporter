package com.example.retailsupporter.Controller;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.retailsupporter.Activity.ManagerActivity;
import com.example.retailsupporter.Model.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.example.retailsupporter.Activity.FragmentPages.DailySalesFragment.alertDialogMethod;
import static com.example.retailsupporter.Activity.LoginActivity.USER;
import static com.example.retailsupporter.Activity.LoginActivity.alertDialogMethodwithTitle;
import static com.example.retailsupporter.Activity.LoginActivity.dailySalesPurpose;
import static com.example.retailsupporter.Activity.LoginActivity.schedulePurpose;
import static com.example.retailsupporter.Activity.FragmentPages.TimeSheetFragment.getDate;
import static com.example.retailsupporter.Constant.USER_AUTH_MANAGER;
import static com.example.retailsupporter.Constant.USER_MANAGER;
import static com.example.retailsupporter.Constant.USER_POSITION;

public class Authentication {

    // user sign in
    public static void signIn(final Context context, final FirebaseFirestore db, final FirebaseAuth mAuth, final ProgressDialog progressDialog, String email, String password, final AlertDialog warning) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.hide();
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.e(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(context, db, user);
                        } else {
                            // If sign in fails, display a message to the user.
                            alertDialogMethodwithTitle(warning,context,"Please check your account, password, connection, and try again.","Sign in Error");
                        }
                    }
                });
    }

    private static void updateUI(final Context context, final FirebaseFirestore db, FirebaseUser user) {
        Log.e("successful", "log in !");
        USER.setUserEmail(user.getEmail());
        USER.setDocDate(getDate());
        schedulePurpose();
        dailySalesPurpose();
        Log.e("F user", user.getEmail());
        Intent intent = new Intent(context, ManagerActivity.class);
        context.startActivity(intent);
    }

    //manager register new account for user
    public static void registerUser(final FirebaseFirestore db, final Context context, final FirebaseAuth mAuth, final String email, String password, final HashMap<String, Object> userAuthority, final AlertDialog warning) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //FirebaseUser userInfo = mAuth.getCurrentUser();
                            // Create new user object
                            Users user = new Users();
                            user.setUserEmail(email);
                            user.setUserAuthority(userAuthority);

                            db.collection(user.getUserEmail())
                                    .document(USER_POSITION)
                                    .set(userAuthority)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d("Add User", "DocumentSnapshot successfully written!");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(Exception e) {
                                            Log.w("Add User", "Error writing document", e);
                                        }
                                    });

                            USER_AUTH_MANAGER.put("UserPosition", USER_MANAGER);

                            db.collection(USER.getUserEmail())
                                    .document(USER_POSITION)
                                    .set(USER_AUTH_MANAGER)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d("Add User", "DocumentSnapshot successfully written!");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(Exception e) {
                                            Log.w("Add User", "Error writing document", e);
                                        }
                                    });


                            //Toast.makeText(context, "Successfully register!", Toast.LENGTH_SHORT).show();
                        } else {
                            // If sign in fails, display a message to the user.
                            alertDialogMethod(warning, context, "Authentication failed, please check your connection.");
                        }

                    }
                });
    }
}
