package com.myapplication;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RemoteViews;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class MainActivity extends Activity {

    private final int NOTIFICATION_ID = 1; // 如果id设置为0,会导致不能设置为前台service
    private NotificationManager manager;
    private RemoteViews remoteViews;
    private Notification notification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //注册eventbus
        EventBus.getDefault().register(this);

        //注册点击事件
        findViewById(R.id.btn_set).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setNotification();
            }
        });

    }



    @Subscribe
    public void message(MusicBroadcastReceiver.MusicBroadcastMsg msg){
        switch(msg.msg){
            case MusicBroadcastReceiver.MSG_PLAY:{
                remoteViews.setTextViewText(R.id.wt_title, "播放");
                manager.notify(NOTIFICATION_ID, notification);
            }
            break;
            case MusicBroadcastReceiver.MSG_NEXT:{
                remoteViews.setTextViewText(R.id.wt_title, "下一首");
                manager.notify(NOTIFICATION_ID, notification);
            }
            break;
            case MusicBroadcastReceiver.MSG_PREVIOUS:{
                remoteViews.setTextViewText(R.id.wt_title, "上一首");
                manager.notify(NOTIFICATION_ID, notification);
            }
            break;
            case MusicBroadcastReceiver.MSG_CLEAR:{
                if (manager != null) {
                    manager.cancel(NOTIFICATION_ID);
                }
            }
            break;
        }
    }


    /**
     * 设置通知栏
     */
    public void setNotification() {
        remoteViews = new RemoteViews(getPackageName(), R.layout.widget);
        //设置文字
        remoteViews.setTextViewText(R.id.wt_title, "title");
        //设置图片
        remoteViews.setImageViewResource(R.id.icon1, R.drawable.music_file);

        remoteViews.setImageViewResource(R.id.wt_play, R.drawable.pause);


        //上一首
        Intent previous = new Intent(getString(R.string.previous));
        PendingIntent pi_previous = PendingIntent.getBroadcast(MainActivity.this, 0,
                previous, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.wt_previous, pi_previous);



        //播放
        Intent play = new Intent(getString(R.string.play));
        PendingIntent pi_play = PendingIntent.getBroadcast(MainActivity.this, 0,
                play, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.wt_play, pi_play);


        //下一首
        Intent next = new Intent(getString(R.string.next));
        PendingIntent pi_next = PendingIntent.getBroadcast(MainActivity.this, 0,
                next, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.wt_next, pi_next);

        //清除
        Intent clear = new Intent(getString(R.string.clear));
        PendingIntent pi_clear = PendingIntent.getBroadcast(MainActivity.this, 0,
                clear, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.wt_clear, pi_clear);


        notification = new Notification();
        notification.icon = R.drawable.music_file;
        notification.tickerText = "title";
        notification.when = System.currentTimeMillis();
        notification.flags = Notification.FLAG_AUTO_CANCEL;

        notification.contentView = remoteViews;
        manager = (NotificationManager) getSystemService(
                Context.NOTIFICATION_SERVICE);

        manager.notify(NOTIFICATION_ID, notification);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (manager != null) {
            manager.cancel(NOTIFICATION_ID);
        }

        EventBus.getDefault().unregister(this);
    }

}