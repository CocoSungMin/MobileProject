package com.example.mobiletermproject;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class JoinGroup extends AppCompatActivity {
    EditText groupID;
    Button createBtn;
    Button joinBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_group);

        groupID = (EditText) findViewById(R.id.joinGroupID);
        createBtn = (Button) findViewById(R.id.createBtn);
        joinBtn = (Button) findViewById(R.id.joinBtn);
        joinBtn.setEnabled(false);

        groupID.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override//20글자 넘어가면 JOIN 버튼 활성화
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = s.toString();
                if (text.length() >= 20)
                    joinBtn.setEnabled(true);
                else
                    joinBtn.setEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("dbtest", "create");
            }
        });

        joinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                joinGroup(groupID.getText().toString());
            }
        });
    }

    /*그룹 조인 메소드*/
    private void joinGroup(final String groupID) {
        final Map<String, Object> docData = new HashMap<String, Object>();

        String id = null;
        FirebaseUser user1 = FirebaseAuth.getInstance().getCurrentUser();
        if (user1 != null) {
            String uid = user1.getUid();
            id = uid;
        }

        if (id != null) {
            final FirebaseFirestore db = FirebaseFirestore.getInstance();
            final String finalId = id;
            db.collection("Group").document(groupID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            // 문서 받아와서 사용자 추가
                            docData.clear();
                            docData.put("GroupName", document.getData().get("GroupName"));

                            ArrayList<String> memList = (ArrayList<String>) document.getData().get("Member");

                            if (memList.contains(finalId)) {
                                Log.d("JoinGroup", "Already joined this group.");
                                Toast.makeText(getApplicationContext(), "이미 가입된 그룹입니다.",Toast.LENGTH_SHORT).show();
                            } else {
                                memList.add(finalId);
                                docData.put("Member", memList);
                                FirebaseFirestore.getInstance().collection("Group").document(groupID).set(docData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d("JoinGroup", "DocumentSnapshot successfully written");
                                        Toast.makeText(getApplicationContext(), "그룹 가입 완료!",Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d("JoinGroup", "Error writing document", e);
                                        Toast.makeText(getApplicationContext(), "DB 오류",Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "존재하지 않는 그룹입니다.",Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.d("JoinGroup", "join Group failed.", task.getException());
                        Toast.makeText(getApplicationContext(), "DB 오류",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
