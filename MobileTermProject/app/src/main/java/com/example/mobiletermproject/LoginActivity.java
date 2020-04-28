package com.example.mobiletermproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button login = (Button)findViewById(R.id.loginBtn);

        login.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent gotoLoading = new Intent(getApplicationContext(),Loading.class);
                startActivity(gotoLoading);
            }
        });

        TextView registerButton = (TextView)findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(LoginActivity.this,RegisterActicity.class);
                startActivityForResult(registerIntent,1);
                finish();
            }
        });
    }
}
