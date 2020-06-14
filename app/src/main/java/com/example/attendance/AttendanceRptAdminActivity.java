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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
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

public class AttendanceRptAdminActivity extends AppCompatActivity {

    private singlten_attendanace_rpt_admin adpAll;
    private singlten_report adp;
    private DatePickerDialog picker;
    private ProgressDialog progressDialog;
    private int EmployeeId, UserId, selectedEmpId = 0;
    private String EmployeeName, EmployeeImage, FromDate, ToDate;
    private TextView FromDate_tv, ToDate_tv;
    private AutoCompleteTextView emp_e;
    private Button save_b;
    private ListView listView;
    private List<String> DateList, CheckInTimeList, CheckOutTimeList, EmployeeNameList, EmployeeNameList1;
    private List<Integer> LateMinList, TotalWorkTimeList, EarlyMinList, BreakMinList, EmployeeIdList, EmployeeIdList1, TotalDaysList, PresentDaysList, RestDaysList;
    private List<Date> DateList1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_rpt_admin);
        initailization();

        FromDate_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                final int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(AttendanceRptAdminActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                if (((int) (Math.log10(dayOfMonth) + 1) == 1)) {
                                    FromDate = year + "-" + (monthOfYear + 1) + "-0" + dayOfMonth;
                                } else {
                                    FromDate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                                }
                                FromDate_tv.setText(FromDate);
                                Toast.makeText(AttendanceRptAdminActivity.this, FromDate, Toast.LENGTH_SHORT).show();
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
                picker = new DatePickerDialog(AttendanceRptAdminActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                if (((int) (Math.log10(dayOfMonth) + 1) == 1)) {
                                    ToDate = year + "-" + (monthOfYear + 1) + "-0" + dayOfMonth;
                                } else {
                                    ToDate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                                }
                                ToDate_tv.setText(ToDate);
                                Toast.makeText(AttendanceRptAdminActivity.this, ToDate, Toast.LENGTH_SHORT).show();
                            }
                        }, year, month, day);
                picker.show();
            }
        });

        getEmployees();


        save_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(FromDate) || TextUtils.isEmpty(ToDate)) {
                    Toast.makeText(AttendanceRptAdminActivity.this, "Select All Fields First...", Toast.LENGTH_SHORT).show();
                } else {
                    getReportAttendanceAdmin();
                }
            }
        });

    }

    private void getReportAttendanceAdmin() {
        if (selectedEmpId == 0) {
            progressDialog.show();
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://minhasdemo1.000webhostapp.com/Attendance_test/getAttendanceRptAdminAll.php", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        progressDialog.dismiss();
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        EarlyMinList.clear();
                        LateMinList.clear();
                        BreakMinList.clear();
                        RestDaysList.clear();
                        PresentDaysList.clear();
                        TotalDaysList.clear();
                        EmployeeIdList1.clear();
                        EmployeeNameList1.clear();
                        String temp = response.substring(response.indexOf("["));
                        JSONArray jsonArry = new JSONArray(temp);
                        Toast.makeText(AttendanceRptAdminActivity.this, response.substring(0, response.indexOf("[")), Toast.LENGTH_SHORT).show();
                        for (int i = 0; i < jsonArry.length(); i++) {
                            JSONObject jsonObject = jsonArry.getJSONObject(i);
                            EmployeeNameList1.add(getMonthName(jsonObject.getString("EmployeeName")));
                            EmployeeIdList1.add(jsonObject.getInt("EmpId"));
                            EarlyMinList.add(jsonObject.getInt("TotalEarlyTime"));
                            LateMinList.add(jsonObject.getInt("TotalLateTime"));
                            BreakMinList.add(jsonObject.getInt("TotalBreakTime"));
                            RestDaysList.add(jsonObject.getInt("RestDays"));
                            PresentDaysList.add(jsonObject.getInt("PresentDays"));
                            TotalDaysList.add(jsonObject.getInt("totalDays"));
                        }
                        adpAll = new singlten_attendanace_rpt_admin(AttendanceRptAdminActivity.this, EmployeeNameList1, TotalDaysList,
                                PresentDaysList,
                                RestDaysList, BreakMinList, EarlyMinList, LateMinList);
                        listView.setAdapter(adpAll);
                    } catch (JSONException e) {
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
                    hashMap.put("FromDate", FromDate);
                    hashMap.put("ToDate", ToDate);
                    return hashMap;
                }
            };

            stringRequest.setRetryPolicy(new

                    DefaultRetryPolicy(10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            RequestQueue requestQueue = Volley.newRequestQueue(AttendanceRptAdminActivity.this);
            requestQueue.add(stringRequest);

        } else if (selectedEmpId > 0) {

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
                        Toast.makeText(AttendanceRptAdminActivity.this, response.substring(0, response.indexOf("[")), Toast.LENGTH_SHORT).show();
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
                        adp = new singlten_report(AttendanceRptAdminActivity.this, DateList, DateList1, CheckInTimeList, CheckOutTimeList, BreakMinList, EarlyMinList, LateMinList);
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
                    hashMap.put("EmpId", selectedEmpId + "");
                    hashMap.put("FromDate", FromDate);
                    hashMap.put("ToDate", ToDate);
                    return hashMap;
                }
            };

            stringRequest.setRetryPolicy(new

                    DefaultRetryPolicy(10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            RequestQueue requestQueue = Volley.newRequestQueue(AttendanceRptAdminActivity.this);
            requestQueue.add(stringRequest);
        }
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

    private void getEmployees() {
        progressDialog.show();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://minhasdemo1.000webhostapp.com/Attendance_test/getEmployees.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    progressDialog.dismiss();
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    EmployeeNameList.clear();
                    EmployeeIdList.clear();
                    EmployeeNameList.add("All Employees");
                    EmployeeIdList.add(0);
                    String temp = response.substring(response.indexOf("["));
                    JSONArray jsonArry = new JSONArray(temp);
                    Toast.makeText(AttendanceRptAdminActivity.this, response.substring(0, response.indexOf("[")), Toast.LENGTH_SHORT).show();
                    for (int i = 0; i < jsonArry.length(); i++) {
                        JSONObject jsonObject = jsonArry.getJSONObject(i);
                        EmployeeIdList.add(jsonObject.getInt("EmployeeId"));
                        EmployeeNameList.add(jsonObject.getString("EmployeeName"));
                    }
                    ArrayAdapter<String> arrayAdapter4 = new ArrayAdapter<String>(AttendanceRptAdminActivity.this, android.R.layout.simple_dropdown_item_1line, EmployeeNameList);
                    emp_e.setAdapter(arrayAdapter4);
                    emp_e.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            int i = EmployeeNameList.indexOf(emp_e.getText().toString());
                            selectedEmpId = EmployeeIdList.get(i);
                            emp_e.setText(EmployeeNameList.get(i));
                        }
                    });//item selected


                } catch (JSONException e) {
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
                return hashMap;
            }
        };

        stringRequest.setRetryPolicy(new
                DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(AttendanceRptAdminActivity.this);
        requestQueue.add(stringRequest);
    }

    private void initailization() {
        FromDate_tv = findViewById(R.id.FromDate_report_admin);
        ToDate_tv = findViewById(R.id.ToDate_report_admin);
        save_b = findViewById(R.id.save_report_admin);
        emp_e = findViewById(R.id.employee_report_admin);
        listView = findViewById(R.id.listview_attendance_admin);
        Intent intent = getIntent();
        EmployeeId = intent.getIntExtra("EmpId", 0);
        EmployeeName = intent.getStringExtra("EmpName");
        UserId = intent.getIntExtra("UserId", 0);
        EmployeeImage = intent.getStringExtra("EmployeeImage");
        progressDialog = new ProgressDialog(AttendanceRptAdminActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        DateList = new ArrayList<>();
        CheckInTimeList = new ArrayList<>();
        CheckOutTimeList = new ArrayList<>();
        EmployeeNameList = new ArrayList<>();
        LateMinList = new ArrayList<>();
        TotalWorkTimeList = new ArrayList<>();
        EarlyMinList = new ArrayList<>();
        BreakMinList = new ArrayList<>();
        EmployeeIdList = new ArrayList<>();
        DateList1 = new ArrayList<>();
        TotalDaysList = new ArrayList<>();
        PresentDaysList = new ArrayList<>();
        RestDaysList = new ArrayList<>();
        EmployeeIdList1 = new ArrayList<>();
        EmployeeNameList1 = new ArrayList<>();

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
        AlertDialog.Builder b = new AlertDialog.Builder(AttendanceRptAdminActivity.this);
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

    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(AttendanceRptAdminActivity.this, Home2Activity.class);
        intent.putExtra("EmpId", EmployeeId);
        intent.putExtra("EmpName", EmployeeName);
        intent.putExtra("UserId", UserId);
        intent.putExtra("EmployeeImage", EmployeeImage);
        startActivity(intent);
        finish();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

}
