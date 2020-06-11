package com.example.mobiletermproject;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class CreateSchedule extends AppCompatActivity {
    private static final String TAG = "tag";

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

    boolean isEdit = false;
    String editID;

    //날짜, 시간 선택했는지 확인하기 위한 값
    int flag1 = 0;
    int flag2 = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_schedule);
        //DatePickerDialog로 UI변경해볼까 해서 임시로 추가한 코드(72~23)
        dateTimeIn = findViewById(R.id.date_time_input);
        dateTimeIn.setInputType(InputType.TYPE_NULL);

        getDataend = findViewById(R.id.endDayButton);
        getDatestr = findViewById(R.id.DayButton);

        title = findViewById(R.id.schduleTitle);
        content = findViewById(R.id.schduleContent);

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

        //일정 수정으로 왔을 시
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            isEdit = true;
            Schedule sch = (Schedule) bundle.getSerializable("selSchedule");
            editID = sch.getID();

            title.setText(sch.getTitle());
            content.setText(sch.getContent());

            getDatestr.setText(sch.startDateToString());
            getDataend.setText(sch.endDateToString());

            Hourstr.setSelection(sch.startTimeByClass().getHour());
            Mitstr.setSelection(sch.startTimeByClass().getMinute() / 5);
            HourEnd.setSelection(sch.endTimeByClass().getHour());
            MitEnd.setSelection(sch.endTimeByClass().getMinute() / 5);
        }
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
                flag1 = 1;
                getDatestr.setText(temp);
            }
        } else if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                getDataend = findViewById(R.id.endDayButton);
                String temp = data.getStringExtra("result");
                flag1 = 2;
                getDataend.setText(temp);
            }
        }
    }

    public void registerSchedule(View v) {


        String[] startDate = getDatestr.getText().toString().split("[.]");
        String[] endDate = getDataend.getText().toString().split("[.]");

        int strH = Integer.parseInt(Hourstr.getSelectedItem().toString());
        int strM = Integer.parseInt(Mitstr.getSelectedItem().toString());
        int endH = Integer.parseInt(HourEnd.getSelectedItem().toString());
        int endM = Integer.parseInt(MitEnd.getSelectedItem().toString());

        //오류 메세지 출력
        if (flag1 != 2) {
            Toast.makeText(CreateSchedule.this, "일정 날짜가 입력되지 않았습니다.", Toast.LENGTH_SHORT).show();
        }
        else if (getDatestr.getText().toString().equals("") || getDataend.getText().toString().equals("") ) {
            Toast.makeText(CreateSchedule.this, "일정 날짜가 입력되지 않았습니다.", Toast.LENGTH_SHORT).show();
        }
        else if (Integer.parseInt(startDate[1] + startDate[2]) - Integer.parseInt(endDate[1] + endDate[2]) > 0) {
            Toast.makeText(CreateSchedule.this, "일정 날짜가 정상적으로 입력되지 않았습니다.", Toast.LENGTH_SHORT).show();
        }
        else if (Integer.parseInt(startDate[1] + startDate[2]) - Integer.parseInt(endDate[1] + endDate[2]) == 0 &&
                ((strH - endH > 0)||((strH - endH == 0)&&(strM - endM > 0)))) {
            Toast.makeText(CreateSchedule.this, "시간이 정상적으로 입력되지 않았습니다.", Toast.LENGTH_SHORT).show();
        }
        else if (content.getText().toString().equals("")) {
            Toast.makeText(CreateSchedule.this, "일정 내용이 입력되지 않았습니다.", Toast.LENGTH_SHORT).show();
        }
        else if (title.getText().toString().equals("")) {
            Toast.makeText(CreateSchedule.this, "일정 제목이 입력되지 않았습니다.", Toast.LENGTH_SHORT).show();
        }
        else {

            Schedule schedule = new Schedule(title.getText().toString(), content.getText().toString(),
                    LocalDateTime.of(Integer.parseInt(startDate[0]), Integer.parseInt(startDate[1]), Integer.parseInt(startDate[2]), strH, strM),
                    LocalDateTime.of(Integer.parseInt(endDate[0]), Integer.parseInt(endDate[1]), Integer.parseInt(endDate[2]), endH, endM));

            ////////////////////////////////////////////

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
                if (isEdit) {//수정일 경우
                    db.collection(finalId).document(editID).set(schedule, SetOptions.merge())
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "DocumentSnapshot successfully written!");
                                    finish();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error writing document", e);
                                }
                            });
                } else {// 그냥 추가일 경우
                    db.collection(finalId).document()
                            .set(schedule, SetOptions.merge())
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "DocumentSnapshot successfully written!");
                                    finish();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error writing document", e);
                                }
                            });
                }
            } else {
                Toast.makeText(this, "Fail", Toast.LENGTH_SHORT);
            }
        }
    }
}
