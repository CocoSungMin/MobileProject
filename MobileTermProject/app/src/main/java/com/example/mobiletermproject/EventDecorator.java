package com.example.mobiletermproject;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class EventDecorator implements DayViewDecorator {
    private final Calendar calendar = Calendar.getInstance();
    private final Drawable drawble;
    ArrayList<String>result = new ArrayList<String>();
    Date date;
    int calScheduleNum[];

    public EventDecorator(Activity context){
        drawble = context.getResources().getDrawable(R.drawable.event_dot);
    }


    @Override
    public boolean shouldDecorate(CalendarDay day) {
        day.copyTo(calendar);
        //이벤트 있는 날짜 db에서 받아오기 // 지금은 확인을 위해 수요일로 때려밖음
        int eventday = calendar.get(calendar.DAY_OF_WEEK);
        ClassifySchedule();


        return eventday == Calendar.WEDNESDAY;
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.setBackgroundDrawable(drawble);
    }

    public void existSchedule() {
        String id = null;
        FirebaseUser user1 = FirebaseAuth.getInstance().getCurrentUser();
        if (user1 != null) {
            id = user1.getUid();
        }
        final Map<String, Object>[] schedule = new Map[]{new HashMap<>()};
        final ArrayList<String> temp = new ArrayList<String>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                temp.add(document.get("Start Date").toString());
                                // 일정 있는거 전부 찾아보리기
                            }
                        }
                        setResult(temp);
                    }
                });
    }
    public void setResult(ArrayList<String>temp){
        result.addAll(temp);
        HashSet<String> non_overlap = new HashSet<String>(result);
        result = new ArrayList<String>(non_overlap);
        //DB에서 중복된 날짜 값 제거하는 거임
        Log.d("Tag1","중복값 제거 : \n"+result.toString());

    }

    public void ClassifySchedule() {
        existSchedule();
    }
}
