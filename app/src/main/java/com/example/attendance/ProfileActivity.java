package com.example.attendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.solver.widgets.ConstraintAnchor;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.AlphabeticIndex;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.Attributes;

import androidx.core.app.ActivityCompat;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {
    private DatePickerDialog picker;
    private ProgressDialog progressDialog;
    private int EmployeeId, UserId;
    private String EmployeeName, EmployeeImage, FirstName, ImageString, LastName, Address, PhoneNo, AccountNo, Cnic, Dob, CnicExp, LastEditTime;
    private EditText FirstName_e, LastName_e, Address_e, PhoneNo_e, AccountNo_e, Cnic_e, Dob_e, CnicExp_e;
    private CircleImageView Dp;
    private Button update_b;
    private ImageView EditDp;
    private final int request_codeGallery = 999;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initialization();
        getProfileInfo();

        update_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile();
            }
        });

        FirstName_e.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                FirstName_e.setTextIsSelectable(true);
                FirstName_e.setInputType(InputType.TYPE_CLASS_TEXT);
                update_b.setVisibility(View.VISIBLE);
                return true;
            }
        });
        LastName_e.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                LastName_e.setTextIsSelectable(true);
                LastName_e.setInputType(InputType.TYPE_CLASS_TEXT);
                update_b.setVisibility(View.VISIBLE);
                return true;
            }
        });
        Address_e.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Address_e.setTextIsSelectable(true);
                Address_e.setInputType(InputType.TYPE_CLASS_TEXT);
                update_b.setVisibility(View.VISIBLE);
                return true;
            }
        });
        PhoneNo_e.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                PhoneNo_e.setTextIsSelectable(true);
                PhoneNo_e.setInputType(InputType.TYPE_CLASS_NUMBER);
                update_b.setVisibility(View.VISIBLE);
                return true;
            }
        });
        AccountNo_e.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AccountNo_e.setTextIsSelectable(true);
                AccountNo_e.setInputType(InputType.TYPE_CLASS_NUMBER);
                update_b.setVisibility(View.VISIBLE);
                return true;
            }
        });
        Cnic_e.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Cnic_e.setTextIsSelectable(true);
                Cnic_e.setInputType(InputType.TYPE_CLASS_NUMBER);
                update_b.setVisibility(View.VISIBLE);
                return true;
            }
        });
        Dob_e.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Dob_e.setTextIsSelectable(true);
                update_b.setVisibility(View.VISIBLE);
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                final int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(ProfileActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                Dob = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                                Dob_e.setText(Dob);
                                Toast.makeText(ProfileActivity.this, Dob, Toast.LENGTH_SHORT).show();
                            }
                        }, year, month, day);
                picker.show();
                return true;
            }
        });
        CnicExp_e.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                CnicExp_e.setTextIsSelectable(true);
                update_b.setVisibility(View.VISIBLE);
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                final int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(ProfileActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                CnicExp = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                                CnicExp_e.setText(CnicExp);
                                Toast.makeText(ProfileActivity.this, CnicExp, Toast.LENGTH_SHORT).show();
                            }
                        }, year, month, day);
                picker.show();
                return true;
            }
        });


        EditDp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditProfilePic();
            }
        });

    }

    private void EditProfilePic() {
        ActivityCompat.requestPermissions(ProfileActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, request_codeGallery);
    }

    // Scaling
    private static Bitmap scaleDownBitmap(Bitmap photo, Context context) {
        final float densityMultiplier = context.getResources().getDisplayMetrics().density;
        int h = (int) (150 * densityMultiplier);
        int w = (int) (h * photo.getWidth() / ((double) photo.getHeight()));
        photo = Bitmap.createScaledBitmap(photo, w, h, true);
        return photo;
    }

    // Upload Image # 1
    private String imageViewToByte(Bitmap imageBitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    // Upload Image # 2
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == request_codeGallery) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, request_codeGallery);
            } else {
                Toast.makeText(ProfileActivity.this, "You don't have permission to excess file location", Toast.LENGTH_LONG).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    // Upload Image # 3
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == request_codeGallery && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            assert uri != null;
            InputStream inputStream = null;
            try {
                inputStream = getContentResolver().openInputStream(uri);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            bitmap = BitmapFactory.decodeStream(inputStream);
            if (bitmap != null) {
                Bitmap bitmap_Scaled = scaleDownBitmap(bitmap, ProfileActivity.this);
                ImageString = imageViewToByte(bitmap_Scaled);
                RequestOptions transcodeTypeRequestBuilder = new RequestOptions().error(R.drawable.avatar12);
                Glide.with(ProfileActivity.this).load(Base64.decode(ImageString, Base64.DEFAULT)).apply(transcodeTypeRequestBuilder).into(Dp);
                update_b.setVisibility(View.VISIBLE);
            }

            return;
        } else {
            Toast.makeText(ProfileActivity.this, "Couldn't Upload Image!", Toast.LENGTH_LONG).show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void updateProfile() {
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        LastEditTime = df.format(currentTime);
        FirstName = FirstName_e.getText().toString();
        LastName = LastName_e.getText().toString();
        Address = Address_e.getText().toString();
        PhoneNo = PhoneNo_e.getText().toString();
        AccountNo = AccountNo_e.getText().toString();
        Cnic = Cnic_e.getText().toString();
        Dob = Dob_e.getText().toString();
        CnicExp = CnicExp_e.getText().toString();

        if (TextUtils.isEmpty(FirstName) || TextUtils.isEmpty(LastName) || TextUtils.isEmpty(Address) || TextUtils.isEmpty(PhoneNo) ||
                TextUtils.isEmpty(AccountNo) || TextUtils.isEmpty(Cnic) || TextUtils.isEmpty(Dob) || TextUtils.isEmpty(CnicExp)) {
            Toast.makeText(this, "Please Fill All Fields Correctly", Toast.LENGTH_LONG).show();
        } else {
            progressDialog.show();
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://minhasdemo1.000webhostapp.com/Attendance_test/updateProfileInfo.php", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    progressDialog.dismiss();
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    Toast.makeText(ProfileActivity.this, response, Toast.LENGTH_SHORT).show();
                    EmployeeImage = ImageString;
                    Intent intent = new Intent(ProfileActivity.this, HomeActivity.class);
                    intent.putExtra("EmpId", EmployeeId);
                    intent.putExtra("EmpName", EmployeeName);
                    intent.putExtra("UserId", UserId);
                    intent.putExtra("EmployeeImage", EmployeeImage);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
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
                    hashMap.put("FirstName", FirstName);
                    hashMap.put("LastName", LastName);
                    hashMap.put("Address", Address);
                    hashMap.put("AccountNo", AccountNo);
                    hashMap.put("PhoneNo", PhoneNo);
                    hashMap.put("NICNo", Cnic);
                    hashMap.put("DOB", Dob);
                    hashMap.put("CNICExp", CnicExp);
                    hashMap.put("EImage", ImageString);
                    hashMap.put("LastEditTime", LastEditTime);
                    return hashMap;
                }
            };

            stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            RequestQueue requestQueue = Volley.newRequestQueue(ProfileActivity.this);
            requestQueue.add(stringRequest);
        }

    }

    private void getProfileInfo() {
        progressDialog.show();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://minhasdemo1.000webhostapp.com/Attendance_test/getProfileInfo.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    progressDialog.dismiss();
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    JSONArray jsonArry = new JSONArray(response);
                    for (int i = 0; i < jsonArry.length(); i++) {
                        JSONObject jsonObject = jsonArry.getJSONObject(i);
                        FirstName = jsonObject.getString("FirstName");
                        LastName = jsonObject.getString("LastName");
                        Address = jsonObject.getString("Address1");
                        PhoneNo = jsonObject.getString("MobileNo");
                        AccountNo = jsonObject.getString("AccountNumber");
                        Cnic = jsonObject.getString("NICNo");
                        Dob = jsonObject.getString("DOB");
                        CnicExp = jsonObject.getString("CNICExpDate");
                    }
                    FirstName_e.setText(FirstName);
                    LastName_e.setText(LastName);
                    Address_e.setText(Address);
                    PhoneNo_e.setText(PhoneNo);
                    AccountNo_e.setText(AccountNo);
                    Cnic_e.setText(Cnic);
                    Dob_e.setText(Dob);
                    CnicExp_e.setText(CnicExp);

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
                hashMap.put("EmpId", EmployeeId + "");
                return hashMap;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(ProfileActivity.this);
        requestQueue.add(stringRequest);
    }

    private void initialization() {
        Intent intent = getIntent();
        EmployeeId = intent.getIntExtra("EmpId", 0);
        EmployeeName = intent.getStringExtra("EmpName");
        UserId = intent.getIntExtra("UserId", 0);
        EmployeeImage = intent.getStringExtra("EmployeeImage");
        progressDialog = new ProgressDialog(ProfileActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        FirstName_e = findViewById(R.id.editText4);
        LastName_e = findViewById(R.id.editText5);
        Address_e = findViewById(R.id.editText9);
        PhoneNo_e = findViewById(R.id.editText6);
        AccountNo_e = findViewById(R.id.editText8);
        Cnic_e = findViewById(R.id.editText7);
        Dob_e = findViewById(R.id.editText10);
        CnicExp_e = findViewById(R.id.editText11);
        update_b = findViewById(R.id.button15);
        Dp = findViewById(R.id.Profile_pic);
        EditDp = findViewById(R.id.imageButton_EditDp);
        RequestOptions transcodeTypeRequestBuilder = new RequestOptions().error(R.drawable.avatar12);
        Glide.with(ProfileActivity.this).load(Base64.decode(EmployeeImage, Base64.DEFAULT)).apply(transcodeTypeRequestBuilder).into(Dp);

        FirstName_e.setInputType(InputType.TYPE_NULL);
        LastName_e.setInputType(InputType.TYPE_NULL);
        Address_e.setInputType(InputType.TYPE_NULL);
        PhoneNo_e.setInputType(InputType.TYPE_NULL);
        AccountNo_e.setInputType(InputType.TYPE_NULL);
        Cnic_e.setInputType(InputType.TYPE_NULL);
        Dob_e.setInputType(InputType.TYPE_NULL);
        CnicExp_e.setInputType(InputType.TYPE_NULL);

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
        AlertDialog.Builder b = new AlertDialog.Builder(ProfileActivity.this);
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
        Intent intent = new Intent(ProfileActivity.this, HomeActivity.class);
        intent.putExtra("EmpId", EmployeeId);
        intent.putExtra("EmpName", EmployeeName);
        intent.putExtra("UserId", UserId);
        intent.putExtra("EmployeeImage", EmployeeImage);
        startActivity(intent);
        finish();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }
}
