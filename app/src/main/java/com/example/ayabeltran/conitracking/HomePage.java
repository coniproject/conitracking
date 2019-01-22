package com.example.ayabeltran.conitracking;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomePage extends AppCompatActivity {

    Button btnlogin, btnsignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        btnlogin = findViewById(R.id.btn_login);
        btnsignup = findViewById(R.id.btn_signup);

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent toUserLogin = new Intent(HomePage.this, UserLogin.class);
                startActivity(toUserLogin);
            }
        });

        btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent toRegistration = new Intent(HomePage.this, GuardianRegistration.class);
                startActivity(toRegistration);

            }
        });
    }
}
