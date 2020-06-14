package com.example.attendance;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import android.text.format.DateFormat;

import java.util.Date;
import java.util.List;

public class singlten_report extends BaseAdapter {
    Context c;
    List<String> DateList, chekinTime, checkoutTime;
    List<Integer> EarlyMin, LateMin, BreakTime;
    TextView tv1, tv2, tv3, tv4, tv6;
    ImageView imageView;
    List<Date> DateList1;

    singlten_report(Context context, List<String> _Date, List<Date> _Date1, List<String> _checkinTime, List<String> _checkoutTime, List<Integer> _BreakTime, List<Integer> _EarlyMin, List<Integer> _LateMin) {
        c = context;
        DateList = _Date;
        DateList1 = _Date1;
        chekinTime = _checkinTime;
        checkoutTime = _checkoutTime;
        EarlyMin = _EarlyMin;
        LateMin = _LateMin;
        BreakTime = _BreakTime;
    }

    @Override
    public int getCount() {
        return DateList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(c).inflate(R.layout.singlten_report, null, false);
        tv1 = convertView.findViewById(R.id.single_text2);
        tv2 = convertView.findViewById(R.id.single_text3);
        tv4 = convertView.findViewById(R.id.single_text6);
        tv6 = convertView.findViewById(R.id.single_text7);
        tv3 = convertView.findViewById(R.id.single_text8);
        imageView = convertView.findViewById(R.id.single_imageView);
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE");
        String s = dateFormat.format(DateList1.get(position));
        if (s.equals("Sunday") || s.equals("sunday")) {
            tv1.setText(DateList.get(position));
            tv6.setText("Rest");
            tv6.setTextColor(Color.parseColor("#14cf06"));//green
        } else {
            if (chekinTime.get(position).equals("null")) {
                tv1.setText(DateList.get(position));
                tv6.setText("Absent");
                tv6.setTextColor(Color.parseColor("#961717"));//red
                imageView.setImageResource(R.mipmap.aaawww);
            } else {
                tv1.setText(DateList.get(position));
                tv3.setText(BreakTime.get(position) + "");
                tv2.setText(chekinTime.get(position) + " (" + LateMin.get(position) + ")");
                tv4.setText(checkoutTime.get(position) + " (" + EarlyMin.get(position) + ")");

                if (LateMin.get(position) > 5 || EarlyMin.get(position) > 0) {
                    imageView.setImageResource(R.mipmap.think);
                }

                if (EarlyMin.get(position) > 5 && LateMin.get(position) > 0) {
                    imageView.setImageResource(R.mipmap.aaawww);
                }
            }
        }

        return convertView;
    }
}
