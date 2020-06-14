package com.example.attendance;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.baoyz.widget.PullRefreshLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewUserActivity2 extends AppCompatActivity {

    private DatePickerDialog picker;
    private ProgressDialog progressDialog;
    private int EmployeeId, UserId, LastModifyById, DepartmentId = 0;
    private String EmployeeName,EmployeeImage, FirstName, LastName, Address, PhoneNo, AccountNo,
            Cnic, Dob, CnicExp, LastEditTime, joiningDate, fatherName, Designation,
            salaryPackage, email_addr, username, password, UserIp, EntryTime, imageString;
    private EditText salaryPackage_e, UserName_e, Password_e, Designation_e;
    private Spinner Department_e;
    private Button addUser_b;
    private TextView joiningdate_e;
    private List<Integer> DepartmentIdList;
    private List<String> DepartmentNameList;
    private ImageButton addNewDepart;
    private PullRefreshLayout pullRefreshLayout_JHome;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user2);
        initialization();
        getDepartment();

        addUser_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addUSer();
            }
        });
        joiningdate_e.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                final int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(NewUserActivity2.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                joiningDate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                                joiningdate_e.setText(joiningDate);
                                Toast.makeText(NewUserActivity2.this, joiningDate, Toast.LENGTH_SHORT).show();
                            }
                        }, year, month, day);
                picker.show();
            }
        });

        Department_e.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    DepartmentId = DepartmentIdList.get(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        addNewDepart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDepartment();
            }
        });


        pullRefreshLayout_JHome.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDepartment();
            }
        });
    }

    private void addDepartment() {
        AlertDialog.Builder builder = new AlertDialog.Builder(NewUserActivity2.this);
        builder.setTitle("New Department Name");
        final EditText depart = new EditText(NewUserActivity2.this);
        depart.setBackground(getResources().getDrawable(R.drawable.listviewitem));
        depart.setPadding(30, 5, 20, 5);
        depart.setHint("Department Name:");
        depart.setMinimumHeight(150);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            depart.setElevation(20f);
        }
        final LinearLayout linearLayout = new LinearLayout(NewUserActivity2.this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setPadding(20, 20, 20, 20);
        linearLayout.addView(depart);
        builder.setView(linearLayout);

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (TextUtils.isEmpty(depart.getText().toString())) {
                    Toast.makeText(NewUserActivity2.this, "Enter Departmant Name Properly", Toast.LENGTH_SHORT).show();
                } else {
                    progressDialog.show();
                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://minhasdemo1.000webhostapp.com/Attendance_test/addnewDepartment.php", new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            Toast.makeText(NewUserActivity2.this, response, Toast.LENGTH_SHORT).show();
                            getDepartment();
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
                            hashMap.put("DepartmentName", depart.getText().toString());
                            return hashMap;
                        }
                    };

                    stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    RequestQueue requestQueue = Volley.newRequestQueue(NewUserActivity2.this);
                    requestQueue.add(stringRequest);
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

    private void getDepartment() {
        progressDialog.show();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://minhasdemo1.000webhostapp.com/Attendance_test/getDepartments.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                DepartmentNameList.clear();
                DepartmentIdList.clear();
                DepartmentId = 0;
                DepartmentIdList.add(0);
                DepartmentNameList.add("Select Department");
                try {
                    JSONArray jsonArry = new JSONArray(response);
                    for (int i = 0; i < jsonArry.length(); i++) {
                        JSONObject jsonObject = jsonArry.getJSONObject(i);
                        DepartmentIdList.add(jsonObject.getInt("Id"));
                        DepartmentNameList.add(jsonObject.getString("Department"));
                    }
                    ArrayAdapter adapter = new ArrayAdapter(NewUserActivity2.this, android.R.layout.simple_spinner_item, DepartmentNameList);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    adapter.notifyDataSetChanged();
                    adapter.notifyDataSetInvalidated();
                    Department_e.setAdapter(adapter);
                    Department_e.setSelection(0);
                    pullRefreshLayout_JHome.setRefreshing(false);
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

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(NewUserActivity2.this);
        requestQueue.add(stringRequest);

    }

    private void addUSer() {
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        LastEditTime = df.format(currentTime);
        EntryTime = LastEditTime;
        username = UserName_e.getText().toString();
        password = Password_e.getText().toString();
        joiningDate = joiningdate_e.getText().toString();
        salaryPackage = salaryPackage_e.getText().toString();
        Designation = Designation_e.getText().toString();


        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password) || DepartmentId == 0) {
            Toast.makeText(NewUserActivity2.this, "Please Enter Username & Password & Department...", Toast.LENGTH_SHORT).show();
        } else {

            progressDialog.show();
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://minhasdemo1.000webhostapp.com/Attendance_test/AddNewUser.php", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    progressDialog.dismiss();
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    Toast.makeText(NewUserActivity2.this, response, Toast.LENGTH_SHORT).show();
                    if (response.equals("Username Already Exists Try Again...")) {
                        Toast.makeText(NewUserActivity2.this, response, Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = new Intent(NewUserActivity2.this, Home2Activity.class);
                        intent.putExtra("EmpId", EmployeeId);
                        intent.putExtra("EmpName", EmployeeName);
                        intent.putExtra("UserId", UserId);
                        intent.putExtra("EmployeeImage", EmployeeImage);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
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
                    hashMap.put("FirstName", FirstName);
                    hashMap.put("LastName", LastName);
                    hashMap.put("FatherName", fatherName);
                    hashMap.put("SalaryPackage", salaryPackage);
                    hashMap.put("Address", Address);
                    hashMap.put("AccountNo", AccountNo);
                    hashMap.put("PhoneNo", PhoneNo);
                    hashMap.put("NICNo", Cnic);
                    hashMap.put("DOB", Dob);
                    hashMap.put("CNICExp", CnicExp);
                    hashMap.put("LastEditTime", LastEditTime);
                    hashMap.put("LastModifyById", LastModifyById + "");
                    hashMap.put("EImage", imageString);
                    hashMap.put("Email", email_addr);
                    hashMap.put("UserName", username);
                    hashMap.put("Password", password);
                    hashMap.put("JoiningDate", joiningDate);
                    hashMap.put("EntryTime", EntryTime);
                    hashMap.put("UserIp", UserIp);
                    hashMap.put("Designation", Designation);
                    hashMap.put("DepartmentId", DepartmentId + "");

                    return hashMap;
                }
            };

            stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            RequestQueue requestQueue = Volley.newRequestQueue(NewUserActivity2.this);
            requestQueue.add(stringRequest);

        }

    }

    private void initialization() {
        Intent intent = getIntent();
        EmployeeId = intent.getIntExtra("EmpId", 0);
        EmployeeName = intent.getStringExtra("EmpName");
        EmployeeImage = intent.getStringExtra("EmployeeImage");
        UserId = intent.getIntExtra("UserId", 0);
        FirstName = intent.getStringExtra("FirstName");
        LastName = intent.getStringExtra("LastName");
        fatherName = intent.getStringExtra("FatherName");
        PhoneNo = intent.getStringExtra("PhoneNo");
        email_addr = intent.getStringExtra("EmailAdrress");
        AccountNo = intent.getStringExtra("AccountNo");
        Cnic = intent.getStringExtra("Cnic");
        Dob = intent.getStringExtra("Dob");
        CnicExp = intent.getStringExtra("CnicExp");
        Address = intent.getStringExtra("Address");
        imageString = intent.getStringExtra("Image");
        LastModifyById = UserId;

        progressDialog = new ProgressDialog(NewUserActivity2.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        joiningdate_e = findViewById(R.id.editText5_admin);
        addUser_b = findViewById(R.id.button15_admin2);
        salaryPackage_e = findViewById(R.id.editText5_admin5);
        UserName_e = findViewById(R.id.editText5_admin8);
        Password_e = findViewById(R.id.editText5_admin7);
        Designation_e = findViewById(R.id.editText5_admin2);
        Department_e = findViewById(R.id.editText5_admin6);
        addNewDepart = findViewById(R.id.imageButton3);
        pullRefreshLayout_JHome = findViewById(R.id.pullRefresh);
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE); //user ip
        UserIp = Formatter.formatIpAddress(wifiManager.getConnectionInfo().getIpAddress());//select user ip

        DepartmentIdList = new ArrayList<>();
        DepartmentNameList = new ArrayList<>();


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
        AlertDialog.Builder b = new AlertDialog.Builder(NewUserActivity2.this);
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
        finish();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }
}
