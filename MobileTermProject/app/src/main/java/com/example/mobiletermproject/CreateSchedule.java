package com.example.mobiletermproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.prolificinteractive.materialcalendarview.CalendarDay;

public class CreateSchedule extends AppCompatActivity {
    private EditText schduleTitle;
    private EditText schduleContent;

    private CalendarDay startDate;
    private CalendarDay endDate;

    private ArrayAdapter adapter;
    private Spinner Yearstr;
    private Spinner Monstr;
    private Spinner Daystr;
    private Spinner YearEnd;
    private Spinner MonEnd;
    private Spinner DayEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_schedule);

        schduleTitle = findViewById(R.id.schduleTitle);
        schduleContent = findViewById(R.id.schduleContent);

        Yearstr = findViewById(R.id.YearSpinner);
        adapter = ArrayAdapter.createFromResource(this,R.array.Year,android.R.layout.simple_spinner_dropdown_item);
        Yearstr.setAdapter(adapter);

        Monstr = findViewById(R.id.MonthSpinner);
        adapter = ArrayAdapter.createFromResource(this,R.array.Month,android.R.layout.simple_spinner_dropdown_item);
        Monstr.setAdapter(adapter);

        Daystr = findViewById(R.id.DaySpinner);

        YearEnd = findViewById(R.id.endYear);
        adapter = ArrayAdapter.createFromResource(this,R.array.Year,android.R.layout.simple_spinner_dropdown_item);
        YearEnd.setAdapter(adapter);

        MonEnd = findViewById(R.id.endMonth);
        adapter = ArrayAdapter.createFromResource(this,R.array.Month,android.R.layout.simple_spinner_dropdown_item);
        MonEnd.setAdapter(adapter);




    }

    public void onClickHandler(View view){
        //DatePickerDialog dialog = new DatePickerDialog();
    }
}
