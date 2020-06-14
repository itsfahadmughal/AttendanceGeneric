package com.example.attendance;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class singlten_newUser_admin extends BaseAdapter {

    Context c;
    TextView tv1, tv2, tv3;

    List<String> DateList, PayPeriodFromList, PayPeriodToList, EmpNameList;
    List<Integer> PayIdList, EmpIdList;
    List<Double> TotalAmountPaidList, ChecquePaidList, CashPaidList, AdditionList, DeductionList;

    singlten_newUser_admin(Context context, List<Integer> _PayIdList, List<String> _DateList,
                           List<String> _PayPeriodFromList, List<String> _PayPeriodToList, List<String> _EmpNameList,
                           List<Integer> _EmpIdList, List<Double> _TotalAmountPaidList, List<Double> _ChecquePaidList,
                           List<Double> _CashPaidList, List<Double> _AdditionList, List<Double> _DeductionList) {
        c = context;
        DateList = _DateList;
        PayPeriodFromList = _PayPeriodFromList;
        PayPeriodToList = _PayPeriodToList;
        EmpNameList = _EmpNameList;
        PayIdList = _PayIdList;
        EmpIdList = _EmpIdList;
        TotalAmountPaidList = _TotalAmountPaidList;
        ChecquePaidList = _ChecquePaidList;
        CashPaidList = _CashPaidList;
        AdditionList = _AdditionList;
        DeductionList = _DeductionList;

    }

    @Override
    public int getCount() {
        return PayIdList.size();
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
        convertView = LayoutInflater.from(c).inflate(R.layout.singlten_newuser_admin_layout, null, false);

        tv1 = convertView.findViewById(R.id.text1_single_newuser);
        tv2 = convertView.findViewById(R.id.text3_single_newuser);
        tv3 = convertView.findViewById(R.id.text4_single_newuser);

        tv1.setText(EmpNameList.get(position));
        tv2.setText(DateList.get(position));
        tv3.setText(TotalAmountPaidList.get(position) + "");

        return convertView;
    }
}
