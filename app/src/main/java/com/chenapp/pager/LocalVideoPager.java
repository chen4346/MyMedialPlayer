package com.chenapp.pager;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.chenapp.base.BasePager;
import com.chenapp.bean.VideoInfo;
import com.chenapp.mymedialplayer.R;
import com.chenapp.mymedialplayer.VideoPlayActivity;
import com.chenapp.utils.Util;

import java.util.ArrayList;

import static com.chenapp.mymedialplayer.R.id.tv_load;

/**
 * Created by Administrator on 2016/10/16 0016.
 */

public class LocalVideoPager extends BasePager {

    private ListView listView;
    private TextView tvLoad;
    private ProgressBar pbLoad;
    private ArrayList<VideoInfo> list;

    public LocalVideoPager(Context context) {
        super(context);
    }

    @Override
    public View initView() {
        View view = View.inflate(mContext, R.layout.pager_localvideo, null);
        listView = (ListView) view.findViewById(R.id.listview);
        tvLoad = (TextView) view.findViewById(tv_load);
        pbLoad = (ProgressBar) view.findViewById(R.id.pb_load);
        return view;
    }

    @Override
    public void initData() {
        super.initData();
        list = new ArrayList<>();
        tvLoad.setVisibility(View.GONE);
        new Thread() {
            @Override
            public void run() {
                isGrantExternalRW((Activity) mContext);
                ContentResolver contentResolver = mContext.getContentResolver();
                Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                String[] pro = new String[]{
                        MediaStore.Video.Media.DISPLAY_NAME,
                        MediaStore.Video.Media.SIZE,
                        MediaStore.Video.Media.DURATION,
                        MediaStore.Video.Media.DATA
                };
                Cursor cursor = contentResolver.query(uri, pro, null, null, null);
                if (cursor != null) {
                    while (cursor.moveToNext()) {
                        list.add(new VideoInfo().setName(cursor.getString(0))
                                .setSize(cursor.getLong(1))
                                .setTime(cursor.getLong(2))
                                .setData(cursor.getString(3)));
                    }
                    cursor.close();
                }
                mHandler.sendEmptyMessage(0);
            }
        }.start();
    }
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            pbLoad.setVisibility(View.GONE);
            if(list!=null && list.size()>0){
                tvLoad.setVisibility(View.GONE);
                listView.setAdapter(new MyAdapter());
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent intent = new Intent();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("playerlist",list);
                        bundle.putInt("selection",i);
                        intent.putExtra("playerlist",bundle);
                        intent.setClass(mContext, VideoPlayActivity.class);
                        mContext.startActivity(intent);
                    }
                });
            }else{
                tvLoad.setVisibility(View.VISIBLE);
            }
        }
    };
    private class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
           ViewHolder holder;
           if(view==null){
               view = View.inflate(mContext,R.layout.listview_video_item,null);
               holder = new ViewHolder();
               holder.tvName = (TextView) view.findViewById(R.id.tv_video_name);
               holder.tvTime = (TextView) view.findViewById(R.id.tv_video_time);
               holder.tvSize = (TextView) view.findViewById(R.id.tv_video_size);
               holder.ivIcon = (ImageView) view.findViewById(R.id.iv_icon);
               view.setTag(holder);
           }else{
               holder = (ViewHolder) view.getTag();
           }
                holder.tvName.setText(list.get(i).getName());
                holder.tvSize.setText(android.text.format.Formatter.formatFileSize(mContext, list.get(i).getSize()));
                holder.tvTime.setText(Util.formatTime(list.get(i).getTime()));
                holder.ivIcon.setImageResource(R.drawable.video_default_icon);
            return view;
        }

    }
    static class ViewHolder{
        TextView tvName,tvTime,tvSize;
        ImageView ivIcon;
    }

    public static boolean isGrantExternalRW(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && activity.checkSelfPermission(
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            activity.requestPermissions(new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, 1);

            return false;
        }

        return true;
    }


    public Bitmap getVideoThumbnail(String filePath) {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(filePath);
            bitmap = retriever.getFrameAtTime();
        }catch(IllegalArgumentException e) {
            e.printStackTrace();
        }catch (RuntimeException e) {
            e.printStackTrace();
        }
        finally {
            try {
                retriever.release();
            }catch (RuntimeException e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }


}
