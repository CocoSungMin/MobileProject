package com.LEGENO.SaveTheTime;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;

public class RecommendSchedule extends AppCompatActivity {
    ArrayList<String> memberId;

    ArrayList<Schedule> schedules;

    LocalDate selectDate;
    LocalTime cursor;
    ArrayList<LocalTime> cursorStack;

    int loaded = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_recommend_schedule);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            memberId = bundle.getStringArrayList("MI");
        }

        // 날짜 선택 되면 실행
        //getAllSchedules();

        // 빈 시간 찾는 알고리즘
        //legeno();
    }

    private void getAllSchedules() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        for (int i = 0; i < memberId.size(); i++) {
            db.collection(memberId.get(i)).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        QuerySnapshot document = task.getResult();
                        schedules.clear();
                        for (QueryDocumentSnapshot doc : document) {
                            Schedule sch = doc.toObject(Schedule.class);
                            if (sch.startTimeByClass().toLocalDate().isEqual(selectDate) &&
                                    sch.endTimeByClass().toLocalDate().isEqual(selectDate)) {
                                schedules.add(sch);
                            }
                        }
                        loaded++;
                    } else {

                    }
                }
            });
        }
    }

    private void legeno() {
        cursorStack.clear();
        cursor = LocalTime.of(0, 0);
        schedules.sort(new Asending());

        for (int i = 0; i < schedules.size(); i++) {
            if (cursor.isBefore(schedules.get(i).startTimeByClass().toLocalTime())) {
                cursorStack.add(cursor);
                cursorStack.add(schedules.get(i).startTimeByClass().toLocalTime());
                cursor = schedules.get(i).endTimeByClass().toLocalTime();
            } else {
                if (cursor.isBefore(schedules.get(i).endTimeByClass().toLocalTime())){
                    cursor = schedules.get(i).endTimeByClass().toLocalTime();
                }
            }
        }
    }

    class Asending implements Comparator<Schedule> {
        public int compare(Schedule a, Schedule b) {
            if (a.startTimeByClass().isEqual(b.startTimeByClass()))
                return 0;
            else if (a.startTimeByClass().isBefore(b.startTimeByClass()))
                return 1;
            else
                return -1;
        }
    }
}

