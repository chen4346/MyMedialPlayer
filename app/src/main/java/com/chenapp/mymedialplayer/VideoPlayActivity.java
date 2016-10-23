package com.chenapp.mymedialplayer;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.chenapp.bean.VideoInfo;
import com.chenapp.utils.Util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class VideoPlayActivity extends Activity implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener, SeekBar.OnSeekBarChangeListener, View.OnClickListener {

    private VideoView vv ;
    private TextView tvTime,tvCurrent,tvSystime,tvVideoName;
    private SeekBar sbVideo;
    private static final int PROGRESS = 1;
    private static final int CONTROL = 2;
    private ImageView ivBattery;
    private BatteryReceiver batteryReceiver;
    private ArrayList<VideoInfo> list;
    private int currentSelection;
    private VideoInfo videoInfo;
    private Button btNext,btPre,btExit,btPause;
    private Uri uri;
    private GestureDetector detector;
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_video_play);
        initView();

        Intent intent  = getIntent();
        Bundle bundle = intent.getBundleExtra("playerlist");
        list = (ArrayList<VideoInfo>) bundle.getSerializable("playerlist");
        currentSelection = bundle.getInt("selection");
        vv = (VideoView) findViewById(R.id.vv_video);
        videoInfo = list.get(currentSelection);
        detector = new GestureDetector(this,new MyGesture());

        setListener();

        if(list!=null && list.size()>0){
            setUri();
        }else{
            finish();
        }
        /*Uri uri  = Uri.parse(intent.getStringExtra("data"));
        if(uri!=null){
            vv.setVideoURI(uri);
        }*/

        batteryReceiver = new BatteryReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(batteryReceiver,intentFilter);
    }

    private void setUri() {
        uri  = Uri.parse(videoInfo.getData());
        if(uri!=null){
            vv.setVideoURI(Uri.parse(videoInfo.getData()));
        }
    }

    private void setListener(){
        vv.setOnPreparedListener(this);
        vv.setOnErrorListener(this);
        vv.setOnCompletionListener(this);
        sbVideo.setOnSeekBarChangeListener(this);
        btPre.setOnClickListener(this);
        btNext.setOnClickListener(this);
        btExit.setOnClickListener(this);
        btPause.setOnClickListener(this);
    }
    private void initView(){
        view = findViewById(R.id.video_control);
        tvTime = (TextView) findViewById(R.id.tv_total_time);
        tvCurrent = (TextView) findViewById(R.id.tv_now_time);
        sbVideo = (SeekBar) findViewById(R.id.sb_video);
        ivBattery = (ImageView) findViewById(R.id.iv_batteryinfo);
        tvSystime = (TextView) findViewById(R.id.tv_systime);
        tvVideoName = (TextView) findViewById(R.id.tv_videoinfo);
        btExit = (Button) findViewById(R.id.btn_exit);
        btNext = (Button) findViewById(R.id.btn_video_next);
        btPre = (Button) findViewById(R.id.btn_video_pre);
        btPause = (Button) findViewById(R.id.btn_video_start_pause);

    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(batteryReceiver);
        super.onDestroy();
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        vv.start();
        mHandle.sendEmptyMessageDelayed(CONTROL,3000);
        tvVideoName.setText(videoInfo.getName());
        int duration = vv.getDuration();
        sbVideo.setMax(duration);
        tvTime.setText(Util.formatTime(duration));
        mHandle.sendEmptyMessage(PROGRESS);
    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
        Toast.makeText(this,"格式错误",Toast.LENGTH_SHORT);
        return false;
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        next();
    }
    private Handler mHandle = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case PROGRESS:
                    int currentPosition  = vv.getCurrentPosition();
                    sbVideo.setProgress(currentPosition);
                    tvCurrent.setText(Util.formatTime(currentPosition));
                    tvSystime.setText(getTime());
                    removeMessages(PROGRESS);
                    sendEmptyMessageDelayed(PROGRESS,1000);
                    break;
                case CONTROL:
                    view.setVisibility(View.GONE);
                    break;
            }
        }
    };
    private String getTime(){
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        return format.format(new Date());
    }
    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        if(b){
            vv.seekTo(i);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        mHandle.removeMessages(CONTROL);
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mHandle.sendEmptyMessageDelayed(CONTROL,3000);
    }

    @Override
    public void onClick(View view) {
        mHandle.removeMessages(CONTROL);
        mHandle.sendEmptyMessageDelayed(CONTROL,3000);
        switch (view.getId()){
            case R.id.btn_exit:

                break;
            case R.id.btn_video_next:
                next();
                break;
            case R.id.btn_video_pre:
                currentSelection--;
                if(currentSelection<0){
                    currentSelection = list.size()-1;
                }
                videoInfo = list.get(currentSelection);
                uri = Uri.parse(videoInfo.getData());
                if(uri!=null){
                    vv.setVideoURI(uri);
                }
                break;
            case R.id.btn_video_start_pause:
                startAndPause();
                break;
        }
    }

    private void startAndPause() {
        if(vv.isPlaying()){
            vv.pause();
            btPause.setBackgroundResource(R.drawable.bt_video_start_selector);
        }else{
            vv.start();
            btPause.setBackgroundResource(R.drawable.bt_video_pause_selector);
        }
    }

    private void next() {
        currentSelection++;
        if(currentSelection==list.size()){
            currentSelection = 0;
        }
        videoInfo = list.get(currentSelection);
        uri = Uri.parse(videoInfo.getData());
        if(uri!=null){
            vv.setVideoURI(uri);
        }
    }


    private class BatteryReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            int level = intent.getIntExtra("level",0);
            setBatteryInfo(level);
        }
        private void setBatteryInfo(int level) {
            if(level<=0){
                ivBattery.setImageResource(R.drawable.ic_battery_0);
            }else if(level<=10){
                ivBattery.setImageResource(R.drawable.ic_battery_10);
            }else if(level<=20){
                ivBattery.setImageResource(R.drawable.ic_battery_20);
            }else if(level<=40){
                ivBattery.setImageResource(R.drawable.ic_battery_40);
            }else if(level<=60){
                ivBattery.setImageResource(R.drawable.ic_battery_60);
            }else if(level<=80){
                ivBattery.setImageResource(R.drawable.ic_battery_80);
            }else if(level<=100){
                ivBattery.setImageResource(R.drawable.ic_battery_100);
            }else {
                ivBattery.setImageResource(R.drawable.ic_battery_100);
            }

        }
    }

    private class MyGesture extends GestureDetector.SimpleOnGestureListener{
        @Override
        public void onLongPress(MotionEvent e) {
            super.onLongPress(e);
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            return super.onDoubleTap(e);
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {

            hideAndViewControl();
            return super.onSingleTapConfirmed(e);
        }
    }
    private void hideAndViewControl(){
        if(view.getVisibility()==View.GONE){
            mHandle.sendEmptyMessageDelayed(CONTROL,3000);
            view.setVisibility(View.VISIBLE);
        }else{
            mHandle.removeMessages(CONTROL);
            view.setVisibility(View.GONE);
        }
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        detector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }
}
