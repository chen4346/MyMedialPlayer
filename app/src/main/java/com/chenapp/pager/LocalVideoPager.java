package com.chenapp.pager;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.chenapp.base.BasePager;

/**
 * Created by Administrator on 2016/10/16 0016.
 */

public class LocalVideoPager extends BasePager {


    private TextView tv;

    public LocalVideoPager(Context context) {
        super(context);
    }

    @Override
    public View initView() {
        tv = new TextView(mContext);
        tv.setGravity(Gravity.CENTER);
        tv.setTextColor(Color.BLUE);
        tv.setTextSize(30);
        return tv;
    }

    @Override
    public void initData() {
        super.initData();
        tv.setText("本地视频");
    }
}
