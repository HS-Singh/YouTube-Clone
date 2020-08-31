package com.example.youtubeplayer;

import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import java.util.ConcurrentModificationException;

public class UpdateNotification extends IntentService {

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     *
     */
    public UpdateNotification() {
        super(UpdateNotification.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Context context=getApplicationContext();
        if(!ExoPlayerWrapper.getExoPlayer(context).getPlayWhenReady())
        {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "MusicChannel");

            builder.setContentTitle(intent.getStringExtra("ttl"))
                    .setContentIntent(PendingIntent.getActivity(getApplicationContext(),0,new Intent(getApplicationContext(),  MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT));
            builder.setSmallIcon(R.drawable.exo_controls_play);

            Intent i =new Intent();
            i.setAction(NotificationReceiver.RESUME_ACTION);
            i.setClass(this, NotificationReceiver.class);
            i.putExtra("ttl", intent.getStringExtra("ttl"));
            PendingIntent pi  = PendingIntent.getBroadcast(this, 1, i, PendingIntent.FLAG_UPDATE_CURRENT);

            Intent kk =new Intent();
            kk.setAction(NotificationReceiver.STOP_ACTION);
            kk.setClass(this, NotificationReceiver.class);
            PendingIntent pi2  = PendingIntent.getBroadcast(this, 12, kk, PendingIntent.FLAG_UPDATE_CURRENT);


            NotificationCompat.Action action = new NotificationCompat.Action.Builder(R.drawable.exo_controls_play, "Resume", pi).build();
            NotificationCompat.Action stopAction = new NotificationCompat.Action.Builder(R.drawable.exo_controls_pause, "Stop", pi2).build();


            builder.addAction(action);
            builder.addAction(stopAction);
            startForeground(1337, builder.build());




        }
        else {

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "MusicChannel");

            builder.setContentTitle(intent.getStringExtra("ttl"))
                    .setContentIntent(PendingIntent.getActivity(getApplicationContext(),0,new Intent(getApplicationContext(),  MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT));
            builder.setSmallIcon(R.drawable.exo_controls_play);

            Intent i =new Intent();
            i.setAction(NotificationReceiver.PAUSE_ACTION);
            i.setClass(this, NotificationReceiver.class);
            i.putExtra("ttl", intent.getStringExtra("ttl"));
            PendingIntent pi  = PendingIntent.getBroadcast(this, 1, i, PendingIntent.FLAG_UPDATE_CURRENT);

            Intent kk =new Intent();
            kk.setAction(NotificationReceiver.STOP_ACTION);
            kk.setClass(this, NotificationReceiver.class);
            PendingIntent pi2  = PendingIntent.getBroadcast(this, 12, kk, PendingIntent.FLAG_UPDATE_CURRENT);


            NotificationCompat.Action action = new NotificationCompat.Action.Builder(R.drawable.exo_controls_pause, "Pause", pi).build();
            NotificationCompat.Action stopAction = new NotificationCompat.Action.Builder(R.drawable.exo_controls_pause, "Stop", pi2).build();


            builder.addAction(action);
            builder.addAction(stopAction);


            startForeground(1337, builder.build());

        }
    }
}
