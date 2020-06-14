package com.example.attendance;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class singlten_loan_rpt extends BaseAdapter {
    Context c;
    TextView t1, t2, t3, t4, t5, t6;
    List<String> purpose, date, empName;
    List<Integer> isapproved, noofInstall;
    List<Double> amount;

    singlten_loan_rpt(Context context, List<String> _Date, List<Integer> _isApproved, List<Double> _amount, List<Integer> _NoOfInstall, List<String> _purpose, List<String> _empName) {
        c = context;
        purpose = _purpose;
        date = _Date;
        isapproved = _isApproved;
        noofInstall = _NoOfInstall;
        amount = _amount;
        empName = _empName;
    }

    @Override
    public int getCount() {
        return date.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(c).inflate(R.layout.singlten_loan_rpt, null, false);
        t1 = convertView.findViewById(R.id.singlten_text1_loan_rpt);
        t2 = convertView.findViewById(R.id.singlten_text2_loan_rpt);
        t3 = convertView.findViewById(R.id.singlten_text3_loan_rpt);
        t4 = convertView.findViewById(R.id.singlten_text4_loan_rpt);
        t5 = convertView.findViewById(R.id.singlten_text5_loan_rpt);
        t6 = convertView.findViewById(R.id.singlten_text6_loan_rpt);

        t1.setText(t1.getText().toString() + date.get(position));
        t3.setText(t3.getText().toString() + amount.get(position) + "");
        t4.setText(t4.getText().toString() + noofInstall.get(position) + "");
        t5.setText(t5.getText().toString() + purpose.get(position));
        t6.setText(t6.getText().toString() + empName.get(position));

        if (isapproved.get(position) == 0) {
            t2.setText("InProgress");
        } else {
            t2.setText("Approved");
            t2.setTextColor(Color.parseColor("#14cf06"));//green
        }


        return convertView;
    }
}
