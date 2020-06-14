package com.example.attendance;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class singlten_admin_app_approval1 extends BaseAdapter {
    Context c;
    TextView tv1, tv2, tv3, tv4, tv5;

    @Override
    public int getCount() {
        return 0;
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
        convertView = LayoutInflater.from(c).inflate(R.layout.singlten_admin_app_approval, null, false);

        tv1 = convertView.findViewById(R.id.text1_single_approval);
        tv2 = convertView.findViewById(R.id.text2_single_approval);
        tv3 = convertView.findViewById(R.id.text3_single_approval);
        tv4 = convertView.findViewById(R.id.text4_single_approval);
        tv5 = convertView.findViewById(R.id.text5_single_approval);


        return convertView;
    }
}
