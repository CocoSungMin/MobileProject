package com.example.mobiletermproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
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
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Calendar_test extends AppCompatActivity {
    //메뉴부분
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int curId = item.getItemId();
        switch (curId) {
            case R.id.menu_info:
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.menu_group1:
                Toast.makeText(this, "그룹 페이지1", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_group2:
                Toast.makeText(this, "그룹 페이지2", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_set:
                Toast.makeText(this, "그룹 설정 페이지", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private BottomSheetBehavior bottomSheetBehavior;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_test);

        final TextView botSheetDate = findViewById(R.id.botsheetDate);

        //액션바 부분
        getSupportActionBar().setTitle("시간엄수");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFF339999));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //오늘 날짜 바텀시트 설정
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("M월 dd일");
        botSheetDate.setText(String.format("%s",format.format(Calendar.getInstance().getTime())));


        final MaterialCalendarView calenderView = findViewById(R.id.calendarView);
        // calender set up
        //calenderView.setOnDateChangedListener((OnDateSelectedListener) this);
        //calenderView.setOnMonthChangedListener((OnMonthChangedListener) this);
        calenderView.setTopbarVisible(true);

        calenderView.state().edit()
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setMinimumDate(CalendarDay.from(2017,0,1))
                .setMaximumDate(CalendarDay.from(2030,11,31))
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();

        calenderView.setShowOtherDates(MaterialCalendarView.SHOW_OUT_OF_RANGE);
        calenderView.setDynamicHeightEnabled(true);
        //calenderView.setWeekDayTextAppearance(R.style.asdasd);
        //calenderView.setDateTextAppearance(R.style.asdasd);


        //----------- Date selected events ----------
        calenderView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                //Toast.makeText(getApplicationContext(), " " + date, Toast.LENGTH_SHORT).show();
                //바텀시트 날짜 변경
                botSheetDate.setText(String.format("%s월 %s일", String.valueOf(date.getMonth()+1), String.valueOf(date.getDay())));
            }
        });

        LinearLayout linearLayout = findViewById(R.id.schedule_bottom_sheet);

        bottomSheetBehavior = BottomSheetBehavior.from(linearLayout);

        Button button = findViewById(R.id.btn_show_schedulr_botton_sheet);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });
    }
}
