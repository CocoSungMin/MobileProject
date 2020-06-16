package com.example.mobiletermproject;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

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

    String gId;
    String gName;
    String managerId;
    ArrayList<String> memberId;
    ArrayList<String> memberName;
    String membersetting = "";

    int request = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_group);

        Intent intent = getIntent();
        final Bundle bundle = intent.getExtras();
        if (bundle != null) {
            gId = bundle.getString("GroupId");
            gName = bundle.getString("GroupName");
        }

        changeMasterBtn = (Button) findViewById(R.id.changeMaster);
        recommendBtn = (Button) findViewById(R.id.recommendSch);
        copyBtn = (Button) findViewById(R.id.copyBtn);
        changeNameBtn = (Button) findViewById(R.id.changeName);
        withdrawalBtn = (Button) findViewById(R.id.withdrawal);
        masterName = (TextView) findViewById(R.id.groupMaster);
        groupName = (TextView) findViewById(R.id.groupName);
        member = (TextView) findViewById(R.id.groupMember);
        groupID = (EditText) findViewById(R.id.GroupID);


        groupName.setText(gName);

        // 그룹 아이디 불러와서 set 해주세요
        groupID.setText(gId);

        // 그룹장 불러와서 set 해주세요
        masterName.setText("모바일");


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
                Bundle bundle1 = new Bundle();
                bundle1.putString("GN", gName);
                bundle1.putString("GID", gId);
                bundle1.putString("M", managerId);
                intent.putExtras(bundle1);
                startActivityForResult(intent, request);
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
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                if (!user.getUid().equals(managerId)) {
                    Toast.makeText(getApplicationContext(), "관리자만 이용 가능합니다.", Toast.LENGTH_SHORT).show();
                } else {

                }
            }
        });


        groupDBManage();
    }

    // 탈퇴시 그룹페이지 종료
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == request && resultCode == 1) {
            finish();
        }
    }

    //그룹정보 가져오기
    public void groupDBManage() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("Group").document(gId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    managerId = (String) task.getResult().get("Manager");
                    memberId = (ArrayList<String>) task.getResult().get("Member");
                    memberName = (ArrayList<String>) task.getResult().get("MemberName");

                    masterName.setText(memberName.get(memberId.indexOf(managerId)));

                    for (int i = 0; i < memberName.size(); i++) {
                        membersetting += memberName.get(i);
                        membersetting += "\n";
                    }
                    member.setText(membersetting);

                } else {
                    Log.e("ManageGroup", "Get document error.", task.getException());
                }
            }
        });
    }


}
