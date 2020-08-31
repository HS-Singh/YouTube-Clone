package com.example.youtubeplayer;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;


import androidx.annotation.Nullable;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import java.io.IOException;

public class BackgroundService extends Service {

    MediaPlayer player;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate()
    {
        player=new MediaPlayer();
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        super.onStartCommand(intent, flags, startId);
        Bundle bundle = intent.getExtras();
        String url = bundle.getString("URL", null);
        Log.d("Meine URL", url);
       if(url!=null)
        new BackgroundThread().execute(url);






        return  START_STICKY;
    }



    @Override
    public void onDestroy()
    {
        player.stop();
    }

    public class BackgroundThread extends AsyncTask<String , Void, String> {


        @Override
        protected String doInBackground(String... strings) {
            String url=strings[0];
            String downloadUrl=null;
            try {
                Document doc = Jsoup.connect(url).get();
               // Document doc = Jsoup.parse("<a href=\"https://video.genyt.net/aqz-KE-bpKQ?dl=1&s=1589689372&h=f7a9aef94256dbc904ec66e732ddde09\" id=\"getlink\" class=\"btn btn-outline-danger btn-block mt-3\" role=\"button\" aria-pressed=\"true\">Generate Download Links</a>"); //Jsoup.connect(url).get();
                Log.d("HTML", doc.html().substring(100000));

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

                //String linksurl = link.attr("href");

                //Log.d("LinksUrl", linksurl);
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
            try {
                player.setDataSource(downloadUrl);
                player.prepare();
                player.start();
            }catch (IOException ioe){
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
            }


        }

    }



}
