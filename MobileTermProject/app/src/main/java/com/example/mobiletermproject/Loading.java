package com.example.mobiletermproject;


import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ProgressBar;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class Loading extends Activity {
    TextView LoadingState;
    ProgressBar bar;
    ProgressHandler handler;
    boolean isRunning = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        bar = (ProgressBar) findViewById(R.id.progress);
        LoadingState = findViewById(R.id.LoadingText);
        handler = new ProgressHandler();

    }
    @Override
    protected void onStart() {
        super.onStart();
        bar.setProgress(0);
        Thread thread1 = new Thread(new Runnable() {
            public void run() {
                try {
                    for (int i = 0; i < 20 && isRunning; i++) {
                        Thread.sleep(50);
                        Message msg = handler.obtainMessage();
                        handler.sendMessage(msg);
                    }
                } catch (Exception ex) {
                    Log.e("MainActivity", "Exception in processing message.", ex);
                }
            }
        });
        isRunning = true;
        thread1.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        isRunning = false;
    }

    public class ProgressHandler extends Handler {
        public void handleMessage(Message msg) {
            bar.incrementProgressBy(5);
            if (bar.getProgress() == bar.getMax()) {
                LoadingState.setText("Done");
                startActivity(new Intent(Loading.this, Calendar_test.class));
                finish();
            } else {
                LoadingState.setText("Loading..." + bar.getProgress());
            }
        }
    }
}

