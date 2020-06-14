package com.example.attendance;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
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
import org.json.JSONObject;

import java.lang.annotation.ElementType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private EditText Password;
    private AutoCompleteTextView UserName;
    private Button Login;
    private ProgressDialog progressDialog;
    private int EmployeeId, UserId, TypeId;
    private String EmployeeName, EmployeeImage;
    private SharedPreferences sharedpreferences, sharedpreferences1;
    private SharedPreferences.Editor editor, editor1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialization();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginButton();
            }
        }); //login button

    }

    private void LoginButton() {

        if (TextUtils.isEmpty(UserName.getText().toString()) || TextUtils.isEmpty(Password.getText().toString())) {
            Toast.makeText(this, "Please Select UserName & Password...", Toast.LENGTH_SHORT).show();
        } else {
            progressDialog.show();
            editor.putString("SuggestionsList", UserName.getText().toString());
            editor.apply();
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://minhasdemo1.000webhostapp.com/Attendance_test/loginUser.php", new Response.Listener<String>() {
                public void onResponse(String response) {
                    try {
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        progressDialog.dismiss();
                        String temp = response.substring(response.indexOf("["));
                        String temp2 = response.substring(0, response.indexOf("["));
                        if (temp2.equals("Record Found.")) {
                            JSONArray jsonArry = new JSONArray(temp);
                            for (int i = 0; i < jsonArry.length(); i++) {
                                JSONObject jsonObject = jsonArry.getJSONObject(i);
                                UserId = jsonObject.getInt("UserId");
                                EmployeeId = jsonObject.getInt("EmpId");
                                EmployeeName = jsonObject.getString("EntityName");
                                TypeId = jsonObject.getInt("TypeId");
                                EmployeeImage = jsonObject.getString("EImage");
                                if (!jsonObject.getString("TimeIn").equals(null)) {
                                    if (jsonObject.getString("TimeOut").equals(null)) {
                                        editor1.putInt("Flag", 1);
                                        editor1.putString("DateTimeCheck", jsonObject.getString("TimeIn"));
                                        editor1.apply();
                                    }
                                } else if (!jsonObject.getString("TimeOut").equals(null)) {
                                    if (jsonObject.getString("TimeIn").equals(null)) {
                                        editor1.putInt("Flag", 2);
                                        editor1.putString("DateTimeCheck", jsonObject.getString("TimeOut"));
                                        editor1.apply();
                                    }
                                }
                            }
                            if (TypeId >= 5) {
                                Intent intent = new Intent(MainActivity.this, Home2Activity.class);
                                intent.putExtra("EmpId", EmployeeId);
                                intent.putExtra("EmpName", EmployeeName);
                                intent.putExtra("UserId", UserId);
                                intent.putExtra("EmployeeImage", EmployeeImage);
                                startActivity(intent);
                                Toast.makeText(MainActivity.this, "Welcome " + EmployeeName, Toast.LENGTH_LONG).show();
                                finish();
                                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                            } else if (TypeId < 5 && TypeId >= 2) {
                                Intent intent = new Intent(MainActivity.this, Home3Activity.class);
                                intent.putExtra("EmpId", EmployeeId);
                                intent.putExtra("EmpName", EmployeeName);
                                intent.putExtra("UserId", UserId);
                                startActivity(intent);
                                Toast.makeText(MainActivity.this, "Welcome " + EmployeeName, Toast.LENGTH_LONG).show();
                                finish();
                                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                            } else if (TypeId < 2) {
                                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                                intent.putExtra("EmpId", EmployeeId);
                                intent.putExtra("EmpName", EmployeeName);
                                intent.putExtra("UserId", UserId);
                                intent.putExtra("EmployeeImage", EmployeeImage);
                                startActivity(intent);
                                Toast.makeText(MainActivity.this, "Welcome " + EmployeeName, Toast.LENGTH_LONG).show();
                                finish();
                                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                            }
                        } else {
                            Toast.makeText(MainActivity.this, "UserName & Password Is Incorrect.Try Again!!!", Toast.LENGTH_SHORT).show();
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
                    hashMap.put("UserName", UserName.getText().toString());
                    hashMap.put("Password", Password.getText().toString());
                    return hashMap;
                }
            };
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
            requestQueue.add(stringRequest);
        }
    }

    private void initialization() {
        UserName = findViewById(R.id.editText);
        Password = findViewById(R.id.editText2);
        Login = findViewById(R.id.button);
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        sharedpreferences = getSharedPreferences("suggestions", MODE_PRIVATE);
        editor = sharedpreferences.edit();
        String Suggestions = sharedpreferences.getString("SuggestionsList", "");
        List<String> list = new ArrayList<>();
        list.add(Suggestions);
        ArrayAdapter usernames = new ArrayAdapter(MainActivity.this, android.R.layout.simple_dropdown_item_1line, list);
        UserName.setAdapter(usernames);
        sharedpreferences1 = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        editor1 = sharedpreferences.edit();
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
        AlertDialog.Builder b = new AlertDialog.Builder(MainActivity.this);
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
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        super.onBackPressed();
    }
}
