package com.example.mobiletermproject;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ManageGroup extends AppCompatActivity {

    private Button copyBtn;
    private Button recommendBtn;
    private Button changeNameBtn;
    private Button changeMasterBtn;
    private Button withdrawalBtn;
    private TextView masterName;
    private TextView member;
    private TextView groupName;
    private EditText groupID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_group);


        changeMasterBtn = (Button) findViewById(R.id.changeMaster);
        recommendBtn = (Button) findViewById(R.id.recommendSch);
        copyBtn = (Button) findViewById(R.id.copyBtn);
        changeNameBtn = (Button) findViewById(R.id.changeName);
        withdrawalBtn = (Button) findViewById(R.id.withdrawal);
        masterName = (TextView) findViewById(R.id.groupMaster);
        groupName = (TextView) findViewById(R.id.groupName);
        member = (TextView) findViewById(R.id.groupMember);
        groupID = (EditText) findViewById(R.id.GroupID);


        // 그룹 아이디 불러와서 set 해주세요
        groupID.setText("그룹 아이디 들어갈 곳");

        // 그룹장 불러와서 set 해주세요
        masterName.setText("모바일");

        // 그룹 구성원 불러와서 set 해주세요 (계정 아이디까지 가능?)
        member.setText("오성원(lee@com)\n이성민(lee@com)\n이수빈(lee@com)\n이재윤(lee@com)\n");


        copyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("Code", groupID.getText().toString());
                clipboardManager.setPrimaryClip(clipData);

                Toast.makeText(getApplicationContext(), "그룹 코드가 복사되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        recommendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "그룹 일정 시간 추천 기능", Toast.LENGTH_SHORT).show();
            }
        });

        withdrawalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManageGroup.this, Withdrawal.class);
                startActivity(intent);
            }
        });

        changeNameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManageGroup.this, ChangeGroupName.class);
                startActivity(intent);
            }
        });

        changeMasterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "관리자 변경 기능", Toast.LENGTH_SHORT).show();
            }
        });

    }

}
