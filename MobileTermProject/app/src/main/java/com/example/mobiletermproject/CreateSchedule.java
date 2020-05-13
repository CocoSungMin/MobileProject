package com.example.mobiletermproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.prolificinteractive.materialcalendarview.CalendarDay;

public class CreateSchedule extends AppCompatActivity {
    private EditText schduleTitle;
    private EditText schduleContent;

    private CalendarDay startDate;
    private CalendarDay endDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_schedule);

        schduleTitle = findViewById(R.id.schduleTitle);
        schduleContent = findViewById(R.id.schduleContent);

        Button btnStartDate = findViewById(R.id.startDate);

        Button btnEndDate = findViewById(R.id.endDate);


    }

    public void onClickHandler(View view){
        //DatePickerDialog dialog = new DatePickerDialog();
    }
}
