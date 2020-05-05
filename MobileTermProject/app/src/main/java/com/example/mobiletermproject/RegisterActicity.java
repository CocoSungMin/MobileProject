package com.example.mobiletermproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActicity extends AppCompatActivity {
    private ArrayAdapter adapter;
    private Spinner spinner;

    private EditText email_join;
    private EditText pwd_join;
    private Button btn;
    private Button cancleBtn;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_acticity);

        email_join = (EditText) findViewById(R.id.idText);
        pwd_join = (EditText) findViewById(R.id.passwordText);
        btn = (Button) findViewById(R.id.registerButton);
        cancleBtn = (Button)findViewById(R.id.cancleButton);

        firebaseAuth = FirebaseAuth.getInstance();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = email_join.getText().toString();
                String pwd = pwd_join.getText().toString();

                firebaseAuth.createUserWithEmailAndPassword(email, pwd)
                        .addOnCompleteListener(RegisterActicity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Intent intent = new Intent(RegisterActicity.this, LoginActivity.class);
                                    Toast.makeText(RegisterActicity.this,"Success",Toast.LENGTH_SHORT).show();
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(RegisterActicity.this, "등록 에러", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }
                        });
            }
        });

        cancleBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent cancled = new Intent(RegisterActicity.this,LoginActivity.class);
                startActivity(cancled);
                finish();
            }
        });


    }
}
