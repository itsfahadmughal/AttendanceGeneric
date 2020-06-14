package com.example.attendance;

import androidx.activity.OnBackPressedDispatcherOwner;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.format.Formatter;
import android.util.Base64;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity {

    private boolean doubleBackToExitPressedOnce = false;
    private Button checkin, checkout, breakin, breakout, report, application,
            viewApplication_b, loan_b, task_submition, payroll_b, viewtasks_b;
    private CircleImageView profile_b;
    private int EmployeeId, FlagCheck, UserId, selectedMonth = 0, selectedYear = 0;
    private SharedPreferences sharedpreferences;
    private SharedPreferences.Editor editor;
    private TextView EntityName;
    private String EmployeeName, selecteduserip, currentDateTime, currentDate, EmployeeImage;
    private List<String> MessItems;
    private List<Double> TokenPrice;
    private ProgressDialog progressDialog;
    private Button demo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        inialization();
        demo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(HomeActivity.this, "Demo", Toast.LENGTH_SHORT).show();
            }
        });
        checkin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkInPress();
            }
        });

        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkoutPress();
            }
        });

        breakin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                breakinPress();
            }
        });

        breakout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                breakoutPress();
            }
        });

        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReportGenerate();
            }
        });

        application.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applicationsubmite();
            }
        });

        viewApplication_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewApplications();
            }
        });

        loan_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoanApplication();
            }
        });

        task_submition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                task_submitionFunc();
            }
        });

        payroll_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PayrollFunc();
            }
        });

        profile_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profile_func();
            }
        });

        viewtasks_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewTasks();
            }
        });

    }

    private void ViewTasks() {
        Intent intent = new Intent(HomeActivity.this, usertasksActivity.class);
        intent.putExtra("EmpId", EmployeeId);
        intent.putExtra("EmpName", EmployeeName);
        intent.putExtra("UserId", UserId);
        intent.putExtra("EmployeeImage", EmployeeImage);
        startActivity(intent);
        finish();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    private void profile_func() {
        Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
        intent.putExtra("EmpId", EmployeeId);
        intent.putExtra("EmpName", EmployeeName);
        intent.putExtra("UserId", UserId);
        intent.putExtra("EmployeeImage", EmployeeImage);
        startActivity(intent);
        finish();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    private void PayrollFunc() {
        Intent intent = new Intent(HomeActivity.this, PayrollActivity.class);
        intent.putExtra("EmpId", EmployeeId);
        intent.putExtra("EmpName", EmployeeName);
        intent.putExtra("UserId", UserId);
        intent.putExtra("EmployeeImage", EmployeeImage);
        startActivity(intent);
        finish();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    private void task_submitionFunc() {
        Intent intent = new Intent(HomeActivity.this, TaskSubActivity.class);
        intent.putExtra("EmpId", EmployeeId);
        intent.putExtra("EmpName", EmployeeName);
        intent.putExtra("UserId", UserId);
        intent.putExtra("EmployeeImage", EmployeeImage);
        startActivity(intent);
        finish();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    private void LoanApplication() {
        Intent intent = new Intent(HomeActivity.this, PerformanceActivity.class);
        intent.putExtra("EmpId", EmployeeId);
        intent.putExtra("EmpName", EmployeeName);
        intent.putExtra("UserId", UserId);
        intent.putExtra("EmployeeImage", EmployeeImage);
        startActivity(intent);
        finish();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    private void ViewApplications() {
        Intent intent = new Intent(HomeActivity.this, usertasksActivity.class);
        intent.putExtra("EmpId", EmployeeId);
        intent.putExtra("EmpName", EmployeeName);
        intent.putExtra("UserId", UserId);
        intent.putExtra("EmployeeImage", EmployeeImage);
        startActivity(intent);
        finish();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    private void callApi(final Double price) {
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        currentDateTime = df.format(currentTime);
        SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
        currentDate = df1.format(currentTime);
        progressDialog.show();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://192.168.100.115:8010/Attendance_test/insertMeal.php", new Response.Listener<String>() {

            public void onResponse(String response) {
                try {
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    progressDialog.dismiss();

                    AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                    builder.setTitle("Token For " + EmployeeName);
                    builder.setMessage("Generated Time : " + currentDateTime + "\n" + "Price : " + price + "\n");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    builder.show();

                } catch (Exception e) {
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
                hashMap.put("EmpId", EmployeeId + "");
                hashMap.put("Date", currentDate);
                hashMap.put("Time", currentDateTime);
                hashMap.put("Price", price + "");
                hashMap.put("UserId", UserId + "");
                hashMap.put("UserIP", selecteduserip);

                return hashMap;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(HomeActivity.this);
        requestQueue.add(stringRequest);
    }


    private void applicationsubmite() {
        Intent intent = new Intent(HomeActivity.this, applicationActivity.class);
        intent.putExtra("EmpId", EmployeeId);
        intent.putExtra("EmpName", EmployeeName);
        intent.putExtra("UserId", UserId);
        intent.putExtra("EmployeeImage", EmployeeImage);
        startActivity(intent);
        finish();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    private void ReportGenerate() {
        Intent intent = new Intent(HomeActivity.this, ReportActivity.class);
        intent.putExtra("EmpId", EmployeeId);
        intent.putExtra("EmpName", EmployeeName);
        intent.putExtra("UserId", UserId);
        intent.putExtra("EmployeeImage", EmployeeImage);
        startActivity(intent);
        finish();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    private void breakoutPress() {
        if (EmployeeId != 0) {
            Date currentTime = Calendar.getInstance().getTime();
            SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
            currentDateTime = df.format(currentTime);
            SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
            currentDate = df1.format(currentTime);
            progressDialog.show();
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://minhasdemo1.000webhostapp.com/Attendance_test/breakoutAttendance.php", new Response.Listener<String>() {
                public void onResponse(String response) {
                    try {
                        Toast.makeText(HomeActivity.this, response, Toast.LENGTH_SHORT).show();
                        if (response.equals("BreakIn Successfully...")) {
                            editor.putInt("FlagBreak", 2);
                            editor.putString("BreakTimeCheck", currentDateTime);
                            editor.apply();
                            breakout.setText(currentDateTime);
                            breakin.setEnabled(true);
                            breakout.setEnabled(false);
                        }
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        progressDialog.dismiss();

                    } catch (Exception e) {
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
                    hashMap.put("EmpId", EmployeeId + "");
                    hashMap.put("Date", currentDate);
                    hashMap.put("BreakOut", currentDateTime);
                    hashMap.put("UserId", UserId + "");
                    return hashMap;
                }
            };
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            RequestQueue requestQueue = Volley.newRequestQueue(HomeActivity.this);
            requestQueue.add(stringRequest);
        } else {
            Toast.makeText(this, "Login Again!!!", Toast.LENGTH_SHORT).show();
        }
    }

    private void breakinPress() {
        if (EmployeeId != 0) {
            Date currentTime = Calendar.getInstance().getTime();
            SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
            currentDateTime = df.format(currentTime);
            SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
            currentDate = df1.format(currentTime);
            progressDialog.show();
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://minhasdemo1.000webhostapp.com/Attendance_test/breakinAttendance.php", new Response.Listener<String>() {
                public void onResponse(String response) {
                    try {
                        Toast.makeText(HomeActivity.this, response, Toast.LENGTH_SHORT).show();
                        if (response.equals("BreakIn Successfully...")) {
                            editor.putInt("FlagBreak", 1);
                            editor.putString("BreakTimeCheck", currentDateTime);
                            editor.apply();
                            breakin.setText(currentDateTime);
                            breakin.setEnabled(false);
                        }
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        progressDialog.dismiss();

                    } catch (Exception e) {
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
                    hashMap.put("EmpId", EmployeeId + "");
                    hashMap.put("Date", currentDate);
                    hashMap.put("BreakIn", currentDateTime);
                    hashMap.put("UserId", UserId + "");
                    return hashMap;
                }
            };
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            RequestQueue requestQueue = Volley.newRequestQueue(HomeActivity.this);
            requestQueue.add(stringRequest);
        } else {
            Toast.makeText(this, "Login Again!!!", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkoutPress() {
        Intent intent = new Intent(HomeActivity.this, CheckOutActivity.class);
        intent.putExtra("EmpId", EmployeeId);
        intent.putExtra("EmpName", EmployeeName);
        intent.putExtra("UserId", UserId);
        intent.putExtra("EmployeeImage", EmployeeImage);
        startActivity(intent);
        finish();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    private void checkInPress() {
        Intent intent = new Intent(HomeActivity.this, CheckInActivity.class);
        intent.putExtra("EmpId", EmployeeId);
        intent.putExtra("EmpName", EmployeeName);
        intent.putExtra("UserId", UserId);
        intent.putExtra("EmployeeImage", EmployeeImage);
        startActivity(intent);
        finish();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    private void inialization() {
        demo = findViewById(R.id.button19);
        EntityName = findViewById(R.id.textView);
        checkin = findViewById(R.id.button3);
        breakin = findViewById(R.id.button4);
        breakout = findViewById(R.id.button5);
        checkout = findViewById(R.id.button6);
        report = findViewById(R.id.button9);
        application = findViewById(R.id.button10);
        viewApplication_b = findViewById(R.id.button11);
        loan_b = findViewById(R.id.button12);
        task_submition = findViewById(R.id.button13);
        viewtasks_b = findViewById(R.id.button17);
        payroll_b = findViewById(R.id.button14);
        profile_b = findViewById(R.id.dp_Profile);
        MessItems = new ArrayList<>();
        MessItems.add("--Select Token Price--");
        MessItems.add("Roti");
        MessItems.add("Tea");
        MessItems.add("Meal");
        MessItems.add("Drink");
        MessItems.add("Sting");
        TokenPrice = new ArrayList<>();
        TokenPrice.add(0.0);
        TokenPrice.add(4.0);
        TokenPrice.add(5.0);
        TokenPrice.add(10.0);
        TokenPrice.add(20.0);
        TokenPrice.add(25.0);

        progressDialog = new ProgressDialog(HomeActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE); //user ip
        selecteduserip = Formatter.formatIpAddress(wifiManager.getConnectionInfo().getIpAddress());//select user ip
        RequestOptions transcodeTypeRequestBuilder = new RequestOptions().error(R.drawable.avatar12);
        Intent intent = getIntent();
        sharedpreferences = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        EmployeeId = intent.getIntExtra("EmpId", 0);
        EmployeeName = intent.getStringExtra("EmpName");
        EmployeeImage = intent.getStringExtra("EmployeeImage");
        UserId = intent.getIntExtra("UserId", 0);
        FlagCheck = sharedpreferences.getInt("Flag", 0);
        EntityName.setText(EmployeeName);
        Glide.with(HomeActivity.this).load(Base64.decode(EmployeeImage, Base64.DEFAULT)).apply(transcodeTypeRequestBuilder).into(profile_b);
        if (FlagCheck == 1) {
            checkin.setEnabled(false);
            String currentDate = sharedpreferences.getString("DateTimeCheck", "");
            checkin.setText(currentDate);
            checkout.setEnabled(true);
            checkout.setText("Check-Out");
        } else if (FlagCheck == 2) {
            checkout.setEnabled(false);
            String currentDate = sharedpreferences.getString("DateTimeCheck", "");
            checkout.setText(currentDate);
            checkin.setEnabled(true);
            checkin.setText("Check-In");
        }

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
        AlertDialog.Builder b = new AlertDialog.Builder(HomeActivity.this);
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
