package com.example.attendance;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportActivity extends AppCompatActivity {

    private DatePickerDialog picker;
    private ProgressDialog progressDialog;
    private int EmployeeId, UserId;
    private String EmployeeName, EmployeeImage, FromDate, ToDate;
    private TextView FromDate_tv, ToDate_tv;
    private ListView listView;
    private singlten_report adp;
    private List<String> DateList, CheckInTimeList, CheckOutTimeList;
    private List<Integer> LateMinList, TotalWorkTimeList, EarlyMinList, BreakMinList;
    private List<Date> DateList1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        inialization();
        FromDate_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                final int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(ReportActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                if (((int) (Math.log10(dayOfMonth) + 1) == 1)) {
                                    FromDate = year + "-" + (monthOfYear + 1) + "-0" + dayOfMonth;
                                } else {
                                    FromDate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                                }
                                FromDate_tv.setText(FromDate);
                                Toast.makeText(ReportActivity.this, FromDate, Toast.LENGTH_SHORT).show();
                            }
                        }, year, month, day);
                picker.show();
            }
        });
        ToDate_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(ReportActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                if (((int) (Math.log10(dayOfMonth) + 1) == 1)) {
                                    ToDate = year + "-" + (monthOfYear + 1) + "-0" + dayOfMonth;
                                } else {
                                    ToDate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                                }
                                ToDate_tv.setText(ToDate);
                                Toast.makeText(ReportActivity.this, ToDate, Toast.LENGTH_SHORT).show();
                                if (TextUtils.isEmpty(FromDate)) {
                                    Toast.makeText(ReportActivity.this, "Select From Date First...", Toast.LENGTH_SHORT).show();
                                } else {
                                    getReport();
                                }
                            }
                        }, year, month, day);
                picker.show();
            }
        });


    }

    private void getReport() {
        progressDialog.show();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://minhasdemo1.000webhostapp.com/Attendance_test/getAttendanceReport.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    progressDialog.dismiss();
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    DateList.clear();
                    CheckInTimeList.clear();
                    CheckOutTimeList.clear();
                    EarlyMinList.clear();
                    LateMinList.clear();
                    BreakMinList.clear();
                    TotalWorkTimeList.clear();
                    SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
                    String temp = response.substring(response.indexOf("["));
                    JSONArray jsonArry = new JSONArray(temp);
                    Toast.makeText(ReportActivity.this, response.substring(0, response.indexOf("[")), Toast.LENGTH_SHORT).show();
                    for (int i = 0; i < jsonArry.length(); i++) {
                        JSONObject jsonObject = jsonArry.getJSONObject(i);
                        DateList.add(getMonthName(jsonObject.getString("Date")));
                        DateList1.add(df1.parse(jsonObject.getString("Date")));
                        CheckInTimeList.add(jsonObject.getString("TimeIn"));
                        CheckOutTimeList.add(jsonObject.getString("TimeOut"));
                        BreakMinList.add(jsonObject.getInt("BreakTime"));
                        EarlyMinList.add(jsonObject.getInt("EarlyTime"));
                        LateMinList.add(jsonObject.getInt("LateTime"));
                        TotalWorkTimeList.add(jsonObject.getInt("TotalWorkTime"));
                    }
                    adp = new singlten_report(ReportActivity.this, DateList, DateList1, CheckInTimeList, CheckOutTimeList, BreakMinList, EarlyMinList, LateMinList);
                    listView.setAdapter(adp);
                    adp.notifyDataSetChanged();
                    adp.notifyDataSetInvalidated();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                checkInternetConnection(error);
            }
        }) {
            protected Map<String, String> getParams() {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("EmpId", EmployeeId + "");
                hashMap.put("FromDate", FromDate);
                hashMap.put("ToDate", ToDate);
                return hashMap;
            }
        };

        stringRequest.setRetryPolicy(new

                DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(ReportActivity.this);
        requestQueue.add(stringRequest);
    }

    private String getMonthName(String date) {
        StringBuilder builder = new StringBuilder(date);

        if (date.substring(5, 7).equals("01")) {
            builder.replace(5, 7, "Jan");
        } else if (date.substring(5, 7).equals("02")) {
            builder.replace(5, 7, "Feb");
        } else if (date.substring(5, 7).equals("03")) {
            builder.replace(5, 7, "Mar");
        } else if (date.substring(5, 7).equals("04")) {
            builder.replace(5, 7, "Apr");
        } else if (date.substring(5, 7).equals("05")) {
            builder.replace(5, 7, "May");
        } else if (date.substring(5, 7).equals("06")) {
            builder.replace(5, 7, "Jun");
        } else if (date.substring(5, 7).equals("07")) {
            builder.replace(5, 7, "Jul");
        } else if (date.substring(5, 7).equals("08")) {
            builder.replace(5, 7, "Aug");
        } else if (date.substring(5, 7).equals("09")) {
            builder.replace(5, 7, "Sep");
        } else if (date.substring(5, 7).equals("10")) {
            builder.replace(5, 7, "Oct");
        } else if (date.substring(5, 7).equals("11")) {
            builder.replace(5, 7, "Nov");
        } else if (date.substring(5, 7).equals("12")) {
            builder.replace(5, 7, "Dec");
        }
        return String.valueOf(builder);
    }

    private void inialization() {
        Intent intent = getIntent();
        EmployeeId = intent.getIntExtra("EmpId", 0);
        EmployeeName = intent.getStringExtra("EmpName");
        UserId = intent.getIntExtra("UserId", 0);
        EmployeeImage = intent.getStringExtra("EmployeeImage");
        FromDate_tv = findViewById(R.id.FromDate_report);
        ToDate_tv = findViewById(R.id.ToDate_report);
        listView = findViewById(R.id.listview);
        progressDialog = new ProgressDialog(ReportActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        DateList = new ArrayList<>();
        CheckInTimeList = new ArrayList<>();
        CheckOutTimeList = new ArrayList<>();
        LateMinList = new ArrayList<>();
        EarlyMinList = new ArrayList<>();
        TotalWorkTimeList = new ArrayList<>();
        BreakMinList = new ArrayList<>();
        DateList1 = new ArrayList<>();
    }

    private void checkInternetConnection(VolleyError error) {
        String message = null;
        if (error instanceof NetworkError) {
            message = "Cannot connect to Internet...Please check your connection!";
        } else if (error instanceof ServerError) {
            message = "The server could not be found. Please try again after some time!!";
        } else if (error instanceof AuthFailureError) {
            message = "Cannot connect to Internet...Please check your connection!";
        } else if (error instanceof ParseError) {
            message = "Parsing error! Please try again after some time!!";
        } else if (error instanceof NoConnectionError) {
            message = "Cannot connect to Internet...Please check your connection!";
        } else if (error instanceof TimeoutError) {
            message = "Connection TimeOut! Please check your internet connection.";
        }
        AlertDialog.Builder b = new AlertDialog.Builder(ReportActivity.this);
        b.setTitle("Network Error");
        b.setMessage(message);
        b.setPositiveButton("Wifi Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final Intent intent = new Intent(Intent.ACTION_MAIN, null);
                intent.addCategory(Intent.CATEGORY_LAUNCHER);
                final ComponentName cn = new ComponentName("com.android.settings", "com.android.settings.wifi.WifiSettings");
                intent.setComponent(cn);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        b.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        b.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ReportActivity.this, HomeActivity.class);
        intent.putExtra("EmpId", EmployeeId);
        intent.putExtra("EmpName", EmployeeName);
        intent.putExtra("UserId", UserId);
        intent.putExtra("EmployeeImage", EmployeeImage);
        startActivity(intent);
        finish();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }
}
