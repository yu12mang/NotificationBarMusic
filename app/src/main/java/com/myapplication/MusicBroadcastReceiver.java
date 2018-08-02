package com.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.greenrobot.eventbus.EventBus;

public class MusicBroadcastReceiver extends BroadcastReceiver {

    public static final int MSG_PLAY = 100;
    public static final int MSG_NEXT = 101;
    public static final int MSG_PREVIOUS = 102;
    public static final int MSG_CLEAR = 103;

    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();

        if (action.equals(context.getString(R.string.play))) {
            EventBus.getDefault().post(new MusicBroadcastMsg(MSG_PLAY));
        } else if (action.equals(context.getString(R.string.next))) {
            EventBus.getDefault().post(new MusicBroadcastMsg(MSG_NEXT));
        } else if (action.equals(context.getString(R.string.previous))) {
            EventBus.getDefault().post(new MusicBroadcastMsg(MSG_PREVIOUS));
        } else if (action.equals(context.getString(R.string.clear))) {
            EventBus.getDefault().post(new MusicBroadcastMsg(MSG_CLEAR));
        }
    }

    public class MusicBroadcastMsg{
        int msg;
        public MusicBroadcastMsg(int msg) {
            this.msg = msg;
        }
    }

}
