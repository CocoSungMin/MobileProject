package com.example.mobiletermproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
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
    private Spinner Hourstr;
    private Spinner Mitstr;
    private Spinner stramfm;
    private Spinner HourEnd;
    private Spinner MitEnd;
    private Spinner endamfm;
    private Button getDatestr;
    private Button getDataend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_schedule);

        schduleTitle = findViewById(R.id.schduleTitle);
        schduleContent = findViewById(R.id.schduleContent);

        getDataend = findViewById(R.id.endDayButton);
        getDatestr = findViewById(R.id.DayButton);

        stramfm = findViewById(R.id.stramfm);
        endamfm = findViewById(R.id.endamfm);
        adapter=ArrayAdapter.createFromResource(this,R.array.amfm,android.R.layout.simple_spinner_dropdown_item);
        stramfm.setAdapter(adapter);
        endamfm.setAdapter(adapter);

        Hourstr = findViewById(R.id.Hour);
        adapter = ArrayAdapter.createFromResource(this,R.array.Hour,android.R.layout.simple_spinner_dropdown_item);
        Hourstr.setAdapter(adapter);

        Mitstr = findViewById(R.id.Minute);
        adapter = ArrayAdapter.createFromResource(this,R.array.Minute,android.R.layout.simple_spinner_dropdown_item);
        Mitstr.setAdapter(adapter);


        HourEnd = findViewById(R.id.endHour);
        adapter = ArrayAdapter.createFromResource(this,R.array.Hour,android.R.layout.simple_spinner_dropdown_item);
        HourEnd.setAdapter(adapter);

        MitEnd = findViewById(R.id.endMinute);
        adapter = ArrayAdapter.createFromResource(this,R.array.Minute,android.R.layout.simple_spinner_dropdown_item);
        MitEnd.setAdapter(adapter);





    }
    public void mOnPopupClick(View v) {
        Intent intent = new Intent(this, DateSelection.class);
        intent.putExtra("data", "Date select");
        startActivityForResult(intent, 1);
    }
    public void mOnPopupClick2(View v) {
        Intent intent = new Intent(this, DateSelection.class);
        intent.putExtra("data", "Date select");
        startActivityForResult(intent, 2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                getDatestr = findViewById(R.id.DayButton);
                String temp = data.getStringExtra("result");
                getDatestr.setText(temp);
            }
        } else if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                getDataend = findViewById(R.id.endDayButton);
                String temp = data.getStringExtra("result");
                getDataend.setText(temp);
            }
        }
    }


}
