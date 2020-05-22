package com.example.mobiletermproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Calendar_test extends AppCompatActivity {

    ArrayAdapter sch;
    ArrayList<String> temp= new ArrayList<String>();
    Menu menu;
    //메뉴부분
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.appbar_menu, menu);
        this.menu = menu;
        menu.getItem(0).setChecked(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        switch (id)
        {
            case R.id.search :
                Toast.makeText(getApplicationContext(), "이거 누르면 마이페이지로 화면 변환", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.option :
                Toast.makeText(getApplicationContext(), "이거 누르면 그룹관리(사용자 추가)로 화면 변환", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.group1 :
                Toast.makeText(getApplicationContext(), "이거 누르면 그룹1로 화면 변환", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.group2 :
                Toast.makeText(getApplicationContext(), "이거 누르면 그룹2로 화면 변환", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.group3 :
                Toast.makeText(getApplicationContext(), "이거 누르면 그룹3로 화면 변환", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private BottomSheetBehavior bottomSheetBehavior;
    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_test);

        //툴바 부분
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(" ");
        setSupportActionBar(toolbar);

        /*
        String id = null;
        FirebaseUser user1 = FirebaseAuth.getInstance().getCurrentUser();
        if (user1 != null) {
            String uid = user1.getUid();
            id = uid;
        }
        final Map<String, Object>[] schedule = new Map[]{new HashMap<>()};

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(DocumentSnapshot document : task.getResult()){
                                Log.d("TAG",document.getData().toString());
                            }
                        }
                    }
                });
         */

        //액션바 부분인데 툴바 사용해가지고 일단 주석처리
        /*getSupportActionBar().setTitle("시간엄수");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFF339999));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/


        final TextView botSheetDate = findViewById(R.id.botsheetDate);

        //오늘 날짜 바텀시트 설정
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("M월 dd일");
        botSheetDate.setText(String.format("%s",format.format(Calendar.getInstance().getTime())));

        //캘린더 설정//////////////////////////////////////////////////////////////////////
        final MaterialCalendarView calenderView = findViewById(R.id.calendarView);
        // calender set up
        calenderView.setTopbarVisible(true);

        calenderView.state().edit()
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setMinimumDate(CalendarDay.from(2017,0,1))
                .setMaximumDate(CalendarDay.from(2030,11,31))
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();
        calenderView.setShowOtherDates(MaterialCalendarView.SHOW_OUT_OF_RANGE);
        calenderView.setDynamicHeightEnabled(true);
        calenderView.addDecorators(
                new SundayDecorator(),
                new SaturdayDecorator(),
                new EventDecorator(this)
        );

        //----------- Date selected events ----------
        calenderView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                //바텀시트 날짜 변경
                botSheetDate.setText(String.format("%s월 %s일", String.valueOf(date.getMonth()+1), String.valueOf(date.getDay())));
                //바텀시트 내용 업데이트 추후 추가
                SearchScheduleDB(date); // bottom sheet 내용 추가 하는 method 아래에 있음
            }
        });
        //////////////////////////////////////////////////////////////////////////////
        /*Bottom Sheet 올릴 때 모션이 안좋아서 잠깐 빼뒀습니당
        //BottomSheet
        LinearLayout linearLayout = findViewById(R.id.schedule_bottom_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(linearLayout);
        //초기 높이 조절
        bottomSheetBehavior.setPeekHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,230,getResources().getDisplayMetrics()));

        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                //내려간 상태
                if(slideOffset == bottomSheet.SCREEN_STATE_OFF){
                    calenderView.state().edit().setCalendarDisplayMode(CalendarMode.MONTHS).commit();
                }
                //올라간 상태
                else if(slideOffset == bottomSheet.SCREEN_STATE_ON){
                    calenderView.state().edit().setCalendarDisplayMode(CalendarMode.WEEKS).commit();
                }
            }
        });*/
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



    public void SearchScheduleDB(CalendarDay date) {
        temp.clear();
        final String ScDate = String.valueOf(date.getYear()) + "." + String.valueOf(date.getMonth() + 1) + "." + String.valueOf(date.getDay());
        String id = null;
        FirebaseUser user1 = FirebaseAuth.getInstance().getCurrentUser();
        if (user1 != null) {
            id = user1.getUid();
        }
        final Map<String, Object>[] schedule = new Map[]{new HashMap<>()};

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(id).
                whereEqualTo("Start Date", ScDate)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                temp.add(document.get("Title").toString() + "\n"
                                        + document.get("StartTime") + " ~ "
                                        + document.get("EndTime").toString()
                                        + "\n" + document.get("Content").toString());
                            }
                        }
                        compSchedule();
                    }
                });
    }


    public void compSchedule(){
        Log.d("Tag1","Temp state : \n"+temp.toString());
        ArrayAdapter sch = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_list_item_1, temp);
        ListView list = findViewById(R.id.scheduleList);
        list.setAdapter(sch);
    }
}
