package com.example.attendance;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApproveAppAdminActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;
    private int EmployeeId, UserId, flagCheck;
    private String EmployeeName, EmployeeImage;
    private RadioGroup radioGroup;
    private RadioButton radioButton_leave, radioButton_loan;
    private ListView listView;
    private singlten_tasks_rpt adp;
    private singlten_loan_rpt adp1;
    private List<Integer> loanidlist, leaveIdList, noofInstallmentsList, isApprovedList, statusList;
    private List<Double> amountList;
    private List<String> purposeList, LoanDateList, applyFromDateList, applyToDateList, loanEmpName, leaveEmpName, reasonList, applicationTypeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approve_app_admin);
        initailization();
        getTasksRptLoan();
        getTasksRptLeave();

        radioButton_leave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adp = new singlten_tasks_rpt(ApproveAppAdminActivity.this, applyFromDateList, applyToDateList, reasonList, statusList, applicationTypeList,leaveEmpName);
                listView.setAdapter(adp);
                flagCheck = 1;

            }
        });

        radioButton_loan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adp1 = new singlten_loan_rpt(ApproveAppAdminActivity.this, LoanDateList, isApprovedList, amountList, noofInstallmentsList, purposeList,loanEmpName);
                listView.setAdapter(adp1);
                flagCheck = 2;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ApproveAppAdminActivity.this);
                builder.setTitle("Approve Application");
                builder.setMessage("Press Yes If You Want To Approve Application.");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (flagCheck == 1) {
                            ApproveApplication(leaveIdList.get(position), "https://minhasdemo1.000webhostapp.com/Attendance_test/updatestatusleave.php", 1);
                        } else if (flagCheck == 2) {
                            ApproveApplication(loanidlist.get(position), "https://minhasdemo1.000webhostapp.com/Attendance_test/updatestatusloan.php", 2);
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                builder.show();
            }
        });
    }

    private void ApproveApplication(final Integer integer, String _url, final int n) {
        progressDialog.show();
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        final String currentDateTime = df.format(currentTime);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, _url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                Toast.makeText(ApproveAppAdminActivity.this, response, Toast.LENGTH_SHORT).show();
                if (n == 1) {
                    getTasksRptLeave();
                } else if (n == 2) {
                    getTasksRptLoan();
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
                hashMap.put("Id", integer + "");
                hashMap.put("currentDate", currentDateTime + "");
                hashMap.put("UserId", UserId + "");
                return hashMap;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(ApproveAppAdminActivity.this);
        requestQueue.add(stringRequest);

    }

    private void getTasksRptLoan() {
        progressDialog.show();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://minhasdemo1.000webhostapp.com/Attendance_test/getApplicationRpt.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    loanidlist.clear();
                    LoanDateList.clear();
                    amountList.clear();
                    noofInstallmentsList.clear();
                    purposeList.clear();
                    isApprovedList.clear();
                    loanEmpName.clear();
                    progressDialog.dismiss();
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    String temp = response.substring(response.indexOf("["));
                    JSONArray jsonArry = new JSONArray(temp);
                    Toast.makeText(ApproveAppAdminActivity.this, response.substring(0, response.indexOf("[")), Toast.LENGTH_SHORT).show();
                    for (int i = 0; i < jsonArry.length(); i++) {
                        JSONObject jsonObject = jsonArry.getJSONObject(i);
                        loanidlist.add(jsonObject.getInt("Id"));
                        LoanDateList.add(jsonObject.getString("LoanDate").substring(0, 10));
                        amountList.add(jsonObject.getDouble("Amount"));
                        noofInstallmentsList.add(jsonObject.getInt("NoOfInstallments"));
                        purposeList.add(jsonObject.getString("PurposeOfLoan"));
                        isApprovedList.add(jsonObject.getInt("IsApproved"));
                        loanEmpName.add(jsonObject.getString("Name"));
                    }
                    if (adp1 != null) {
                        adp1.notifyDataSetChanged();
                        adp1.notifyDataSetInvalidated();
                    }

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
                hashMap.put("EmpId", 0 + "");
                hashMap.put("flag", 2 + "");
                return hashMap;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(ApproveAppAdminActivity.this);
        requestQueue.add(stringRequest);

    }


    private void getTasksRptLeave() {
        progressDialog.show();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://minhasdemo1.000webhostapp.com/Attendance_test/getApplicationRptLeave.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    leaveIdList.clear();
                    applyFromDateList.clear();
                    applyToDateList.clear();
                    reasonList.clear();
                    statusList.clear();
                    applicationTypeList.clear();
                    leaveEmpName.clear();
                    progressDialog.dismiss();
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    String temp = response.substring(response.indexOf("["));
                    JSONArray jsonArry = new JSONArray(temp);
                    Toast.makeText(ApproveAppAdminActivity.this, response.substring(0, response.indexOf("[")), Toast.LENGTH_SHORT).show();
                    for (int i = 0; i < jsonArry.length(); i++) {
                        JSONObject jsonObject = jsonArry.getJSONObject(i);
                        leaveIdList.add(jsonObject.getInt("AppId"));
                        applyFromDateList.add(jsonObject.getString("AppliedFrom").substring(0, 10));
                        applyToDateList.add(jsonObject.getString("AppliedTo").substring(0, 10));
                        reasonList.add(jsonObject.getString("Reason"));
                        statusList.add(jsonObject.getInt("Status"));
                        applicationTypeList.add(jsonObject.getString("Application_Type"));
                        leaveEmpName.add(jsonObject.getString("Name"));
                    }
                    adp = new singlten_tasks_rpt(ApproveAppAdminActivity.this, applyFromDateList, applyToDateList, reasonList, statusList, applicationTypeList,leaveEmpName);
                    listView.setAdapter(adp);
                    adp.notifyDataSetChanged();
                    adp.notifyDataSetInvalidated();

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
                hashMap.put("EmpId", 0 + "");
                hashMap.put("flag", 2 + "");
                return hashMap;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(ApproveAppAdminActivity.this);
        requestQueue.add(stringRequest);


    }


    private void initailization() {
        radioButton_loan = findViewById(R.id.radio_loan_admin);
        radioButton_leave = findViewById(R.id.radio_leave_admin);
        radioGroup = findViewById(R.id.radiogroup_appstatus_admin);
        listView = findViewById(R.id.listview_viewapp_admin);
        Intent intent = getIntent();
        EmployeeId = intent.getIntExtra("EmpId", 0);
        EmployeeName = intent.getStringExtra("EmpName");
        UserId = intent.getIntExtra("UserId", 0);
        EmployeeImage = intent.getStringExtra("EmployeeImage");
        progressDialog = new ProgressDialog(ApproveAppAdminActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        loanidlist = new ArrayList<>();
        leaveIdList = new ArrayList<>();
        noofInstallmentsList = new ArrayList<>();
        isApprovedList = new ArrayList<>();
        statusList = new ArrayList<>();
        amountList = new ArrayList<>();
        purposeList = new ArrayList<>();
        LoanDateList = new ArrayList<>();
        applyFromDateList = new ArrayList<>();
        applyToDateList = new ArrayList<>();
        reasonList = new ArrayList<>();
        applicationTypeList = new ArrayList<>();
        loanEmpName = new ArrayList<>();
        leaveEmpName = new ArrayList<>();
        flagCheck = 1;
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
        AlertDialog.Builder b = new AlertDialog.Builder(ApproveAppAdminActivity.this);
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
        Intent intent = new Intent(ApproveAppAdminActivity.this, Home2Activity.class);
        intent.putExtra("EmpId", EmployeeId);
        intent.putExtra("EmpName", EmployeeName);
        intent.putExtra("UserId", UserId);
        intent.putExtra("EmployeeImage", EmployeeImage);
        startActivity(intent);
        finish();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

}
