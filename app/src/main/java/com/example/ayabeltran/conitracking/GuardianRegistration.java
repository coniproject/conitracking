package com.example.ayabeltran.conitracking;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class GuardianRegistration extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    DBHelper mydb;

    EditText edtlastname, edtfirstname,
            edtmidname, edtage,
            edtbday, edtgender,
            edtcontactno, edtemail,
            edtuname, edtpass,
            confirmpword;


    Button btnsignup;
    RadioButton rdbmale, rdbfemale;
    RadioGroup rdbgender;
    TextView txtbday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guardian_registration);
        edtlastname.requestFocus();

        mydb = new DBHelper(this);
//        mydb = ((DBApp)getApplication()).getDatabase();

        edtlastname =  findViewById(R.id.editlastname);
        edtfirstname = findViewById(R.id.editfirstname);
        edtmidname = findViewById(R.id.editmidname);
        edtage = findViewById(R.id.editage);
//        edtbday = findViewById(R.id.editbday);
//        edtgender = findViewById(R.id.editgender);
        edtcontactno = findViewById(R.id.editcontactnumber);
        edtemail = findViewById(R.id.editemail);
        edtuname = findViewById(R.id.editusername);
        edtpass = findViewById(R.id.editpassword);
        confirmpword = findViewById(R.id.editconfirmpass);
        btnsignup = findViewById(R.id.btn_signup);
        rdbmale= findViewById(R.id.rdmale);
        rdbfemale=findViewById(R.id.rdmale);
        txtbday=findViewById(R.id.txtbday);




        btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean isInserted = mydb.addguardian(
                        edtlastname.getText().toString(),
                        edtfirstname.getText().toString(),
                        edtmidname.getText().toString(),
                        edtage.getText().toString(),
                        edtbday.getText().toString(),
                        edtgender.getText().toString(),
                        edtcontactno.getText().toString(),
                        edtemail.getText().toString(),
                        edtuname.getText().toString(),
                        edtpass.getText().toString());



                    if (isInserted) {
                        Toast.makeText(GuardianRegistration.this, "Registered", Toast.LENGTH_SHORT).show();
                        Intent toLogin = new Intent(GuardianRegistration.this, UserLogin.class);
                        startActivity(toLogin);
                    }
                    else {
                        Toast.makeText(GuardianRegistration.this, "Please fill up all the required fields.", Toast.LENGTH_SHORT).show();
                    }
                }
        });


        txtbday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        GuardianRegistration.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "onDateSet: mm/dd/yyy: " + month + "/" + day + "/" + year);

                String date = month + "/" + day + "/" + year;
                txtbday.setText(date);
            }
        };
    }

}
