package com.example.mobiletermproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        Log.d(this.getClass().getName(), "Mainmenu OnCreate 실행");

        // 캘린더 테스트 버튼 용
        Button calbtn = findViewById(R.id.calbtn);
        calbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(this.getClass().getName(), "Mainmenu Button 실행");
                Intent intent = new Intent(getApplicationContext(), Calendar_test.class);
                startActivity(intent);
            }
        });
    }
}
