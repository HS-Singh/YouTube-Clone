package com.example.youtubeplayer;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class GetURLTask extends AsyncTask<String , Void, String> {

    private Context mContext;

    public interface onTaskComplete{
        public void updateUI(String url);
    }

    onTaskComplete listener;

    GetURLTask(Context context, onTaskComplete listener)
    {
        this.listener=listener;
        mContext=context;

    }


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
        listener.updateUI(downloadUrl);


    }

}
