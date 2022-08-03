package com.example.midterms;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class BillsAdapter extends ArrayAdapter<Bill> {
    int resource;
    List<Bill> bills;

    public BillsAdapter(@NonNull Context context, int resource, @NonNull List<Bill> objects) {
        super(context, resource, objects);
        this.resource = resource;
        this.bills = objects;
    }

    @NonNull
    @Override
    // Milestone B: Show History.
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LinearLayout billView;
        Bill bill = getItem(position);
        int bill_prev = bill.previous;
        int bill_curr = bill.current;
        int bill_month = bill.month;
        double bill_cons = bill.consumption();
        double bill_payment = bill.get_bill();

        if (convertView == null) {
            billView = new LinearLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater li = (LayoutInflater) getContext().getSystemService(inflater);
            li.inflate(resource, billView, true);
        } else {
            billView = (LinearLayout) convertView;
        }

        TextView tvMonth = billView.findViewById(R.id.tvMonth);
        TextView tvConsumption = billView.findViewById(R.id.tvConsumption);
        TextView tvPayment = billView.findViewById(R.id.tvPayment);
        TextView tvCurrent = billView.findViewById(R.id.tvCurrent);
        TextView tvPrevious = billView.findViewById(R.id.tvPrevious);

        tvMonth.setText(bill_month + "");
        tvConsumption.setText(bill_cons + "");
        tvCurrent.setText(bill_curr + "");
        tvPayment.setText(bill_payment + "");
        tvPrevious.setText(bill_prev + "");

        return billView;
    }
}
