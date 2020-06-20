package com.example.retailsupporter.Activity.FragmentPages;

import android.Manifest;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;

import com.example.retailsupporter.Activity.ManagerActivity;
import com.example.retailsupporter.Controller.AlarmReceiver;
import com.example.retailsupporter.Firebase.DataCollection;
import com.example.retailsupporter.GPS.GPSLocation;
import com.example.retailsupporter.Model.Users;
import com.example.retailsupporter.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import static com.example.retailsupporter.Activity.FragmentPages.DailySalesFragment.alertDialogMethod;
import static com.example.retailsupporter.Activity.LoginActivity.USER;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TimeSheetFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TimeSheetFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TimeSheetFragment extends Fragment implements LocationListener, OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private String collection;
    private final String TAG = "ManagerActivity";
    private final String[] key = {"name", "time", "location"};

    private boolean getService = false;
    FirebaseAuth mAuth;
    private LocationManager lms;
    private Location location;
    private String bestProvider = LocationManager.GPS_PROVIDER;
    HashMap<String, Object> hashMap = new HashMap<>();
    Spinner spinner;
    private static String storeSelect;
    public static final String[] STORE_NAME = new String[]{"Please select current store", "Metro Town", "Aberdeen", "Fair View", "Markville", "Road Show"};
    Button clockIn, clockOut;
    private static int PERMISSION_REQUEST_CODE = 1;
    public static ArrayList<String> stockListArray = new ArrayList<>();

    //private Location mLocation;
    private static final String[] INITIAL_PERMS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };
    AlertDialog builder;

    public TimeSheetFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TimeSheetFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TimeSheetFragment newInstance(String param1, String param2) {
        TimeSheetFragment fragment = new TimeSheetFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        Log.e("Fragment onCreate", "TS");
        mAuth = FirebaseAuth.getInstance();
        lms = (LocationManager) (getContext().getSystemService(Context.LOCATION_SERVICE));

        if (getContext().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED &&
                getContext().checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED) {
        } else {
            requestPermissions(INITIAL_PERMS, PERMISSION_REQUEST_CODE);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_time_sheet, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final TextView tv = view.findViewById(R.id.display);
        clockIn = view.findViewById(R.id.clockin);
        clockOut = view.findViewById(R.id.clockout);
        spinner = view.findViewById(R.id.spinner);
        tv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (USER.getWorkDays().toString() != null)
                    Log.e("TEST USER WORK DAY", USER.getWorkDays().toString());
                else
                    Log.e("TEST USER WORK DAY", "GGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG");
                return false;
            }
        });

        ArrayAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, STORE_NAME) {

            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setTextSize(16);
                ((TextView) v).setTextColor(
                        getResources().getColorStateList(R.color.white)
                );
                return v;
            }

            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                v.setBackgroundColor(Color.GRAY);
                ((TextView) v).setTextColor(
                        getResources().getColorStateList(R.color.white)
                );
                ((TextView) v).setGravity(Gravity.CENTER);

                return v;
            }
        };
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.e("onItemSelected", spinner.getSelectedItem().toString());
                storeSelect = spinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.e("onNothingSelected", spinner.getSelectedItem().toString());
            }
        });


        FirebaseUser currentUser = mAuth.getCurrentUser();
        collection = currentUser.getEmail();
        clockIn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //clock in
                DataCollection.readDataCheck();
                gps();
                if (storeSelect.equals("Please select current store")) {
                    alertDialogMethod(builder,getContext(),"Please select current work store.");
                } else if (storeSelect.equals("Road Show") || GPSLocation.checkUserLocation(location, storeSelect)) {
                    if (USER.getGroups() == null) {
                        Log.e("USER.getGroups() =null", "null");
                        hashMap.put("Store", storeSelect);
                        hashMap.put("CI", getDateTime());
                        USER.setGroups(hashMap);
                        DataCollection.addTimeData();
                        Notify();
                        Log.e("USER.getGroups() ==null", USER.getGroups().toString());
                        tv.setText("Succeeded clock-in !" + "\n" + " Time: " + USER.getGroups().get("CI") + "\n" + " At: " + USER.getGroups().get("Store"));
                    } else {
                        DataCollection.readData();
                        Log.e("USER INFO !=null", USER.getGroups().toString());
                        builder = new AlertDialog.Builder(getContext())
                                .setTitle("Change Clock In Time")
                                .setMessage("You have done clock-in at " + USER.getGroups().get("CI") + " at " + USER.getGroups().get("Store") + ", are you sure you want to update the time? (It can NOT be undo)")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        hashMap.put("Store", storeSelect);
                                        hashMap.put("CI", getDateTime());
                                        USER.setGroups(hashMap);
                                        DataCollection.addTimeData();
                                        //notifyManager.cancel(notifyId);
                                        Notify();
                                    }
                                })
                                .setNegativeButton("Cancel",null)
                                .show();
                        builder.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
                        builder.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);

                        tv.setText("Succeeded clock-in !" + "\n" + "Time: " + USER.getGroups().get("CI") + "\n" + "At: " + USER.getGroups().get("Store"));
                    }
                } else {
                    alertDialogMethod(builder,getContext(),"Please arrive " + storeSelect + " and click Clock-in again.");
                }
                //Log.e("USER INFO----in",USER.getGroups().toString());


            }
        });
        clockOut.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                DataCollection.readData();
                Log.e("haspMap", hashMap.toString());
                gps();
                if (USER.getGroups() != null) {
                    if (storeSelect.equals("Road Show") || GPSLocation.checkUserLocation(location, storeSelect)) {
                        builder = new AlertDialog.Builder(getContext())
                                .setTitle("Clock out")
                                .setMessage("Are you sure you want to clock out?")
                                .setPositiveButton("OK",new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        hashMap.put("CO", getDateTime());
                                        USER.setGroups(hashMap);
                                        DataCollection.addTimeData();
                                        tv.setText("Succeeded clock-out !" + "\n" + "Time: " + USER.getGroups().get("CO") + "\n" + "At: " + USER.getGroups().get("Store"));
                                    }
                                })
                                .setNegativeButton("Cancel", null)
                                .show();
                        builder.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
                        builder.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);

                    }
                } else {
                    alertDialogMethod(builder,getContext(),"There is no any clock-in record, please check with your supervisor.");
                }
            }
        });
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
//        broadcastManager.unregisterReceiver(response);
    }

    @Override
    public void onLocationChanged(Location location) {
        getLocation(location);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        //...
    }

    @Override
    public void onProviderEnabled(String provider) {
        //...
    }

    @Override
    public void onProviderDisabled(String provider) {
        alertDialogMethod(builder,getContext(),"Please turn on 3G/4G or GPS");
    }

    @Override
    public void onClick(View v) {

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    /**
     * Check user has turned on the GPS or Network location and update the current location
     */
    public void gps() {
        if (lms.isProviderEnabled(LocationManager.GPS_PROVIDER) || lms.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            //if gps or network is on, call locationServiceInitial() update current location
            locationServiceInitial();
        } else {
            alertDialogMethod(builder,getContext(),"Please turn on the location service");
            getService = true; //make sure location service turned on
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)); //open setting page
        }
    }

    /**
     * Get the current location
     */
    private void locationServiceInitial() {
        // Use criteria to get the accurate information
        Criteria criteria = new Criteria();
        bestProvider = lms.getBestProvider(criteria, true);
        if (getContext().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && getContext().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }
        location = lms.getLastKnownLocation(bestProvider);
        Log.d("TEST", "Location: " + location);

    }

    /**
     * Testing purpose
     * Display the location
     *
     * @param location
     */
    private void getLocation(Location location) {
        if (location != null) {
            Double longitude = location.getLongitude();
            Double latitude = location.getLatitude();
            alertDialogMethod(builder,getContext(),"longitude: " + longitude + " && latitude: " + latitude);
        } else {
            alertDialogMethod(builder,getContext(),"Can not located ");
        }
    }

    /**
     * Get the current time for check-in/out
     *
     * @return
     */
    public static String getDateTime() {
        SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
        Date date = new Date();
        String strDate = sdFormat.format(date);
        return strDate;
    }

    /**
     * Get the current time for document
     *
     * @return
     */
    public static String getDate() {
        SimpleDateFormat sdFormat = new SimpleDateFormat("yyyyMMdd");
        Date date = new Date();
        String strDate = sdFormat.format(date);
        return strDate;
    }

    /**
     * Get the current time for document (hours and minutes)
     *
     * @return
     */
    public static String getHours() {
        SimpleDateFormat sdFormat = new SimpleDateFormat("hh:mm:ss");
        Date date = new Date();
        String strDate = sdFormat.format(date);
        return strDate;
    }

    /**
     * after 8 hours display a notification
     */
    private void Notify() {
        Intent activate = new Intent(getActivity(), AlarmReceiver.class);
        AlarmManager alarms;
        alarms = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);

        PendingIntent alarmIntent = PendingIntent.getBroadcast(getActivity(), 0, activate, 0);
        alarms.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (60 * 60 * 8 * 1000), alarmIntent);
        Log.e("alarms", "alarms created");
    }

    /**
     * after user select date, get the current month
     *
     * @param date
     * @return
     */
    public static ArrayList<String> getMonthDay(Calendar date) {
        ArrayList<String> nextMonth = new ArrayList<>();
        for (int i = 0; i < date.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
            date.set(Calendar.DAY_OF_MONTH, i + 1);
            SimpleDateFormat sdFormat = new SimpleDateFormat("yyyyMMdd");
            String formateDate = sdFormat.format(date.getTime());
            nextMonth.add(formateDate);
        }
        Log.e("nextMonth", nextMonth.toString());
        return nextMonth;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
//            getActivity().setTitle("Time Sheet");
            Log.e("isVisible isResumed", "TS");
        }
    }
}
