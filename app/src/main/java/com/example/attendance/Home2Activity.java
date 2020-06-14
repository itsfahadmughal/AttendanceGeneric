package com.example.attendance;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.Formatter;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

public class Home2Activity extends AppCompatActivity {

    private boolean doubleBackToExitPressedOnce = false;
    private Button createUser_b, assignTask_b, editPayroll_b, attendanceRpt_b, approveTask_b, approveApp_b;
    private CircleImageView profile_b;
    private int EmployeeId, UserId;
    private TextView EntityName;
    private String EmployeeName, EmployeeImage;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home2);

        inialization();

        createUser_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateNewUser();
            }
        });

        assignTask_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assignTask();
            }
        });

        editPayroll_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editPayroll();
            }
        });

        attendanceRpt_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AttendanceRpt();
            }
        });

        approveTask_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewTasks();
            }
        });

        approveApp_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApplicationApproval();
            }
        });

    }

    private void ApplicationApproval() {
        Intent intent = new Intent(Home2Activity.this, ApproveAppAdminActivity.class);
        intent.putExtra("EmpId", EmployeeId);
        intent.putExtra("EmpName", EmployeeName);
        intent.putExtra("UserId", UserId);
        intent.putExtra("EmployeeImage", EmployeeImage);
        startActivity(intent);
        finish();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }


    private void editPayroll() {
        Intent intent = new Intent(Home2Activity.this, getPayrollAdminActivity.class);
        intent.putExtra("EmpId", EmployeeId);
        intent.putExtra("EmpName", EmployeeName);
        intent.putExtra("UserId", UserId);
        intent.putExtra("EmployeeImage", EmployeeImage);
        startActivity(intent);
        finish();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    private void AttendanceRpt() {
        Intent intent = new Intent(Home2Activity.this, AttendanceRptAdminActivity.class);
        intent.putExtra("EmpId", EmployeeId);
        intent.putExtra("EmpName", EmployeeName);
        intent.putExtra("UserId", UserId);
        intent.putExtra("EmployeeImage", EmployeeImage);
        startActivity(intent);
        finish();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    private void ViewTasks() {

        Toast.makeText(this, "Task Clicked", Toast.LENGTH_LONG).show();

//        Intent intent = new Intent(Home2Activity.this, ViewTasksAdminActivity.class);
//        intent.putExtra("EmpId", EmployeeId);
//        intent.putExtra("EmpName", EmployeeName);
//        intent.putExtra("UserId", UserId);
//        intent.putExtra("EmployeeImage", EmployeeImage);
//        startActivity(intent);
//        finish();
//        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    private void assignTask() {
        Toast.makeText(this, "Assigned", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Home2Activity.this, AssignTaskAdminActivity.class);
        intent.putExtra("EmpId", EmployeeId);
        intent.putExtra("EmpName", EmployeeName);
        intent.putExtra("UserId", UserId);
        intent.putExtra("EmployeeImage", EmployeeImage);
        startActivity(intent);
        finish();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    private void CreateNewUser() {
        Toast.makeText(this, "Created", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Home2Activity.this, NewUserCreateActivity.class);
        intent.putExtra("EmpId", EmployeeId);
        intent.putExtra("EmpName", EmployeeName);
        intent.putExtra("UserId", UserId);
        intent.putExtra("EmployeeImage", EmployeeImage);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    private void inialization() {
        EntityName = findViewById(R.id.textView_admin);
        createUser_b = findViewById(R.id.button3_admin);
        assignTask_b = findViewById(R.id.button4_admin);
        editPayroll_b = findViewById(R.id.button5_admin);
        attendanceRpt_b = findViewById(R.id.button6_admin);
        approveTask_b = findViewById(R.id.button9_admin);
        approveApp_b = findViewById(R.id.button11_admin);
        profile_b = findViewById(R.id.dp_Profile_admin);

        progressDialog = new ProgressDialog(Home2Activity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        RequestOptions transcodeTypeRequestBuilder = new RequestOptions().error(R.drawable.avatar12);
        Intent intent = getIntent();
        EmployeeId = intent.getIntExtra("EmpId", 0);
        EmployeeName = intent.getStringExtra("EmpName");
        UserId = intent.getIntExtra("UserId", 0);
        EmployeeImage = intent.getStringExtra("EmployeeImage");
        EntityName.setText(EmployeeName);
        String imageUrl = "https://minhasdemo1.000webhostapp.com/Attendance_test/Emp/admin.jpg";
        Glide.with(Home2Activity.this).load(imageUrl).apply(transcodeTypeRequestBuilder).into(profile_b);

    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }

        Toast.makeText(this, "Press BACK again to exit", Toast.LENGTH_SHORT).show();
        this.doubleBackToExitPressedOnce = true;

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }
}
