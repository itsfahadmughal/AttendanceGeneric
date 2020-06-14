package com.example.attendance;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextClock;
import android.widget.TextView;

import java.util.ArrayList;

public class singlten_admin_assign_task extends BaseAdapter {

    private TextView t1, t2, t3;
    CheckBox checkBox;
    Context c;
    ArrayList<workersList> workers;

    singlten_admin_assign_task(Context context, ArrayList<workersList> w) {
        c = context;
        workers=w;
    }

    @Override
    public int getCount() {
        return workers.size();
    }

    @Override
    public Object getItem(int position) {
        return  workers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(c).inflate(R.layout.singlten_admin_assign_task_layout, null, false);

        t1 = convertView.findViewById(R.id.text1_admin_payroll_single);
        t2 = convertView.findViewById(R.id.text2_admin_payroll_single);
        t3 = convertView.findViewById(R.id.text3_admin_payroll_single);
        checkBox = convertView.findViewById(R.id.checkbox_admin_payroll_single);

        workersList p = getProduct(position);

        t1.setText((position + 1) + "");
        t2.setText(p.emp_code+"");
        t3.setText(p.emp_name);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            t3.setTooltipText(p.emp_name);
        }
        checkBox.setOnCheckedChangeListener(myCheckChangList);
        checkBox.setTag(position);
        checkBox.setChecked(p.box);

        return convertView;
    }

    workersList getProduct(int position) {
        return ((workersList) getItem(position));
    }

    ArrayList<workersList> getBox() {
        ArrayList<workersList> box = new ArrayList<workersList>();
        for (workersList p : workers) {
            if (p.box)
                box.add(p);
        }
        return box;
    }

    CompoundButton.OnCheckedChangeListener myCheckChangList = new CompoundButton.OnCheckedChangeListener() {
        public void onCheckedChanged(CompoundButton buttonView,
                                     boolean isChecked) {
            getProduct((Integer) buttonView.getTag()).box = isChecked;
        }
    };
}
