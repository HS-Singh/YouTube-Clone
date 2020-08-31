package com.example.youtubeplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.google.android.exoplayer2.ui.PlayerView;

public class MainActivity extends AppCompatActivity {


    PlayerView pv;
    TextView titletext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pv=findViewById(R.id.video);
        titletext=findViewById(R.id.Videotitle);

        pv.setPlayer(ExoPlayerWrapper.getExoPlayer(getApplicationContext()));
        Intent j =getIntent();
        String id = j.getStringExtra("VideoId");
        titletext.setText(j.getStringExtra("ttl"));


                String url = "https://video.genyt.net/" + id;
                Intent i = new Intent(MainActivity.this, BackgroundExoPlayer.class);
                i.putExtra("URL", url);
                i.putExtra("notifi_Title", j.getStringExtra("ttl"));
                startService(i);




    }

    @Override
    public void onResume() {

        super.onResume();
        pv.setPlayer(ExoPlayerWrapper.getExoPlayer(getApplicationContext()));
    }

    @Override
    public void onPause() {

        super.onPause();
        pv.setPlayer(null);
    }

    @Override
    public void onDestroy() {

        super.onDestroy();


    }


}
