package com.chenapp.mymedialplayer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;

public class SplashActivity extends Activity {
    private boolean hasStart = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mHandler.sendEmptyMessageDelayed(0,2000);

    }
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            startMainActivity();
        }
    };

    private void startMainActivity() {
        if(!hasStart){
            hasStart = true;
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
            finish();
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mHandler.removeMessages(0);
        startMainActivity();
        return super.onTouchEvent(event);
    }
}
