package com.chenapp.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/10/18 0018.
 */

public class VideoInfo implements Serializable{
    private String name;
    private long size;
    private long time;
    private String data;

    public String getName() {
        return name;
    }

    public VideoInfo setName(String name) {
        this.name = name;
        return this;
    }

    public long getSize() {
        return size;
    }

    public VideoInfo setSize(long size) {
        this.size = size;
        return this;
    }

    public long getTime() {
        return time;
    }

    public VideoInfo setTime(long time) {
        this.time = time;
        return this;
    }

    public String getData() {
        return data;
    }

    public VideoInfo setData(String data) {
        this.data = data;
        return this;
    }
}
