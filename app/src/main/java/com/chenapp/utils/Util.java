package com.chenapp.utils;

/**
 * Created by Administrator on 2016/10/23 0023.
 */

public class Util {
    public static String formatTime(long time){
        time = time / 1000;
        if(time/3600 == 0){
            return addZero(time%3600/60)+":"+addZero(time%3600%60);
        }else {
            return addZero(time/3600)+":"+addZero(time%3600/60)+":"+addZero(time%3600%60);
        }

    }
    public static String addZero(long num){
        if(num<10){
            return "0"+num;
        }else{
            return num+"";
        }
    }
}
