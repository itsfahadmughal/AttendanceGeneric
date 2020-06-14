package com.example.attendance;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class applicationActivity extends AppCompatActivity {

    private DatePickerDialog picker;
    private ProgressDialog progressDialog;
    private TextView FromDate_tv, ToDate_tv;
    private String FromDate, ToDate, condition, EmployeeName,EmployeeImage;
    private Spinner spinner;
    private EditText Reason_e;
    private Button save_b;
    private int EmployeeId, UserId;
    List<String> reasons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application);

        initialization();

        FromDate_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                final int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(applicationActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                if (((int) (Math.log10(dayOfMonth) + 1) == 1)) {
                                    FromDate = year + "-" + (monthOfYear + 1) + "-0" + dayOfMonth;
                                } else {
                                    FromDate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                                }
                                FromDate_tv.setText(FromDate);
                                Toast.makeText(applicationActivity.this, FromDate, Toast.LENGTH_SHORT).show();
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
                picker = new DatePickerDialog(applicationActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                if (((int) (Math.log10(dayOfMonth) + 1) == 1)) {
                                    ToDate = year + "-" + (monthOfYear + 1) + "-0" + dayOfMonth;
                                } else {
                                    ToDate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                                }
                                ToDate_tv.setText(ToDate);
                                Toast.makeText(applicationActivity.this, ToDate, Toast.LENGTH_SHORT).show();
                            }
                        }, year, month, day);
                picker.show();
            }
        });


        save_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveApplication();
            }
        });
    }

    private void saveApplication() {
        condition = reasons.get(spinner.getSelectedItemPosition());
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        final String currentDateTime = df.format(currentTime);
        SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
        final String currentDate = df1.format(currentTime);
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE); //user ip
        final String selecteduserip = Formatter.formatIpAddress(wifiManager.getConnectionInfo().getIpAddress());//select user ip
        if (TextUtils.isEmpty(FromDate) || TextUtils.isEmpty(ToDate) || TextUtils.isEmpty(Reason_e.getText().toString())) {
            Toast.makeText(this, "Please Fill All Fiels...", Toast.LENGTH_SHORT).show();
        } else {
            progressDialog.show();
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://minhasdemo1.000webhostapp.com/Attendance_test/insertApplication.php", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                        progressDialog.dismiss();
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        Toast.makeText(applicationActivity.this, response, Toast.LENGTH_SHORT).show();
                        emptyFields();
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
                    hashMap.put("AppliedFrom", FromDate);
                    hashMap.put("AppliedTo", ToDate);
                    hashMap.put("ApplyDate", currentDate);
                    hashMap.put("Reason", Reason_e.getText().toString());
                    hashMap.put("UserIP", selecteduserip);
                    hashMap.put("EntryTime", currentDateTime);
                    hashMap.put("UserId", UserId + "");
                    hashMap.put("ApplicationType", condition);
                    return hashMap;
                }
            };

            stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            RequestQueue requestQueue = Volley.newRequestQueue(applicationActivity.this);
            requestQueue.add(stringRequest);
        }

    }

    private void emptyFields() {
        FromDate_tv.setText("");
        FromDate = "";
        ToDate_tv.setText("");
        ToDate = "";
        Reason_e.setText("");
        spinner.setSelection(0);
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
        AlertDialog.Builder b = new AlertDialog.Builder(applicationActivity.this);
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

    private void initialization() {
        FromDate_tv = findViewById(R.id.textView4);
        ToDate_tv = findViewById(R.id.textView6);
        spinner = findViewById(R.id.spinner);
        Reason_e = findViewById(R.id.editText3);
        save_b = findViewById(R.id.button2);
        progressDialog = new ProgressDialog(applicationActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        reasons = new ArrayList<>();
        reasons.add("Normal");
        reasons.add("MTTP");
        reasons.add("Special");
        Intent intent = getIntent();
        EmployeeId = intent.getIntExtra("EmpId", 0);
        EmployeeName = intent.getStringExtra("EmpName");
        UserId = intent.getIntExtra("UserId", 0);
        EmployeeImage = intent.getStringExtra("EmployeeImage");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, reasons);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(applicationActivity.this, HomeActivity.class);
        intent.putExtra("EmpId", EmployeeId);
        intent.putExtra("EmpName", EmployeeName);
        intent.putExtra("UserId", UserId);
        intent.putExtra("EmployeeImage", EmployeeImage);
        startActivity(intent);
        finish();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }
}
