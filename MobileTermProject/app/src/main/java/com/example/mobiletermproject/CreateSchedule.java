package com.example.mobiletermproject;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class CreateSchedule extends AppCompatActivity {
    private static final String TAG = "tag";
    private EditText schduleTitle;
    private EditText schduleContent;

    private ArrayAdapter adapter;
    private Spinner Hourstr;
    private Spinner Mitstr;
    private Spinner stramfm;
    private Spinner HourEnd;
    private Spinner MitEnd;
    private Spinner endamfm;
    private Button getDatestr;
    private Button getDataend;
    private EditText title;
    private EditText content;
    //DatePickerDialog로 UI변경해볼까 해서 임시로 추가한 코드(64)
    EditText dateTimeIn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_schedule);
        //DatePickerDialog로 UI변경해볼까 해서 임시로 추가한 코드(72~23)
        dateTimeIn = findViewById(R.id.date_time_input);
        dateTimeIn.setInputType(InputType.TYPE_NULL);

        schduleTitle = findViewById(R.id.schduleTitle);
        schduleContent = findViewById(R.id.schduleContent);

        getDataend = findViewById(R.id.endDayButton);
        getDatestr = findViewById(R.id.DayButton);

        stramfm = findViewById(R.id.stramfm);
        endamfm = findViewById(R.id.endamfm);
        adapter = ArrayAdapter.createFromResource(this, R.array.amfm, android.R.layout.simple_spinner_dropdown_item);
        stramfm.setAdapter(adapter);
        endamfm.setAdapter(adapter);

        Hourstr = findViewById(R.id.Hour);
        adapter = ArrayAdapter.createFromResource(this, R.array.Hour, android.R.layout.simple_spinner_dropdown_item);
        Hourstr.setAdapter(adapter);

        Mitstr = findViewById(R.id.Minute);
        adapter = ArrayAdapter.createFromResource(this, R.array.Minute, android.R.layout.simple_spinner_dropdown_item);
        Mitstr.setAdapter(adapter);


        HourEnd = findViewById(R.id.endHour);
        adapter = ArrayAdapter.createFromResource(this, R.array.Hour, android.R.layout.simple_spinner_dropdown_item);
        HourEnd.setAdapter(adapter);

        MitEnd = findViewById(R.id.endMinute);
        adapter = ArrayAdapter.createFromResource(this, R.array.Minute, android.R.layout.simple_spinner_dropdown_item);
        MitEnd.setAdapter(adapter);
        //DatePickerDialog로 UI변경해볼까 해서 임시로 추가한 코드(104~109)
        dateTimeIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimeDialog(dateTimeIn);
            }
        });

    }

    //DatePickerDialog로 UI변경해볼까 해서 임시로 추가한 코드(113~136)
    private void showDateTimeDialog(final EditText date_time_in) {
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy-MM-dd HH:mm");
                        date_time_in.setText(simpleDateFormat.format(calendar.getTime()));

                    }
                };
                new TimePickerDialog(CreateSchedule.this, timeSetListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show();

            }
        };
        new DatePickerDialog(CreateSchedule.this, dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void registerSchedule(View v) {
        final Intent intent = new Intent(this, Calendar_test.class);

        title = findViewById(R.id.schduleTitle);
        content = findViewById(R.id.schduleContent);
        stramfm = findViewById(R.id.stramfm);
        Hourstr = findViewById(R.id.Hour);
        Mitstr = findViewById(R.id.Minute);
        endamfm = findViewById(R.id.endamfm);
        HourEnd = findViewById(R.id.endHour);
        MitEnd = findViewById(R.id.endMinute);

        //////////////////
        String[] startDate = getDatestr.getText().toString().split("[.]");
        String[] endDate = getDataend.getText().toString().split("[.]");

        int strH = Integer.parseInt(Hourstr.getSelectedItem().toString());
        int strM = Integer.parseInt(Mitstr.getSelectedItem().toString());
        int endH = Integer.parseInt(HourEnd.getSelectedItem().toString());
        int endM = Integer.parseInt(MitEnd.getSelectedItem().toString());

        Schedule schedule = new Schedule(title.getText().toString(), content.getText().toString(),
                LocalDateTime.of(Integer.parseInt(startDate[0]), Integer.parseInt(startDate[1]), Integer.parseInt(startDate[2]), strH, strM),
                LocalDateTime.of(Integer.parseInt(endDate[0]), Integer.parseInt(endDate[1]), Integer.parseInt(endDate[2]), endH, endM));

        Log.d("test", schedule.toString());
        //////////////////


        String id = null;
        FirebaseUser user1 = FirebaseAuth.getInstance().getCurrentUser();
        if (user1 != null) {
            String uid = user1.getUid();
            id = uid;
        }

        if (id != null) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            Map<String, Object> user = new HashMap<>();

            user.put("schedule", schedule);

            final String finalId = id;
            db.collection(finalId).document()
                    .set(schedule, SetOptions.merge())
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "DocumentSnapshot successfully written!");
                            startActivity(intent);
                            finish();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error writing document", e);
                        }
                    });
        } else {
            Toast.makeText(this, "Fail", Toast.LENGTH_SHORT);
        }
    }


}
