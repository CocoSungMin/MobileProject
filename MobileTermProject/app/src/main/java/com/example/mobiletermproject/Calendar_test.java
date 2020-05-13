package com.example.mobiletermproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Calendar_test extends AppCompatActivity {
    //메뉴부분
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.appbar_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.search :
                Toast.makeText(getApplicationContext(), "Search Click", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.option :
                Toast.makeText(getApplicationContext(), "Option Click", Toast.LENGTH_SHORT).show();
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
        toolbar.setTitle(R.string.myAppName);
        setSupportActionBar(toolbar);



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
                new SaturdayDecorator()
        );

        //----------- Date selected events ----------
        calenderView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                //바텀시트 날짜 변경
                botSheetDate.setText(String.format("%s월 %s일", String.valueOf(date.getMonth()+1), String.valueOf(date.getDay())));
                //바텀시트 내용 업데이트 추후 추가
            }
        });
        //////////////////////////////////////////////////////////////////////////////

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
        });
        //////////////////////////////////////////////////////////////////////////////

        Button button = findViewById(R.id.btn_show_schedulr_botton_sheet);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });

        Button btnAdd = findViewById(R.id.asdTest2);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Calendar_test.this, CreateSchedule.class);
                startActivity(intent);
            }
        });

    }
}
