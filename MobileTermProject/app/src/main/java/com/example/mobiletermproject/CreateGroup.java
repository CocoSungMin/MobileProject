package com.example.mobiletermproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class CreateGroup extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_create_group);

        final EditText gr_name = (EditText) findViewById(R.id.gr_name);
        final EditText gr_number = (EditText) findViewById(R.id.gr_number);

        //확인버튼 이벤트
        Button button_ok = (Button) findViewById(R.id.bt_ok);
        button_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //데이터 전달하고 액티비티 닫기
                String name = gr_name.getText().toString();
                String number = gr_number.getText().toString();
                Intent intent = new Intent();
                intent.putExtra("그룹이름 : ", name);
                intent.putExtra("인원 제한 : ", number);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }


    //바깥영역 클릭 방지와 백 버튼 차단
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction()== MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }
}

