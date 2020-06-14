package com.example.attendance;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class singlten_attendanace_rpt_admin extends BaseAdapter {

    Context c;
    TextView tv1, tv2, tv3, tv4, tv5, tv6;
    List<String> EmployeeName;
    List<Integer> TotalDaysList, PresentDaysList, RestDaysList, EarlyMinList, BreakMinList, LateMinList;

    singlten_attendanace_rpt_admin(Context context, List<String> _EmployeeName, List<Integer> _TotalDaysList,
                                   List<Integer> _PresentDaysList, List<Integer> _RestDaysList,
                                   List<Integer> _BreakMinList, List<Integer> _EarlyMinList, List<Integer> _LateMinList) {

        EmployeeName = _EmployeeName;
        TotalDaysList = _TotalDaysList;
        PresentDaysList = _PresentDaysList;
        RestDaysList = _RestDaysList;
        EarlyMinList = _EarlyMinList;
        BreakMinList = _BreakMinList;
        LateMinList = _LateMinList;
        c = context;
    }

    @Override
    public int getCount() {
        return EmployeeName.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(c).inflate(R.layout.singlten_attendance_rpt_admin_layout, null, false);
        tv1 = convertView.findViewById(R.id.text1_singlten_admin_attendance_rpt);
        tv2 = convertView.findViewById(R.id.text2_singlten_admin_attendance_rpt);
        tv3 = convertView.findViewById(R.id.text3_singlten_admin_attendance_rpt);
        tv4 = convertView.findViewById(R.id.text4_singlten_admin_attendance_rpt);
        tv5 = convertView.findViewById(R.id.text5_singlten_admin_attendance_rpt);
        tv6 = convertView.findViewById(R.id.text6_singlten_admin_attendance_rpt);

        tv1.setText(EmployeeName.get(position));
        tv2.setText(tv2.getText() + "" + TotalDaysList.get(position));
        tv3.setText(tv3.getText() + "" + PresentDaysList.get(position));
        tv4.setText(tv4.getText() + "" + RestDaysList.get(position));
        tv5.setText(tv5.getText() + "" + LateMinList.get(position)+" Min");
        tv6.setText(tv6.getText() + "" + EarlyMinList.get(position)+" Min");

        return convertView;
    }
}
