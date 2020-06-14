package com.example.attendance;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class editPayrollActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;
    private Integer UserId, EmpId, PayId, temp = 1,temp1 = 1;
    private String EmpName, Date;
    private Double TotalAmountPaid, ChecquePaid, CashPaid, Addition, Deduction;
    private TextView date_tv, name_tv;
    private EditText cash_e, cheque_e, addition_e, deduction_e, total_e;
    private Button save_b;
    private float nn,nnn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_payroll);

        initialization();

        save_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePayroll();
            }
        });


        cash_e.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String s1 = total_e.getText().toString();
                String s2 = cash_e.getText().toString();
                if (!s1.isEmpty() && !s2.isEmpty()) {
                    float n = Float.parseFloat(s1);
                    float n1 = Float.parseFloat(s2);
                    cheque_e.setText((n - n1) + "");

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        addition_e.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String s1 = total_e.getText().toString();
                String s2 = addition_e.getText().toString();
                if (!s1.isEmpty() && !s2.isEmpty()) {
                    if (temp == 1) {
                        nn = Float.parseFloat(s1);
                    }
                    temp++;
                    float n1 = Float.parseFloat(s2);
                    total_e.setText((nn + n1) + "");

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        deduction_e.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String s1 = total_e.getText().toString();
                String s2 = deduction_e.getText().toString();
                if (!s1.isEmpty() && !s2.isEmpty()) {
                    if (temp1 == 1) {
                        nnn = Float.parseFloat(s1);
                    }
                    temp1++;
                    float n1 = Float.parseFloat(s2);
                    total_e.setText((nnn - n1) + "");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    private void updatePayroll() {
        AlertDialog.Builder builder = new AlertDialog.Builder(editPayrollActivity.this);
        builder.setTitle("Confirmation!!!");
        builder.setMessage("Are You Sure You Want To Edit Payroll???" + "\n\nTotal Payroll Amount : " + total_e.getText().toString());
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                progressDialog.show();
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://minhasdemo1.000webhostapp.com/Attendance_test/updatePayroll.php", new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        Toast.makeText(editPayrollActivity.this, response, Toast.LENGTH_SHORT).show();
                        finish();
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
                        hashMap.put("EmpId", EmpId + "");
                        hashMap.put("PayId", PayId + "");
                        hashMap.put("CashPaid", cash_e.getText().toString());
                        hashMap.put("ChequePaid", cheque_e.getText().toString());
                        hashMap.put("Addition", addition_e.getText().toString());
                        hashMap.put("Deduction", deduction_e.getText().toString());
                        hashMap.put("TotalAmount", total_e.getText().toString());
                        hashMap.put("UserId", UserId + "");
                        return hashMap;
                    }
                };

                stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                RequestQueue requestQueue = Volley.newRequestQueue(editPayrollActivity.this);
                requestQueue.add(stringRequest);
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
        androidx.appcompat.app.AlertDialog.Builder b = new androidx.appcompat.app.AlertDialog.Builder(editPayrollActivity.this);
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
        progressDialog = new ProgressDialog(editPayrollActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        Intent intent = getIntent();
        UserId = intent.getIntExtra("UserId", 0);
        EmpId = intent.getIntExtra("EmpId", 0);
        EmpName = intent.getStringExtra("EmpName");
        PayId = intent.getIntExtra("PayId", 0);
        Date = intent.getStringExtra("Date");
        TotalAmountPaid = intent.getDoubleExtra("TotalAmountPaid", 0.0);
        ChecquePaid = intent.getDoubleExtra("ChecquePaid", 0.0);
        CashPaid = intent.getDoubleExtra("CashPaid", 0.0);
        Addition = intent.getDoubleExtra("Addition", 0.0);
        Deduction = intent.getDoubleExtra("Deduction", 0.0);
        date_tv = findViewById(R.id.textView23);
        name_tv = findViewById(R.id.textView20);
        cash_e = findViewById(R.id.editText19);
        cheque_e = findViewById(R.id.editText16);
        addition_e = findViewById(R.id.editText17);
        deduction_e = findViewById(R.id.editText15);
        total_e = findViewById(R.id.editText18);
        save_b = findViewById(R.id.button18);
        date_tv.setText(Date);
        name_tv.setText(EmpName);
        cash_e.setText(CashPaid + "");
        cheque_e.setText(ChecquePaid + "");
        addition_e.setText(Addition + "");
        deduction_e.setText(Deduction + "");
        total_e.setText(TotalAmountPaid + "");

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }
}
