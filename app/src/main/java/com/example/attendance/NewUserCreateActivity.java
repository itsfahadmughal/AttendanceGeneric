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
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.InputType;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.Base64;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.Attributes;

import androidx.core.app.ActivityCompat;
import de.hdodenhof.circleimageview.CircleImageView;

public class NewUserCreateActivity extends AppCompatActivity {
    private DatePickerDialog picker;
    private ProgressDialog progressDialog;
    private int EmployeeId, UserId;
    private String EmployeeName, FirstName, LastName, Address, PhoneNo, AccountNo,
            Cnic, Dob, CnicExp, fatherName,EmployeeImage,
            email_addr, imageString;
    private EditText FirstName_e, LastName_e, Address_e, PhoneNo_e, AccountNo_e, Cnic_e,
            FatherName_e, Email_e;
    private ImageView Dp;
    private Button addUser_b;
    private TextView CnicExp_e, Dob_e;
    private ImageButton uploadImage;
    private final int request_codeGallery = 999;
    private Bitmap bitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user_create);
        initialization();

        Dob_e.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                final int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(NewUserCreateActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                Dob = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                                Dob_e.setText(Dob);
                                Toast.makeText(NewUserCreateActivity.this, Dob, Toast.LENGTH_SHORT).show();
                            }
                        }, year, month, day);
                picker.show();
            }
        });

        CnicExp_e.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                final int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(NewUserCreateActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                CnicExp = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                                CnicExp_e.setText(CnicExp);
                                Toast.makeText(NewUserCreateActivity.this, Dob, Toast.LENGTH_SHORT).show();
                            }
                        }, year, month, day);
                picker.show();
            }
        });


        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadImageDp();
            }
        });

        addUser_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile();
            }
        });


    }

    private void UploadImageDp() {
        ActivityCompat.requestPermissions(NewUserCreateActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, request_codeGallery);
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
                Toast.makeText(NewUserCreateActivity.this, "You don't have permission to excess file location", Toast.LENGTH_LONG).show();
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
            Dp.setImageBitmap(bitmap);
            return;
        } else {
            Toast.makeText(NewUserCreateActivity.this, "Couldn't Upload Image!", Toast.LENGTH_LONG).show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void updateProfile() {
        FirstName = FirstName_e.getText().toString();
        LastName = LastName_e.getText().toString();
        Address = Address_e.getText().toString();
        PhoneNo = PhoneNo_e.getText().toString();
        AccountNo = AccountNo_e.getText().toString();
        Cnic = Cnic_e.getText().toString();
        Dob = Dob_e.getText().toString();
        fatherName = FatherName_e.getText().toString();
        email_addr = Email_e.getText().toString();
        if (bitmap != null) {
            Bitmap bitmap_Scaled = scaleDownBitmap(bitmap, NewUserCreateActivity.this);
            imageString = imageViewToByte(bitmap_Scaled);
        } else {
            imageString = "";
        }

        if (TextUtils.isEmpty(FirstName) || TextUtils.isEmpty(LastName) || TextUtils.isEmpty(fatherName) || TextUtils.isEmpty(PhoneNo) ||
                TextUtils.isEmpty(AccountNo) || TextUtils.isEmpty(Cnic)) {
            Toast.makeText(this, "Please Fill All Fields Correctly", Toast.LENGTH_LONG).show();
        } else {
            Intent intent = new Intent(NewUserCreateActivity.this, NewUserActivity2.class);
            intent.putExtra("EmpId", EmployeeId);
            intent.putExtra("EmpName", EmployeeName);
            intent.putExtra("UserId", UserId);
            intent.putExtra("FirstName", FirstName);
            intent.putExtra("LastName", LastName);
            intent.putExtra("FatherName", fatherName);
            intent.putExtra("PhoneNo", PhoneNo);
            intent.putExtra("EmailAdrress", email_addr);
            intent.putExtra("AccountNo", AccountNo);
            intent.putExtra("Cnic", Cnic);
            intent.putExtra("Dob", Dob);
            intent.putExtra("CnicExp", CnicExp);
            intent.putExtra("Address", Address);
            intent.putExtra("Image", imageString);
            startActivity(intent);
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);

        }//end of else check empty fields

    }


    private void initialization() {
        Intent intent = getIntent();
        EmployeeId = intent.getIntExtra("EmpId", 0);
        EmployeeName = intent.getStringExtra("EmpName");
        UserId = intent.getIntExtra("UserId", 0);
        EmployeeImage = intent.getStringExtra("EmployeeImage");
        progressDialog = new ProgressDialog(NewUserCreateActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        FirstName_e = findViewById(R.id.editText4_admin);
        LastName_e = findViewById(R.id.editText5_admin3);
        Address_e = findViewById(R.id.editText9_admin);
        PhoneNo_e = findViewById(R.id.editText6_admin);
        AccountNo_e = findViewById(R.id.editText8_admin);
        Cnic_e = findViewById(R.id.editText7_admin);
        Dob_e = findViewById(R.id.editText10_admin);
        CnicExp_e = findViewById(R.id.editText11_admin);
        addUser_b = findViewById(R.id.button15_admin);
        Dp = findViewById(R.id.Profile_pic_admin);
        FatherName_e = findViewById(R.id.editText5_admin4);
        Email_e = findViewById(R.id.editText6_admin2);
        uploadImage = findViewById(R.id.imageButton);
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
        AlertDialog.Builder b = new AlertDialog.Builder(NewUserCreateActivity.this);
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
        Intent intent = new Intent(NewUserCreateActivity.this, Home2Activity.class);
        intent.putExtra("EmpId", EmployeeId);
        intent.putExtra("EmpName", EmployeeName);
        intent.putExtra("UserId", UserId);
        intent.putExtra("EmployeeImage", EmployeeImage);
        startActivity(intent);
        finish();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }
}
