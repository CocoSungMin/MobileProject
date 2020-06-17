package com.LEGENO.SaveTheTime;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Setting extends AppCompatActivity {

    String userName;
    String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);



        //개인 이름이랑 이메일 받아오려고 calendar main에 있는 코드 가져왔는데 데이터를 못가져오네요..
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            userName = bundle.getString("UN");
            userEmail = bundle.getString("UE");
            Log.d("userProfile",userName+userEmail);
        }

        TextView Nametxt = findViewById(R.id.userID);
        TextView Emailtxt = findViewById(R.id.email);
        Nametxt.setText(userName);
        Emailtxt.setText(userEmail);


        //로그아웃 버튼 이벤트
        Button logoutbtn=findViewById(R.id.logout);
        logoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Setting.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //회원탈퇴 버튼 이벤트
        Button withdrawal=findViewById(R.id.withdraw);
        withdrawal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Setting.this, ConfirmWithdrawal.class);
                startActivity(intent);
            }
        });

        //일정 전체삭제
        Button deleteSchedule=findViewById(R.id.deleteAll);
        deleteSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Setting.this, ConfirmDeleteAllSchedule.class);
                startActivity(intent);
            }
        });
    }



}

