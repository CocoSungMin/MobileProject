package com.example.mobiletermproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.navigation.NavigationView;
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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;

public class Calendar_main extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    NavigationView navigationView;

    Menu menu;
    MaterialCalendarView calenderView = null;
    BottomSheetBehavior bottomSheetBehavior;
    TextView botSheetDate;
    ArrayList<Schedule> schedules = new ArrayList<>();//디비에서 불러온 스케줄들 다 여기 있습니다.

    CalendarDay selectedDay;
    EventDecorator eventDecorator;

    //drawble 메뉴 사용하면서 주석처리
    /*@Override
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
    }*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_main);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            schedules = (ArrayList<Schedule>) bundle.getSerializable("schedules");
        }
        // 오늘 설정
        selectedDay = CalendarDay.today();

        //drawble 메뉴
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(" ");
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.navigationView);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);


        botSheetDate = findViewById(R.id.botsheetDate);


        /*                 캘린더 설정                */
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
                eventDecorator = new EventDecorator(this, schedules)
        );

        calenderView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                updateBotSheet(date);
                selectedDay = date;
            }
        });


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
                Intent intent = new Intent(Calendar_main.this, CreateSchedule.class);
                startActivity(intent);
            }
        });

        updateSchedules();

    }


    @Override
    protected void onResume() {
        super.onResume();

        calenderView.removeDecorator(eventDecorator);
        eventDecorator = new EventDecorator(this, schedules);
        calenderView.addDecorators(eventDecorator);

        updateBotSheet(selectedDay);
        calenderView.setDateSelected(selectedDay, true);
    }


    //뒤로 가기 버튼 누르면 메뉴 들어감
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    // 바텀 시트 업데이트
    public void updateBotSheet(@NonNull CalendarDay date) {
        //날짜 변경
        botSheetDate.setText(String.format("%s월 %s일", String.valueOf(date.getMonth() + 1), String.valueOf(date.getDay())));

        LocalDate d = LocalDate.of(date.getYear(), date.getMonth() + 1, date.getDay());

        final ArrayList<ItemData> selectedSchedule = getSelectedSchedule(d);
        //내용 변경
        ListAdapter oAdapter = new ListAdapter(selectedSchedule);
        ListView list = findViewById(R.id.scheduleList);
        list.setAdapter(oAdapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showPopUp(schedules.get(schedules.indexOf(new Schedule(selectedSchedule.get(position).ID))));
            }
        });
    }

    public ArrayList<ItemData> getSelectedSchedule(LocalDate d) {
        ArrayList<ItemData> list = new ArrayList<>();

        for (Schedule sch : schedules) {
            if (sch.containsDate(d)) {
                ItemData item = new ItemData();
                item.ID = sch.getID();
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
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.d("Update Schedules", "Listen failed.", e);
                    return;
                }
                if (queryDocumentSnapshots != null) {
                    schedules.clear();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        Schedule sch = doc.toObject(Schedule.class);
                        sch.setID(doc.getId());
                        schedules.add(sch);
                        //Log.d("dbtest", doc.getId() + "    Main");
                    }

                }
            }
        });
    }

    public void showPopUp(Schedule schedule) {
        Intent popUp = new Intent(this, SchedulePopUp.class);

        popUp.putExtra("schedule", schedule);
        startActivity(popUp);
    }

    //drawble 메뉴 리스너
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.group1) {
            Toast.makeText(getApplicationContext(), "그룹1 화면으로 전환", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.group2) {
            Toast.makeText(getApplicationContext(), "그룹2 화면으로 전환", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.add_group) {
            Intent intent = new Intent(Calendar_main.this, JoinGroup.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
