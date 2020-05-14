package com.example.mobiletermproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.HashMap;
import java.util.Map;

import io.opencensus.tags.Tag;
import io.opencensus.tags.Tags;

public class CreateSchedule extends AppCompatActivity {
    private static final String TAG = "tag";
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
    private EditText title;
    private EditText content;

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

    public void registerSchedule(View v){
        final Intent intent = new Intent(this,Calendar_test.class);

        title = findViewById(R.id.schduleTitle);
        content = findViewById(R.id.schduleContent);
        stramfm = findViewById(R.id.stramfm);
        Hourstr = findViewById(R.id.Hour);
        Mitstr = findViewById(R.id.Minute);
        endamfm = findViewById(R.id.endamfm);
        HourEnd = findViewById(R.id.endHour);
        MitEnd = findViewById(R.id.endMinute);
        String id = null;
        FirebaseUser user1 = FirebaseAuth.getInstance().getCurrentUser();
        if (user1 != null) {
            String uid = user1.getUid();
            id = uid;
        }

        if(id != null) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            Map<String, Object> user = new HashMap<>();
            user.put("id", id);
            user.put("ScheduleName", title.getText().toString());
            user.put("StartTime", stramfm.getSelectedItem().toString() + " " +
                    Hourstr.getSelectedItem().toString() + " : " + Mitstr.getSelectedItem().toString());
            user.put("Start Date", getDatestr.getText().toString());
            user.put("EndTime", endamfm.getSelectedItem().toString() + " " +
                    HourEnd.getSelectedItem().toString() + " : " +
                    MitEnd.getSelectedItem().toString());
            user.put("End Date", getDataend.getText().toString());
            user.put("Content", content.getText().toString());

            db.collection("users").
                    add(user).
                    addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d(TAG, "DocumentSnapshot added with ID" + documentReference.getId());
                            startActivity(intent);
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.w(TAG, "Error adding document", e);
                }
            });
        }
        else{
            Toast.makeText(this,"Fail",Toast.LENGTH_SHORT);
        }


    }


}
