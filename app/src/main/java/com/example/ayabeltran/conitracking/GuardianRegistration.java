package com.example.ayabeltran.conitracking;

import android.content.Intent;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class GuardianRegistration extends AppCompatActivity {

    DBHelper mydb;

    EditText edtlastname, edtfirstname,
            edtmidname, edtage,
            edtbday, edtgender,
            edtcontactno, edtemail,
            edtuname, edtpass,
            confirmpword;


    Button btnsignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guardian_registration);


        mydb = new DBHelper(this);
//        mydb = ((DBApp)getApplication()).getDatabase();

        edtlastname =  findViewById(R.id.editlastname);
        edtfirstname = findViewById(R.id.editfirstname);
        edtmidname = findViewById(R.id.editmidname);
        edtage = findViewById(R.id.editage);
        edtbday = findViewById(R.id.editbday);
        edtgender = findViewById(R.id.editgender);
        edtcontactno = findViewById(R.id.editcontactnumber);
        edtemail = findViewById(R.id.editemail);
        edtuname = findViewById(R.id.editusername);
        edtpass = findViewById(R.id.editpassword);
        confirmpword = findViewById(R.id.editconfirmpass);
        btnsignup = findViewById(R.id.btn_signup);



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

    }

}
