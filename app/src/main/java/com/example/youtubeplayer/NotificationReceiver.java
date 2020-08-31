package com.example.youtubeplayer;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

public class NotificationReceiver extends BroadcastReceiver {

    public static final String RESUME_ACTION = "RESUME_ACTION";
    public static final String PAUSE_ACTION = "PAUSE_ACTION";
    public static final String STOP_ACTION = "STOP_ACTION";


    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction()!=null) {
            if(!intent.getAction().equals(STOP_ACTION)) {
                ExoPlayerWrapper.getExoPlayer(context).setPlayWhenReady(!ExoPlayerWrapper.getExoPlayer(context).getPlayWhenReady());
                Intent i = new Intent(context, UpdateNotification.class);
                i.putExtra("ttl", intent.getStringExtra("ttl"));
                context.startService(i);
            }
            else
            {
                Intent i = new Intent(context, BackgroundExoPlayer.class);
                context.stopService(i);
            }
        }
    }
}
