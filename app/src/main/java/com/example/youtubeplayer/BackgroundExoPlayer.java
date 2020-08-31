package com.example.youtubeplayer;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRouter;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;


import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;

import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class BackgroundExoPlayer extends Service {

    private SimpleExoPlayer player;
    String videoTitle=null;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void initialisePlayer(String downloadUrl)
    {
        player = ExoPlayerWrapper.getExoPlayer(getApplicationContext());

        player.addListener(new Player.DefaultEventListener()
        {
            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int state)
            {
                if(state == Player.STATE_ENDED)
                    stopSelf();
            }

        });

        Uri uri = Uri.parse(downloadUrl);
        MediaSource ms = buildMediaSource(uri);
        player.setPlayWhenReady(true);
        player.seekTo(0);
        player.prepare(ms, false, false);
        startForeground(1337, buildMediaNotification());


    }

    private Notification buildMediaNotification() {

        NotificationManager manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        String channelID = "MusicChannel";

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel channel = new NotificationChannel(channelID , "YoutubePlayer Notifications", NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("Video Playing Channel");
            manager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder= new NotificationCompat.Builder(getApplicationContext(),channelID);
        builder.setContentTitle(videoTitle )
                .setContentIntent(PendingIntent.getActivity(getApplicationContext(),0,new Intent(getApplicationContext(),  MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT), PendingIntent.FLAG_UPDATE_CURRENT));
                builder.setSmallIcon(R.drawable.exo_controls_play);

                Intent i =new Intent();
                i.setAction(NotificationReceiver.PAUSE_ACTION);
                i.setClass(this, NotificationReceiver.class);
                i.putExtra("ttl", videoTitle);
                PendingIntent pi  = PendingIntent.getBroadcast(this, 1, i, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent kk =new Intent();
        kk.setAction(NotificationReceiver.STOP_ACTION);
        kk.setClass(this, NotificationReceiver.class);
        kk.putExtra("ttl", videoTitle);
        PendingIntent pi2  = PendingIntent.getBroadcast(this, 12, kk, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Action action = new NotificationCompat.Action.Builder(R.drawable.exo_controls_pause, "Pause", pi).build();
        NotificationCompat.Action stopAction = new NotificationCompat.Action.Builder(R.drawable.exo_controls_pause, "Stop", pi2).build();

        builder.addAction(action);
        builder.addAction(stopAction);





        return builder.build();




    }

    private MediaSource buildMediaSource(Uri uri)
    {
        return new ExtractorMediaSource.Factory(new DefaultHttpDataSourceFactory("exoplayer-codelab")).createMediaSource(uri);
    }



    @Override
    public void onCreate()
    {


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        super.onStartCommand(intent, flags, startId);

        Bundle bundle = intent.getExtras();
        String url = bundle.getString("URL", null);
        videoTitle = bundle.getString("notifi_Title");

        Log.d("Meine URL", url);
        if(url!=null)
            new BackgroundThread().execute(url);






        return  START_STICKY;
    }



    @Override
    public void onDestroy()
    {
        player.stop();
        stopForeground(true);
    }

    public class BackgroundThread extends AsyncTask<String , Void, String> {


        @Override
        protected String doInBackground(String... strings) {
            String url=strings[0];
            String downloadUrl=null;
            try {
                Document doc = Jsoup.connect(url).get();

                Elements link = doc.select("a");
                String linksurl=null;
                for(Element l : link)
                {
                    String kk = l.attr("href");
                    if(l.attr("id").equals("getlink")) {
                        Log.d("Link", kk);
                        linksurl=kk;break;
                    }
                }

                     Document docc=doc;
                if(linksurl!=null)
                    docc = Jsoup.connect(linksurl).get();
                Element downloadlink = docc.select("a[download]").first();
                downloadUrl = downloadlink.attr("href");
            }
            catch (IOException ioe)
            {

            }
            return  downloadUrl;
        }

        @Override
        protected  void onPostExecute(String downloadUrl)
        {

            initialisePlayer(downloadUrl);


        }

    }



}

