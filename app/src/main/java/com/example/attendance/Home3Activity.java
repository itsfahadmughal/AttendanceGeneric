package com.example.attendance;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
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
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Home3Activity extends AppCompatActivity {

    private boolean doubleBackToExitPressedOnce = false;
    private Button checkin, checkout;
    private CircleImageView profile_b;
    private int EmployeeId, UserId, selectedEmpId = 0;
    private TextView EntityName;
    private String EmployeeName;
    private AutoCompleteTextView edittext_e;
    private ProgressDialog progressDialog;
    private List<Integer> EmpIdList;
    private List<String> EmpNameList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home3);
        inialization();

        getEmployees();

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
    }

    private void getEmployees() {
        progressDialog.show();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://minhasdemo1.000webhostapp.com/Attendance_test/getEmployees.php", new Response.Listener<String>() {
            public void onResponse(String response) {
                try {
                    EmpIdList.clear();
                    EmpNameList.clear();
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    progressDialog.dismiss();
                    String temp = response.substring(response.indexOf("["));
                    JSONArray jsonArry = new JSONArray(temp);
                    Toast.makeText(Home3Activity.this, response.substring(0, response.indexOf("[")), Toast.LENGTH_SHORT).show();
                    for (int i = 0; i < jsonArry.length(); i++) {
                        JSONObject jsonObject = jsonArry.getJSONObject(i);
                        EmpNameList.add(jsonObject.getString("EmployeeName"));
                        EmpIdList.add(jsonObject.getInt("EmployeeId"));
                    }

                    ArrayAdapter<String> arrayAdapter4 = new ArrayAdapter<String>(Home3Activity.this, android.R.layout.simple_dropdown_item_1line, EmpNameList);
                    edittext_e.setAdapter(arrayAdapter4);
                    edittext_e.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            int i = EmpNameList.indexOf(edittext_e.getText().toString());
                            selectedEmpId = EmpIdList.get(i);
                            edittext_e.setText(EmpNameList.get(i));
                        }
                    });//item selected

                } catch (Exception e) {
                    e.printStackTrace();
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    progressDialog.dismiss();
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
        RequestQueue requestQueue = Volley.newRequestQueue(Home3Activity.this);
        requestQueue.add(stringRequest);

    }


    private void checkoutPress() {
        int i = EmpNameList.indexOf(edittext_e.getText().toString());
        if (i != -1) {
            int tempselectedEmpId = EmpIdList.get(i);
            Intent intent = new Intent(Home3Activity.this, CheckOutActivity.class);
            intent.putExtra("EmpId", selectedEmpId);
            intent.putExtra("EmpName", EmployeeName);
            intent.putExtra("UserId", UserId);
            intent.putExtra("FlagUser", 3);
            startActivity(intent);
            finish();
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        } else {
            Toast.makeText(this, "Please Select Employee Name Correctly...", Toast.LENGTH_LONG).show();
        }
    }

    private void checkInPress() {
        int i = EmpNameList.indexOf(edittext_e.getText().toString());
        if (i != -1) {
            int tempselectedEmpId = EmpIdList.get(i);
            Intent intent = new Intent(Home3Activity.this, CheckInActivity.class);
            intent.putExtra("EmpId", selectedEmpId);
            intent.putExtra("EmpName", EmployeeName);
            intent.putExtra("UserId", UserId);
            intent.putExtra("FlagUser", 3);
            startActivity(intent);
            finish();
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        } else {
            Toast.makeText(this, "Please Select Employee Name Correctly...", Toast.LENGTH_LONG).show();
        }
    }


    private void inialization() {
        EntityName = findViewById(R.id.textView_home3);
        checkin = findViewById(R.id.button3_home3);
        checkout = findViewById(R.id.button6_home3);
        profile_b = findViewById(R.id.dp_Profile_home3);
        edittext_e = findViewById(R.id.autoCompleteTextView_home3);
        progressDialog = new ProgressDialog(Home3Activity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        EmpIdList = new ArrayList<>();
        EmpNameList = new ArrayList<>();
        Intent intent = getIntent();
        EmployeeId = intent.getIntExtra("EmpId", 0);
        EmployeeName = intent.getStringExtra("EmpName");
        UserId = intent.getIntExtra("UserId", 0);
        EntityName.setText(EmployeeName);
        RequestOptions transcodeTypeRequestBuilder = new RequestOptions().error(R.drawable.avatar12);
        String imageUrl = "https://minhasdemo1.000webhostapp.com/Attendance_test/Emp/kkk.jpg";
        Glide.with(Home3Activity.this).load(imageUrl).apply(transcodeTypeRequestBuilder).into(profile_b);

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
        AlertDialog.Builder b = new AlertDialog.Builder(Home3Activity.this);
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
