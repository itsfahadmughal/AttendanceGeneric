package com.example.attendance;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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
import android.provider.MediaStore;
import android.text.format.Formatter;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
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
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CheckInActivity extends AppCompatActivity {

    private ImageView imageDp;
    private Button Upload_b, CheckIn_b;
    private Bitmap bitmap;
    private ProgressDialog progressDialog;
    private int EmployeeId, UserId, flagUser;
    private String selecteduserip, currentDateTime, currentDate, EmployeeName, EmployeeImage, EntryTime;
    private SharedPreferences sharedpreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_in);
        inialization();

        Upload_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadImage();
            }
        }); //upload imagebutton

        CheckIn_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkInPress();
            }
        });

    }

    private void checkInPress() {
        if (EmployeeId != 0) {
            Date currentTime = Calendar.getInstance().getTime();
            SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
            currentDateTime = df.format(currentTime);
            SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
            currentDate = df1.format(currentTime);
            SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            EntryTime = df2.format(currentTime);
            progressDialog.show();
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://minhasdemo1.000webhostapp.com/Attendance_test/checkinAttendance.php", new Response.Listener<String>() {

                public void onResponse(String response) {
                    try {
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        progressDialog.dismiss();
                        if (response.equals("Attendance Marked1") || response.equals("Attendance Marked2")) {
                            Toast.makeText(CheckInActivity.this, "CheckIn Succesfully", Toast.LENGTH_SHORT).show();
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    if (flagUser == 3) {
                                        Intent intent = new Intent(CheckInActivity.this, Home3Activity.class);
                                        intent.putExtra("EmpId", EmployeeId);
                                        intent.putExtra("EmpName", EmployeeName);
                                        intent.putExtra("UserId", UserId);
                                        startActivity(intent);
                                        finish();
                                        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                                    } else {
                                        Intent intent = new Intent(CheckInActivity.this, HomeActivity.class);
                                        intent.putExtra("EmpId", EmployeeId);
                                        intent.putExtra("EmpName", EmployeeName);
                                        intent.putExtra("UserId", UserId);
                                        intent.putExtra("EmployeeImage", EmployeeImage);
                                        editor.putInt("Flag", 1);
                                        editor.putString("DateTimeCheck", currentDateTime);
                                        editor.apply();
                                        startActivity(intent);
                                        finish();
                                        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                                    }
                                }
                            }, 1000);
                        } else {
                            Toast.makeText(CheckInActivity.this, response, Toast.LENGTH_SHORT).show();
                        }
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
                    hashMap.put("TimeIn", currentDateTime);
                    hashMap.put("EntryTime", EntryTime);
                    hashMap.put("UserId", UserId + "");
                    hashMap.put("UserIP", selecteduserip);

                    return hashMap;
                }
            };
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            RequestQueue requestQueue = Volley.newRequestQueue(CheckInActivity.this);
            requestQueue.add(stringRequest);
        } else {
            Toast.makeText(this, "Login Again!!!", Toast.LENGTH_SHORT).show();
        }
    }

    private void UploadImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == -1) {
            bitmap = (Bitmap) data.getExtras().get("data");
            imageDp.setImageBitmap(bitmap);
            CheckIn_b.setVisibility(View.VISIBLE);
            Upload_b.setVisibility(View.INVISIBLE);
        }
    }

    private void inialization() {
        imageDp = findViewById(R.id.imageView3);
        Upload_b = findViewById(R.id.button7);
        CheckIn_b = findViewById(R.id.button8);
        progressDialog = new ProgressDialog(CheckInActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE); //user ip
        selecteduserip = Formatter.formatIpAddress(wifiManager.getConnectionInfo().getIpAddress());//select user ip

        Intent intent = getIntent();
        EmployeeId = intent.getIntExtra("EmpId", 0);
        EmployeeName = intent.getStringExtra("EmpName");
        UserId = intent.getIntExtra("UserId", 0);
        EmployeeImage = intent.getStringExtra("EmployeeImage");
        flagUser = intent.getIntExtra("FlagUser", 0);
        sharedpreferences = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
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
        AlertDialog.Builder b = new AlertDialog.Builder(CheckInActivity.this);
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
        if (flagUser == 3) {
            Intent intent = new Intent(CheckInActivity.this, Home3Activity.class);
            intent.putExtra("EmpName", EmployeeName);
            intent.putExtra("UserId", UserId);
            startActivity(intent);
            finish();
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        } else {
            Intent intent = new Intent(CheckInActivity.this, HomeActivity.class);
            intent.putExtra("EmpId", EmployeeId);
            intent.putExtra("EmpName", EmployeeName);
            intent.putExtra("UserId", UserId);
            intent.putExtra("EmployeeImage", EmployeeImage);
            startActivity(intent);
            finish();
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        }
    }
}
