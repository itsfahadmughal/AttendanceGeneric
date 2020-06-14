package com.example.attendance;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CenterInside;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

public class AssignTaskAdminActivity extends AppCompatActivity {

    private DatePickerDialog picker;
    private ProgressDialog progressDialog;
    private int EmployeeId, UserId, selectedDepartId = 0, count, daysInMonth;
    private String EmployeeName, EmployeeImage, FromDate, ToDate, desciption, selectedEmpId_str, selectedEmpName_str;
    private ListView listView;
    private Button addholiday, addemployee, generatepayroll;
    private singlten_admin_assign_task adp;
    private ArrayList<workersList> workers;
    private List<String> DepartmentNameList;
    private List<Integer> DepartmentIdList;
    private int fromdays, frommonths, todays, tomonths, daysdiff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign_task_admin);
        initailization();
        getDepartments();
        addholiday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertHoliday();
            }
        });
        addemployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addemp();
            }
        });

        generatepayroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                genpayroll();
            }
        });


    }

    private void getDepartments() {
        progressDialog.show();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://minhasdemo1.000webhostapp.com/Attendance_test/getDepartments.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArry = new JSONArray(response);
                    DepartmentIdList.clear();
                    DepartmentNameList.clear();
                    DepartmentNameList.add("All Departments");
                    DepartmentIdList.add(0);
                    for (int i = 0; i < jsonArry.length(); i++) {
                        JSONObject jsonObject = jsonArry.getJSONObject(i);
                        DepartmentIdList.add(jsonObject.getInt("Id"));
                        DepartmentNameList.add(jsonObject.getString("Department"));
                    }
                    progressDialog.dismiss();
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                progressDialog.dismiss();
                checkInternetConnection(error);
            }
        }) {
            protected Map<String, String> getParams() {
                HashMap<String, String> hashMap = new HashMap<>();
                return hashMap;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(AssignTaskAdminActivity.this);
        requestQueue.add(stringRequest);

    }

    private void genpayroll() {
        count = 0;
        if (adp == null) {
            Toast.makeText(this, "Add Employees First...", Toast.LENGTH_SHORT).show();
        } else {
            for (workersList p : adp.getBox()) {
                if (p.box) {
                    count++;
                    selectedEmpId_str += p.emp_id + ",";
                    selectedEmpName_str += p.emp_name + ",";
                }
            }


            AlertDialog.Builder builder1 = new AlertDialog.Builder(AssignTaskAdminActivity.this);
            builder1.setTitle("Generate Payroll");
            final TextView tv = new TextView(AssignTaskAdminActivity.this);
            final TextView tv1 = new TextView(AssignTaskAdminActivity.this);
            tv.setWidth(300);
            tv1.setWidth(300);
            tv.setHeight(120);
            tv1.setHeight(120);
            tv.setHint("FromDate");
            tv1.setHint("ToDate");
            tv.setTextSize(17f);
            tv1.setTextSize(17f);
            tv.setGravity(Gravity.CENTER);
            tv1.setGravity(Gravity.CENTER);
            tv.setPadding(5, 5, 5, 5);
            tv1.setPadding(5, 5, 5, 5);
            tv.setTextColor(Color.BLACK);
            tv1.setTextColor(Color.BLACK);
            LinearLayout linearLayout = new LinearLayout(AssignTaskAdminActivity.this);
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout.setGravity(Gravity.CENTER);
            linearLayout.addView(tv);
            linearLayout.addView(tv1);

            builder1.setView(linearLayout);

            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Calendar cldr = Calendar.getInstance();
                    final int day = cldr.get(Calendar.DAY_OF_MONTH);
                    final int month = cldr.get(Calendar.MONTH);
                    int year = cldr.get(Calendar.YEAR);
                    // date picker dialog
                    picker = new DatePickerDialog(AssignTaskAdminActivity.this,
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                    frommonths = (monthOfYear + 1);
                                    fromdays = dayOfMonth;
                                    if (((int) (Math.log10(dayOfMonth) + 1) == 1)) {
                                        FromDate = year + "-" + (monthOfYear + 1) + "-0" + dayOfMonth;
                                    } else {
                                        FromDate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                                    }
                                    tv.setText(FromDate);
                                }
                            }, year, month, day);
                    picker.show();
                }
            });
            tv1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Calendar cldr = Calendar.getInstance();
                    int day = cldr.get(Calendar.DAY_OF_MONTH);
                    int month = cldr.get(Calendar.MONTH);
                    int year = cldr.get(Calendar.YEAR);
                    // date picker dialog
                    picker = new DatePickerDialog(AssignTaskAdminActivity.this,
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                        YearMonth yearMonthObject = YearMonth.of(year, (monthOfYear + 1));
                                        daysInMonth = yearMonthObject.lengthOfMonth();
                                        tomonths = (monthOfYear + 1);
                                        todays = dayOfMonth;
                                    }
                                    if (((int) (Math.log10(dayOfMonth) + 1) == 1)) {
                                        ToDate = year + "-" + (monthOfYear + 1) + "-0" + dayOfMonth;
                                    } else {
                                        ToDate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                                    }
                                    tv1.setText(ToDate);
                                }
                            }, year, month, day);
                    picker.show();
                }
            });

            builder1.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    daysdiff = (todays - fromdays) + 1;
                    if (TextUtils.isEmpty(selectedEmpId_str) || count == 0 || TextUtils.isEmpty(FromDate) || TextUtils.isEmpty(ToDate) || daysdiff > 31 || frommonths != tomonths) {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(AssignTaskAdminActivity.this);
                        builder.setTitle("Alert!");
                        builder.setMessage("Please Enter All Fields OR Select Only One Month");
                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        builder.show();

                    } else {

                        AlertDialog.Builder builder = new AlertDialog.Builder(AssignTaskAdminActivity.this);
                        builder.setTitle("Confirmation Alert!!!");
                        builder.setMessage("Do You Want To Proceed...\nFromDate : " + FromDate + "\nToDate : " + ToDate + "\nEmployees : " + selectedEmpName_str.replaceAll("null", ""));

                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Date currentTime = Calendar.getInstance().getTime();
                                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                                final String currentDateTime = df.format(currentTime);
                                WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE); //user ip
                                final String selecteduserip = Formatter.formatIpAddress(wifiManager.getConnectionInfo().getIpAddress());//select us
                                progressDialog.show();
                                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://minhasdemo1.000webhostapp.com/Attendance_test/generatePayroll.php", new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                        progressDialog.dismiss();
                                        Toast.makeText(AssignTaskAdminActivity.this, response, Toast.LENGTH_SHORT).show();
                                        emptyFields();
                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError volleyError) {
                                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                        progressDialog.dismiss();
                                        checkInternetConnection(volleyError);
                                        emptyFields();
                                    }
                                }) {
                                    protected Map<String, String> getParams() {
                                        HashMap<String, String> hashMap = new HashMap<>();
                                        hashMap.put("EmpNameList", selectedEmpName_str.replaceAll("null", ""));
                                        hashMap.put("EmpIdList", selectedEmpId_str.replaceAll("null", ""));
                                        hashMap.put("NoOfEmp", count + "");
                                        hashMap.put("UserId", UserId + "");
                                        hashMap.put("EntryTime", currentDateTime);
                                        hashMap.put("UserIP", selecteduserip);
                                        hashMap.put("FromDate", FromDate);
                                        hashMap.put("ToDate", ToDate);

                                        return hashMap;
                                    }
                                };
                                stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
                                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                                RequestQueue requestQueue = Volley.newRequestQueue(AssignTaskAdminActivity.this);
                                requestQueue.add(stringRequest);

                            }


                        }); //positive button
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                selectedEmpId_str = "";
                                selectedEmpName_str = "";
                            }
                        });

                        builder.show();

                    }
                }
            });

            builder1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            builder1.show();

        }//end of else chek adp null
    }//end of function

    private void emptyFields() {
        selectedEmpId_str = "";
        selectedEmpName_str = "";
    }

    private void addemp() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AssignTaskAdminActivity.this);
        builder.setTitle("Add Employees From Departments");
        final Spinner spinner = new Spinner(AssignTaskAdminActivity.this);
        spinner.setMinimumWidth(550);
        spinner.setMinimumHeight(150);
        spinner.setPadding(20, 5, 20, 5);
        LinearLayout linearLayout1 = new LinearLayout(AssignTaskAdminActivity.this);
        linearLayout1.setOrientation(LinearLayout.VERTICAL);
        linearLayout1.setGravity(Gravity.CENTER);
        linearLayout1.setPadding(10, 20, 10, 20);
        linearLayout1.addView(spinner);
        builder.setView(linearLayout1);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, DepartmentNameList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectedDepartId = DepartmentIdList.get(spinner.getSelectedItemPosition());
                getEmployees();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }

    private void getEmployees() {
        progressDialog.show();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://minhasdemo1.000webhostapp.com/Attendance_test/payrollEmp.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    workers.clear();
                    String temp = response.substring(response.indexOf("["));
                    JSONArray jsonArry = new JSONArray(temp);
                    Toast.makeText(AssignTaskAdminActivity.this, response.substring(0, response.indexOf("[")), Toast.LENGTH_SHORT).show();
                    for (int i = 0; i < jsonArry.length(); i++) {
                        JSONObject jsonObject = jsonArry.getJSONObject(i);
                        workers.add(new workersList(jsonObject.getInt("EmployeeId"), jsonObject.getString("EmployeeCode"),
                                jsonObject.getString("EmployeeName"), false));
                    }
                    adp = new singlten_admin_assign_task(AssignTaskAdminActivity.this, workers);
                    listView.setAdapter(adp);
                    progressDialog.dismiss();
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                progressDialog.dismiss();
                checkInternetConnection(error);
            }
        }) {
            protected Map<String, String> getParams() {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("departId", selectedDepartId + "");
                return hashMap;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(AssignTaskAdminActivity.this);
        requestQueue.add(stringRequest);
    }//get employees

    private void insertHoliday() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AssignTaskAdminActivity.this);
        builder.setTitle("Add Holidays");
        final TextView tv = new TextView(AssignTaskAdminActivity.this);
        final TextView tv1 = new TextView(AssignTaskAdminActivity.this);
        final EditText editText = new EditText(AssignTaskAdminActivity.this);
        tv.setWidth(300);
        tv1.setWidth(300);
        tv.setHeight(120);
        tv1.setHeight(120);
        tv.setHint("FromDate");
        tv1.setHint("ToDate");
        tv.setTextSize(17f);
        tv1.setTextSize(17f);
        tv.setGravity(Gravity.CENTER);
        tv1.setGravity(Gravity.CENTER);
        tv.setPadding(5, 5, 5, 5);
        tv1.setPadding(5, 5, 5, 5);
        tv.setTextColor(Color.BLACK);
        tv1.setTextColor(Color.BLACK);
        editText.setWidth(550);
        editText.setHeight(150);
        editText.setMaxLines(2);
        editText.setHint("Description");
        editText.setPadding(20, 5, 20, 5);
        LinearLayout linearLayout = new LinearLayout(AssignTaskAdminActivity.this);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setGravity(Gravity.CENTER);
        linearLayout.addView(tv);
        linearLayout.addView(tv1);

        LinearLayout linearLayout1 = new LinearLayout(AssignTaskAdminActivity.this);
        linearLayout1.setOrientation(LinearLayout.VERTICAL);
        linearLayout1.setGravity(Gravity.CENTER);
        linearLayout1.setPadding(10, 20, 10, 20);
        linearLayout1.addView(linearLayout);
        linearLayout1.addView(editText);

        builder.setView(linearLayout1);

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                final int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(AssignTaskAdminActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                if (((int) (Math.log10(dayOfMonth) + 1) == 1)) {
                                    FromDate = year + "-" + (monthOfYear + 1) + "-0" + dayOfMonth;
                                } else {
                                    FromDate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                                }
                                tv.setText(FromDate);
                            }
                        }, year, month, day);
                picker.show();
            }
        });
        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(AssignTaskAdminActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                if (((int) (Math.log10(dayOfMonth) + 1) == 1)) {
                                    ToDate = year + "-" + (monthOfYear + 1) + "-0" + dayOfMonth;
                                } else {
                                    ToDate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                                }
                                tv1.setText(ToDate);
                            }
                        }, year, month, day);
                picker.show();
            }
        });

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                desciption = editText.getText().toString();
                if (TextUtils.isEmpty(desciption) || TextUtils.isEmpty(FromDate) || TextUtils.isEmpty(ToDate)) {
                    Toast.makeText(AssignTaskAdminActivity.this, "Select All Fields...", Toast.LENGTH_SHORT).show();
                } else {
                    Date currentTime = Calendar.getInstance().getTime();
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    final String currentDateTime = df.format(currentTime);
                    WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE); //user ip
                    final String selecteduserip = Formatter.formatIpAddress(wifiManager.getConnectionInfo().getIpAddress());//select us
                    progressDialog.show();
                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://minhasdemo1.000webhostapp.com/Attendance_test/insertHolidays.php", new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            Toast.makeText(AssignTaskAdminActivity.this, response, Toast.LENGTH_SHORT).show();
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
                            hashMap.put("UserId", UserId + "");
                            hashMap.put("FromDate", FromDate);
                            hashMap.put("description", desciption);
                            hashMap.put("ToDate", ToDate);
                            hashMap.put("EntryTime", currentDateTime);
                            hashMap.put("UserIP", selecteduserip);
                            return hashMap;
                        }
                    };

                    stringRequest.setRetryPolicy(new
                            DefaultRetryPolicy(10000,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    RequestQueue requestQueue = Volley.newRequestQueue(AssignTaskAdminActivity.this);
                    requestQueue.add(stringRequest);


                }

            }
        });


        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();

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
        androidx.appcompat.app.AlertDialog.Builder b = new androidx.appcompat.app.AlertDialog.Builder(AssignTaskAdminActivity.this);
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

    private void initailization() {
        listView = findViewById(R.id.listview_payrolladmin);
        addemployee = findViewById(R.id.addemployees_admin);
        addholiday = findViewById(R.id.addholiday_admin);
        generatepayroll = findViewById(R.id.generatePayroll_admin);
        Intent intent = getIntent();
        EmployeeId = intent.getIntExtra("EmpId", 0);
        EmployeeName = intent.getStringExtra("EmpName");
        UserId = intent.getIntExtra("UserId", 0);
        EmployeeImage = intent.getStringExtra("EmployeeImage");
        progressDialog = new ProgressDialog(AssignTaskAdminActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        workers = new ArrayList<>();
        DepartmentNameList = new ArrayList<>();
        DepartmentIdList = new ArrayList<>();
    }

    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(AssignTaskAdminActivity.this, Home2Activity.class);
        intent.putExtra("EmpId", EmployeeId);
        intent.putExtra("EmpName", EmployeeName);
        intent.putExtra("UserId", UserId);
        intent.putExtra("EmployeeImage", EmployeeImage);
        startActivity(intent);
        finish();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

}
