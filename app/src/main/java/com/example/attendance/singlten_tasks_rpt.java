package com.example.attendance;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class singlten_tasks_rpt extends BaseAdapter {
    Context c;
    TextView t1, t2, t3, t4, t5, t6;
    List<String> appFromDate, applyToDate, Reason, Type, empName;
    List<Integer> Status;

    singlten_tasks_rpt(Context context, List<String> _applyFromDate, List<String> _applyToDate, List<String> _Reason, List<Integer> _Status, List<String> _ApplicationType, List<String> _empName) {
        c = context;
        appFromDate = _applyFromDate;
        applyToDate = _applyToDate;
        Reason = _Reason;
        Type = _ApplicationType;
        Status = _Status;
        empName = _empName;
    }

    @Override
    public int getCount() {
        return Status.size();
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
        convertView = LayoutInflater.from(c).inflate(R.layout.singlten_tasks_rpt, null, false);
        t1 = convertView.findViewById(R.id.singlten_text1_task_rpt);
        t2 = convertView.findViewById(R.id.singlten_text2_task_rpt);
        t3 = convertView.findViewById(R.id.singlten_text3_task_rpt);
        t4 = convertView.findViewById(R.id.singlten_text4_task_rpt);
        t5 = convertView.findViewById(R.id.singlten_text5_task_rpt);
        t6 = convertView.findViewById(R.id.singlten_text6_task_rpt);

        t1.setText(t1.getText().toString() + appFromDate.get(position));
        t2.setText(t2.getText().toString() + applyToDate.get(position));
        t4.setText(t4.getText().toString() + Type.get(position));
        t5.setText(t5.getText().toString() + Reason.get(position));
        t6.setText(t6.getText().toString() + empName.get(position));

        if (Status.get(position) == 0) {
            t3.setText("InProgress");
        } else {
            t3.setText("Approved");
            t3.setTextColor(Color.parseColor("#14cf06"));//green
        }


        return convertView;
    }
}
