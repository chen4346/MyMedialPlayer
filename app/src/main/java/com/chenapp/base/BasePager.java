package com.chenapp.base;

import android.content.Context;
import android.view.View;

/**
 * Created by Administrator on 2016/10/16 0016.
 */

public abstract class BasePager  {
    public View rootView;
    public Context mContext;
    public boolean isInitData;
    public BasePager(Context context){
        this.mContext = context;
        rootView = initView();

    }
    public abstract View initView();

    public void initData(){

    }
}
