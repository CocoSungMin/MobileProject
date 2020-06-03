package com.example.mobiletermproject;




import android.app.Activity;
import android.content.Intent;

import android.os.Bundle;

import android.view.Window;

import android.widget.ListView;
import android.widget.TextView;


import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;


public class SchedulePopUp extends Activity {
    ArrayList<Schedule> schedules = new ArrayList<>();//디비에서 불러온 스케줄들 다 여기 있습니다. <- 그래서 가져왔어요
    TextView popUpSheetDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_schedule_pop_up);


        Intent ReceivedData = getIntent();
        String d = ReceivedData.getStringExtra("Date");
        schedules = (ArrayList<Schedule>) ReceivedData.getSerializableExtra("dbset");
        // intent로 넘어온 값 받아용~
        LocalDate date = LocalDate.parse(d, DateTimeFormatter.ISO_DATE);

        String[] textday = date.toString().split("-");

        //LocalDate로 보내면 복잡해서 String으로 받고 박는걸루 했으용
        popUpSheetDate = findViewById(R.id.popUpsheetDate);
        popUpSheetDate.setText(textday[1]+"월"+textday[2]+"일");

        ListAdapter oAdapter = new ListAdapter(getSelectedSchedule(date));
        ListView list = findViewById(R.id.scheduleListInPopup);
        list.setAdapter(oAdapter);

    }

    // 받은 DB list로 search해요!
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




}
