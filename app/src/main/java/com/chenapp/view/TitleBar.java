package com.chenapp.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.chenapp.mymedialplayer.R;

/**
 * Created by Administrator on 2016/10/16 0016.
 */

public class TitleBar extends LinearLayout implements View.OnClickListener {
    private View tvSerach;
    private View rlGame;
    private View ivRecord;

    public TitleBar(Context context) {
        this(context,null);
    }

    public TitleBar(Context context, AttributeSet attrs) {
        this(context, attrs , 0);
    }

    public TitleBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    //布局加载完成后调用
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        tvSerach = getChildAt(1);
        rlGame = getChildAt(2);
        ivRecord = getChildAt(3);
        tvSerach.setOnClickListener(this);
        rlGame.setOnClickListener(this);
        ivRecord.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_title_search:
                Toast.makeText(getContext(),"aa",Toast.LENGTH_SHORT).show();
                break;
            case R.id.rl_title_game:
                Toast.makeText(getContext(),"bb",Toast.LENGTH_SHORT).show();
                break;
            case R.id.iv_title_record:
                Toast.makeText(getContext(),"cc",Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
