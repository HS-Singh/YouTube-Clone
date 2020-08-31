package com.example.youtubeplayer;

import android.content.Context;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.ExoPlayerLibraryInfo;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;

public class ExoPlayerWrapper{

    private static SimpleExoPlayer exoPlayer;

    public static SimpleExoPlayer getExoPlayer(Context context)
    {
        if(exoPlayer==null)
            exoPlayer= ExoPlayerFactory.newSimpleInstance(context, new DefaultTrackSelector());



        return exoPlayer;
    }

    public static void release()
    {
        if(exoPlayer!=null)
            exoPlayer.release();
        exoPlayer=null;
    }

}
