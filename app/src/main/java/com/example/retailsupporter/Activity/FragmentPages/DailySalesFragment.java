package com.example.retailsupporter.Activity.FragmentPages;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.retailsupporter.Activity.LoginActivity;
import com.example.retailsupporter.Activity.ManagerActivity;
import com.example.retailsupporter.Adapter.DailySalesAdapter;
import com.example.retailsupporter.Firebase.DataCollection;
import com.example.retailsupporter.R;

import java.util.ArrayList;
import java.util.HashMap;

import static com.example.retailsupporter.Activity.LoginActivity.USER;
import static com.example.retailsupporter.Activity.FragmentPages.TimeSheetFragment.stockListArray;
import static com.example.retailsupporter.Activity.LoginActivity.mAuth;
import static com.example.retailsupporter.Firebase.DataCollection.readStockList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DailySalesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DailySalesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DailySalesFragment extends Fragment {
    AlertDialog builder;
    HashMap<String, Object> hashMapProductInfo;
    static ListView dailySalesListView;
    static DailySalesAdapter dailySalesAdapter;
    Spinner spinnerStockList, spinnerSalesAmount;
    EditText etSalesPrice;
    String product, amount, price;
    Button addBtn;
    Context context;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public DailySalesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DailySalesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DailySalesFragment newInstance(String param1, String param2) {
        DailySalesFragment fragment = new DailySalesFragment();
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
        Log.e("Fragment onCreate", "DS");
        context = getContext();
        stockListArray.add(" ");
        readStockList();
        DataCollection.readDailySalesData();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_daily_sales, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        setAdapter(view, context);

        etSalesPrice = view.findViewById(R.id.dailySalesPriceEt);
        addBtn = view.findViewById(R.id.dailySalesAddButton);
        spinnerStockList = view.findViewById(R.id.dailySalesStockListSpinner);
        spinnerSalesAmount = view.findViewById(R.id.dailySalesSalesAmountSpinner);

        ArrayList salesAmountArray = new ArrayList<Integer>();
        for (int i = 0; i < 10; i++) {
            salesAmountArray.add(i);
        }
        ArrayAdapter stockListAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, stockListArray) {

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
        stockListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStockList.setAdapter(stockListAdapter);
        spinnerStockList.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.e("ItemSelected-stockList", spinnerStockList.getSelectedItem().toString());
                product = spinnerStockList.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.e("Nothing-stockList", spinnerStockList.getSelectedItem().toString());
            }
        });

        ArrayAdapter salesAmountAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, salesAmountArray) {

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
        salesAmountAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSalesAmount.setAdapter(salesAmountAdapter);
        spinnerSalesAmount.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.e("ItemSelected-salesAm", spinnerSalesAmount.getSelectedItem().toString());
                amount = spinnerSalesAmount.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.e("NothingSelected-salesAm", spinnerSalesAmount.getSelectedItem().toString());
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                price = etSalesPrice.getText().toString();
                if (product.equals(" ")) {
                    alertDialogMethod(builder, getContext(), "Please select product first.");
                } else if (amount.equals("0")) {
                    alertDialogMethod(builder, getContext(), "Please select the sales amount.");
                } else if (price.equals("")) {
                    alertDialogMethod(builder, getContext(), "Please enter the sales price.");
                } else {
                    //add to firebase
                    hashMapProductInfo = new HashMap<>();
                    hashMapProductInfo.put("ProductName", product);
                    hashMapProductInfo.put("Amount", amount);
                    hashMapProductInfo.put("Price", price);
                    USER.setDailySales(hashMapProductInfo);
                    Log.e("USER.getDS", USER.getDailySales().toString());
                    DataCollection.addDailySalesData();
                    //update the listView adapter
                    DataCollection.readDailySalesDataUpdate(view, context);
                }
            }
        });
    }

    /**
     * set list adapter
     */
    private static void setAdapter(View view, Context context) {
        dailySalesListView = view.findViewById(R.id.dailySalesListView);
        dailySalesAdapter = new DailySalesAdapter(context);
        dailySalesListView.setAdapter(dailySalesAdapter);
    }

    /**
     * update the listview data and reflash the listview
     * called in DataCollection-******************
     */
    public static void reflashDailySalesDisplay(View view, Context context) {
        setAdapter(view, context);
        ArrayList<String> dailySaleTime = new ArrayList<>();
        ArrayList<String> dailySaleItems = new ArrayList<>();
        ArrayList<String> dailySalesAmounts = new ArrayList<>();
        ArrayList<String> dailySalesPrices = new ArrayList<>();
        for (Object key : USER.getDailySales().keySet()) {
            dailySaleTime.add(key.toString());
            HashMap<String, String> keyMap = (HashMap) USER.getDailySales().get(key.toString());
            dailySaleItems.add(keyMap.get("ProductName"));
            dailySalesAmounts.add(keyMap.get("Amount"));
            dailySalesPrices.add(keyMap.get("Price"));
        }
        dailySalesAdapter.setDailySaleTime(dailySaleTime);
        dailySalesAdapter.setDailySaleItem(dailySaleItems);
        dailySalesAdapter.setDailySalesAmount(dailySalesAmounts);
        dailySalesAdapter.setDailySalesPrice(dailySalesPrices);
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

    public static void alertDialogMethod(AlertDialog builder, Context context, String message) {
        builder = new AlertDialog.Builder(context)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OK", null)
                .show();
        builder.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
        builder.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
    }

}
