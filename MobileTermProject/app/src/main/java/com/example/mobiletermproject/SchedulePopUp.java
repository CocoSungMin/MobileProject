package com.example.mobiletermproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;


public class SchedulePopUp extends Activity {
    Schedule schedule;
    TextView popUpSheetName;
    TextView popUpSheetTime;
    TextView popUpSheetContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_schedule_pop_up);


        Intent ReceivedData = getIntent();
        schedule = (Schedule) ReceivedData.getSerializableExtra("schedule");
        // intent로 넘어온 값 받아용~

        popUpSheetName = findViewById(R.id.scheduleName);
        popUpSheetTime = findViewById(R.id.scheduleTime);
        popUpSheetContent = findViewById(R.id.scheduleContent);

        popUpSheetName.setText(schedule.getTitle());
        popUpSheetTime.setText(schedule.getStartTime() + " ~ " + schedule.getEndTime());
        popUpSheetContent.setText(schedule.getContent());

        Button editBtn = (Button) findViewById(R.id.scheduleEdit);
        Button delBtn = (Button) findViewById(R.id.scheduleDel);

        //수정 버튼
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //삭제 버튼
        delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = null;
                FirebaseUser user1 = FirebaseAuth.getInstance().getCurrentUser();
                if (user1 != null) {
                    id = user1.getUid();
                }

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection(id).document(schedule.getID()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("dbtest", "DocumentSnapshot successfully deleted!");
                        Toast.makeText(SchedulePopUp.super.getApplicationContext(),"일정 삭제",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("dbtest", "Error deleting document", e);
                        Toast.makeText(SchedulePopUp.super.getApplicationContext(),"일정 삭제에 실패하였습니다.",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

}
