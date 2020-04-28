package com.example.mobiletermproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class RegisterActicity extends AppCompatActivity {
    private ArrayAdapter adapter;
    private Spinner spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_acticity);

        Button submit = (Button)findViewById(R.id.registerButton);

        spinner=(Spinner)findViewById(R.id.jobSpinner);
        adapter = ArrayAdapter.createFromResource(this,R.array.job,android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goback = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(goback);
            }
        });

    }

}
