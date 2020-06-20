package com.example.retailsupporter.Firebase;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

import static android.content.ContentValues.TAG;
import static com.example.retailsupporter.Activity.LoginActivity.USER;
import static com.example.retailsupporter.Activity.LoginActivity.db;
import static com.example.retailsupporter.Activity.FragmentPages.DailySalesFragment.reflashDailySalesDisplay;
import static com.example.retailsupporter.Activity.FragmentPages.TimeSheetFragment.getHours;
import static com.example.retailsupporter.Activity.FragmentPages.TimeSheetFragment.stockListArray;
import static com.example.retailsupporter.Calendar.MyCalendar.initCalenderCell;
import static com.example.retailsupporter.Constant.DAILY_SALES;
import static com.example.retailsupporter.Constant.FIREBASE_WORKDAY;
import static com.example.retailsupporter.Constant.STOCK_LIST;

public class DataCollection {

    /**
     * Firebase db add data------------NEEDS UPDATE-------------
     * Simple add data, just make sure its working
     * Should include add user data, check-in check-out time
     */
    public static void addTimeData() {
        db.collection(USER.getUserEmail())
                .document(USER.getDocDate())
                .set(USER.getGroups())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Collection", "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        Log.d(TAG,
                                "Error adding document " + e);
                    }
                });
    }

    /**
     * checking user has clockin or not
     */
    public static void readDataCheck() {
        db.collection(USER.getUserEmail())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document.getId().equals(USER.getDocDate())) {
                                    HashMap<String, Object> hashMap = new HashMap<>();
                                    hashMap.put("CI", document.getString("CI"));
                                    hashMap.put("Store", document.getString("Store"));
                                    hashMap.put("CO", document.getString("CO"));
                                    USER.setGroups(hashMap);
                                }
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    /**
     * read timesheet data and update
     */
    public static void readData() {
        db.collection(USER.getUserEmail())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                HashMap<String, Object> hashMap = new HashMap<>();
                                hashMap.put("CI", document.getString("CI"));
                                hashMap.put("Store", document.getString("Store"));
                                hashMap.put("CO", document.getString("CO"));
                                USER.setGroups(hashMap);
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }


    /**
     * Firebase db read data------------NEEDS UPDATE-------------
     * Simple read data, just make sure its working
     * Should include get user data, check-in check-out time
     *
     * @param tv
     * @param db
     * @param collection
     * @param key
     */
    public static void getData(final TextView tv, FirebaseFirestore db, String collection, final String[] key) {
        db.collection(collection)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            tv.setText("");
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                tv.append(document.getId() + " => " + document.getData() + "\n");
                                tv.append("\t\t" + key[0] + " is " + document.getData().get(key[0]) + "\n");
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    /**
     * read the workday data from db
     *
     * @param monthdays
     */
    public static void readScheduleData(final ArrayList<String> monthdays) {
        db.collection(USER.getUserEmail())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document.getId().equals(FIREBASE_WORKDAY)) {
                                    HashMap<String, Object> hashMap = new HashMap<>();
                                    for (int i = 0; i < monthdays.size(); i++) {
                                        if (document.getString(monthdays.get(i)) != null) {
                                            hashMap.put(monthdays.get(i), document.getString(monthdays.get(i)));
                                        }
                                    }
                                    Log.e("readScheduleData", hashMap.toString());
                                    USER.setWorkDays(hashMap);
                                    Log.e("readScheduleData -USER", USER.getWorkDays().toString());
                                }
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    /**
     * when user change the date, read the data from db and update the listview
     *
     * @param monthdays
     * @param context
     */
    public static void readScheduleDataUpdate(final ArrayList<String> monthdays, final Context context) {
        db.collection(USER.getUserEmail())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document.getId().equals(FIREBASE_WORKDAY)) {
                                    HashMap<String, Object> hashMap = new HashMap<>();
                                    for (int i = 0; i < monthdays.size(); i++) {
                                        if (document.getString(monthdays.get(i)) != null) {
                                            hashMap.put(monthdays.get(i), document.getString(monthdays.get(i)));
                                        }
                                    }
                                    USER.setWorkDays(hashMap);
                                    initCalenderCell(context);
                                }
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    /**
     * The whole store stock list, only for read.
     * user dont have right to edit it.
     */
    public static void readStockList() {
        db.collection(STOCK_LIST)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                stockListArray.add(document.getId());
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    /**
     * add the dailySales info to firebase
     */
    public static void addDailySalesData() {
        for (String key : USER.getDailySales().keySet()) {
            db.collection(USER.getUserEmail())
                    .document(USER.getDocDate())
                    .collection(DAILY_SALES)
                    .document(getHours())
                    .set(USER.getDailySales())
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("Collection", "DocumentSnapshot successfully written!");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(Exception e) {
                            Log.d(TAG,
                                    "Error adding document " + e);
                        }
                    });
        }
    }

    /**
     * read the dailySales info to firebase, at beginning
     */
    public static void readDailySalesData() {
        db.collection(USER.getUserEmail())
                .document(USER.getDocDate())
                .collection(DAILY_SALES)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            HashMap<String, Object> hashMap = new HashMap<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                HashMap<String, Object> hashMapInfo = new HashMap<>();
                                hashMapInfo.put("ProductName", document.getString("ProductName"));
                                hashMapInfo.put("Amount", document.getString("Amount"));
                                hashMapInfo.put("Price", document.getString("Price"));
                                hashMap.put(document.getId(), hashMapInfo);
                            }
                            USER.setDailySales(hashMap);
                            Log.e("USER.getDailySales()", USER.getDailySales().toString());
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    /**
     * read and update the dailySales info to firebase, at beginning
     *
     * @param view
     * @param context
     */
    public static void readDailySalesDataUpdate(final View view, final Context context) {
        db.collection(USER.getUserEmail())
                .document(USER.getDocDate())
                .collection(DAILY_SALES)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            HashMap<String, Object> hashMap = new HashMap<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                HashMap<String, Object> hashMapInfo = new HashMap<>();
                                hashMapInfo.put("ProductName", document.getString("ProductName"));
                                hashMapInfo.put("Amount", document.getString("Amount"));
                                hashMapInfo.put("Price", document.getString("Price"));
                                hashMap.put(document.getId(), hashMapInfo);
                            }
                            USER.setDailySales(hashMap);
                            reflashDailySalesDisplay(view, context);
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }

}
