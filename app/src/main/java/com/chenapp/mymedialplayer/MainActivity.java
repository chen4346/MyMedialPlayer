package com.chenapp.mymedialplayer;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.chenapp.base.BasePager;
import com.chenapp.pager.LocalMusicPager;
import com.chenapp.pager.LocalVideoPager;
import com.chenapp.pager.NetMusicPager;
import com.chenapp.pager.NetVideoPager;

import java.util.ArrayList;

public class MainActivity extends Activity implements RadioGroup.OnCheckedChangeListener {
    FragmentManager fm;
    private final ArrayList<BasePager> pager = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fm = getFragmentManager();

        pager.add(new LocalVideoPager(this));
        pager.add(new LocalMusicPager(this));
        pager.add(new NetVideoPager(this));
        pager.add(new NetMusicPager(this));
        RadioGroup rgTag = (RadioGroup) findViewById(R.id.rg_bottomtag);
        RadioButton rbLocalVideo = (RadioButton) findViewById(R.id.rb_localvideo);
        rgTag.setOnCheckedChangeListener(this);
        rbLocalVideo.setChecked(true);

    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        switch (radioGroup.getCheckedRadioButtonId()){
            case R.id.rb_localvideo:
                setPager(0);
                break;
            case R.id.rb_localmusic:
                setPager(1);
                break;
            case R.id.rb_netvideo:
                setPager(2);
                break;
            case R.id.rb_netmusic:
                setPager(3);
                break;
        }
    }
    private void setPager(final int position){
        FragmentTransaction ft  = fm.beginTransaction();
        ft.replace(R.id.fl_pager,new Fragment(){
            @Nullable
            @Override
            public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
                BasePager basePager = pager.get(position);
                if(basePager!=null){
                    if(!basePager.isInitData){
                        basePager.initData();
                        //避免每次进入都初始化数据
                        basePager.isInitData = true;
                    }
                    return basePager.rootView;
                }
                return null;
            }
        });
        ft.commit();
    }
}
