package com.example.mobiletermproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

@RequiresApi(api = Build.VERSION_CODES.O)
public class Calendar_test extends AppCompatActivity {
    Menu menu;
    MaterialCalendarView calenderView = null;
    BottomSheetBehavior bottomSheetBehavior;
    TextView botSheetDate;
    ArrayList<Schedule> schedules = new ArrayList<>();//디비에서 불러온 스케줄들 다 여기 있습니다.

    EventDecorator eventDecorator;

    //메뉴부분
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.appbar_menu, menu);
        this.menu = menu;
        menu.getItem(0).setChecked(true);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.search:
                Toast.makeText(getApplicationContext(), "이거 누르면 마이페이지로 화면 변환", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.option:
                Toast.makeText(getApplicationContext(), "이거 누르면 그룹관리(사용자 추가)로 화면 변환", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.group1:
                Toast.makeText(getApplicationContext(), "이거 누르면 그룹1로 화면 변환", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.group2:
                Toast.makeText(getApplicationContext(), "이거 누르면 그룹2로 화면 변환", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.group3:
                Toast.makeText(getApplicationContext(), "이거 누르면 그룹3로 화면 변환", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_test);

        //툴바 부분
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(" ");
        setSupportActionBar(toolbar);

        //액션바 부분인데 툴바 사용해가지고 일단 주석처리
        /*getSupportActionBar().setTitle("시간엄수");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFF339999));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/


        botSheetDate = findViewById(R.id.botsheetDate);

        //오늘 날짜 바텀시트 설정
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("M월 dd일");
        botSheetDate.setText(String.format("%s", format.format(Calendar.getInstance().getTime())));

        //캘린더 설정//////////////////////////////////////////////////////////////////////
        calenderView = findViewById(R.id.calendarView);
        // calender set up
        calenderView.setTopbarVisible(true);

        calenderView.state().edit()
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setMinimumDate(CalendarDay.from(2017, 0, 1))
                .setMaximumDate(CalendarDay.from(2030, 11, 31))
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();
        calenderView.setShowOtherDates(MaterialCalendarView.SHOW_OUT_OF_RANGE);
        calenderView.setDynamicHeightEnabled(true);
        calenderView.addDecorators(
                new SundayDecorator(),
                new SaturdayDecorator(),
                eventDecorator = new EventDecorator(this,schedules)
        );

        calenderView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                updateBotSheet(date);
            }
        });

        //////////////////////////////////////////////////////////////////////////////


//        Bottom Sheet 올릴 때 모션이 안좋아서 잠깐 빼뒀습니당
//        //BottomSheet
//        LinearLayout linearLayout = findViewById(R.id.schedule_bottom_sheet);
//        bottomSheetBehavior = BottomSheetBehavior.from(linearLayout);
//        //초기 높이 조절
//        bottomSheetBehavior.setPeekHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,230,getResources().getDisplayMetrics()));
//
//        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
//            @Override
//            public void onStateChanged(@NonNull View bottomSheet, int newState) {
//
//            }
//
//            @Override
//            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
//                //내려간 상태
//                if(slideOffset == bottomSheet.SCREEN_STATE_OFF){
//                    calenderView.state().edit().setCalendarDisplayMode(CalendarMode.MONTHS).commit();
//                }
//                //올라간 상태
//                else if(slideOffset == bottomSheet.SCREEN_STATE_ON){
//                    calenderView.state().edit().setCalendarDisplayMode(CalendarMode.WEEKS).commit();
//                }
//            }
//        });
        //////////////////////////////////////////////////////////////////////////////


        //스케쥴 추가 버튼

        Button btnAdd = findViewById(R.id.btnAddSchedule);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Calendar_test.this, CreateSchedule.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        updateSchedules();
        calenderView.removeDecorator(eventDecorator);
        eventDecorator = new EventDecorator(this, schedules);
        calenderView.addDecorators(eventDecorator);
    }

    // 바텀 시트 업데이트
    public void updateBotSheet(@NonNull CalendarDay date) {
        //날짜 변경
        botSheetDate.setText(String.format("%s월 %s일", String.valueOf(date.getMonth() + 1), String.valueOf(date.getDay())));

        LocalDate d = LocalDate.of(date.getYear(), date.getMonth() + 1, date.getDay());

        //내용 변경
        ListAdapter oAdapter = new ListAdapter(getSelectedSchedule(d));
        ListView list = findViewById(R.id.scheduleList);
        list.setAdapter(oAdapter);
    }

    public ArrayList<ItemData> getSelectedSchedule(LocalDate d) {
        ArrayList<ItemData> list = new ArrayList<>();

        for (Schedule sch : schedules) {
            if (sch.containsDate(d)) {
                ItemData item = new ItemData();
                item.Title = sch.getTitle();
                item.Time = sch.getStartTime() + " ~ " + sch.getEndTime();
                item.Content = sch.getContent();

                list.add(item);
            }
        }

        return list;
    }


    // 디비 값 변동 생길시 자동 업데이트 (근데 너무 자주함...)
    public void updateSchedules() {
        String id = null;
        FirebaseUser user1 = FirebaseAuth.getInstance().getCurrentUser();
        if (user1 != null) {
            id = user1.getUid();
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(id).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("MainActivity update", "Listen failed.", e);
                    return;
                }
                int count = queryDocumentSnapshots.size();
                schedules.clear();
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    if (doc != null) {
                        //Log.d("dbtest", doc.getId());
                        Schedule sch = doc.toObject(Schedule.class);
                        sch.setID(doc.getId());
                        schedules.add(sch);
                    }
                }
            }
        });
    }
}
