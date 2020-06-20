package com.example.retailsupporter.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.retailsupporter.R;

import java.util.ArrayList;
import java.util.HashMap;

import static com.example.retailsupporter.Activity.LoginActivity.USER;

public class DailySalesAdapter extends BaseAdapter {

    private ArrayList<String> dailySaleItem = new ArrayList<>();
    private ArrayList<String> dailySaleTime = new ArrayList<>();
    private ArrayList<String> dailySalesAmount = new ArrayList<>();
    private ArrayList<String> dailySalesPrice = new ArrayList<>();
    private LayoutInflater layoutInflater;

    public DailySalesAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);

//        Log.e("USER in adapter",USER.getDailySales().toString());
        if (USER.getDailySales() != null) {
            for (Object key : USER.getDailySales().keySet()) {
                dailySaleItem.add(key.toString());
                HashMap<String, String> keyMap = (HashMap) USER.getDailySales().get(key.toString());
                dailySalesAmount.add(keyMap.get("Amount"));
                dailySalesPrice.add(keyMap.get("Price"));
                dailySaleTime.add(keyMap.get("ProductName"));
            }
        }

    }

    public ArrayList<String> getDailySaleTime() {
        return dailySaleTime;
    }

    public void setDailySaleTime(ArrayList<String> dailySaleTime) {
        Log.e("Adap-dailySaleTime", dailySaleTime.toString());
        this.dailySaleTime = dailySaleTime;
        notifyDataSetChanged();
    }

    public ArrayList<String> getDailySaleItem() {
        return dailySaleItem;
    }

    public void setDailySaleItem(ArrayList<String> dailySaleItems) {
        Log.e("Adap-dailySaleItem", dailySaleItems.toString());
        this.dailySaleItem = dailySaleItems;
        notifyDataSetChanged();
    }

    public ArrayList<String> getDailySalesAmount() {
        return dailySalesAmount;
    }

    public void setDailySalesAmount(ArrayList<String> dailySalesAmounts) {
        this.dailySalesAmount = dailySalesAmounts;
        notifyDataSetChanged();
    }

    public ArrayList<String> getDailySalesPrice() {
        return dailySalesPrice;
    }

    public void setDailySalesPrice(ArrayList<String> dailySalesPrices) {
        this.dailySalesPrice = dailySalesPrices;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return dailySaleItem.size();
    }

    @Override
    public Object getItem(int position) {
        return dailySaleItem.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.dailysales_item, null);
        }
        TextView productName = convertView.findViewById(R.id.product_name);
        TextView productAmount = convertView.findViewById(R.id.product_amount);
        TextView productPrice = convertView.findViewById(R.id.product_price);
        TextView productTime = convertView.findViewById(R.id.product_sale_time);

        productTime.setText(dailySaleTime.get(position));
        productName.setText(dailySaleItem.get(position));
        productAmount.setText(dailySalesAmount.get(position));
        productPrice.setText(dailySalesPrice.get(position));

        productTime.setTextColor(Color.WHITE);
        productName.setTextColor(Color.WHITE);
        productAmount.setTextColor(Color.WHITE);
        productPrice.setTextColor(Color.WHITE);

        productTime.setTextSize(16);
        productName.setTextSize(16);
        productAmount.setTextSize(16);
        productPrice.setTextSize(16);

        return convertView;
    }
}
