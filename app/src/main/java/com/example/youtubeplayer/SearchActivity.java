package com.example.youtubeplayer;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class SearchActivity extends AppCompatActivity {

    RecyclerView rv;
    ArrayList<VideoItem> VidItems=null;
    int FLAG=0;

    @Override
    public void onDestroy() {

        super.onDestroy();
    //    Intent i = new Intent(getApplicationContext(), BackgroundExoPlayer.class);
     //   stopService(i);
    }

    @Override
    protected void onCreate(Bundle savedInstance)
    {
        super.onCreate(savedInstance);
        setContentView(R.layout.search_view_layout);

        rv =findViewById(R.id.recyclerView);
        VidItems=new ArrayList<>();
        FLAG=0;

        new FetchItems().execute(getURLforRecent());

    }

    @Override
    public boolean onCreateOptionsMenu( Menu menu)
    {

            getMenuInflater().inflate(R.menu.search_menu, menu);
        MenuItem searchitem=menu.findItem(R.id.search_btn);
        final SearchView view= (SearchView) searchitem.getActionView();
        view.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //////////////////////////////////////////
                new FetchItems().execute(getSearchURL(query));
                FLAG=1;

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId())
        {


            default:
                return true;
        }

    }

    public String getSearchURL(String query)
    {
        VidItems = new ArrayList<>();
        Uri uri = Uri.parse("https://www.googleapis.com/youtube/v3/search").buildUpon().appendQueryParameter("part", "snippet").appendQueryParameter("maxResults", "25").appendQueryParameter("q", query).appendQueryParameter("type", "video").appendQueryParameter("key", "AIzaSyC3DozrjPo4bppoN8p60ZjzXc23UAAARBw").build();
        return  uri.toString();
    }








    public String getURLforRecent()
    {
        VidItems = new ArrayList<>();
        String base = "https://www.googleapis.com/youtube/v3/videos?part=snippet%2CcontentDetails%2Cstatistics&chart=mostPopular&regionCode=IN&key=AIzaSyC3DozrjPo4bppoN8p60ZjzXc23UAAARBw";
        return base;

    }

    class FetchItems extends AsyncTask<String , Void , String>
    {

        @Override
        protected String doInBackground(String... strings) {
            String urlString = strings[0];
            URL url = null;
            try {
               url = new URL(urlString);
            }
            catch(MalformedURLException mue)
            { }
            HttpURLConnection conn=null;


            try {
                if (url != null) {
                    conn = (HttpURLConnection) url.openConnection();
                    InputStream in = conn.getInputStream();
                    Scanner sc  = new Scanner(in);
                    sc.useDelimiter("\\A");
                    boolean hasInput = sc.hasNext();
                    if(hasInput)
                    {
                        return sc.next();
                    }

                }
                return null;
            }
            catch(IOException ioe)
            {

            }
            finally {
                conn.disconnect();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String response)
        {
            JSONObject res;
           try {
               res = new JSONObject(response);
               JSONArray items = res.getJSONArray("items");
               for(int i=0;i<items.length();i++)
               {
                   JSONObject item = items.getJSONObject(i);
                   String videoId=null;
                   if(FLAG==0)
                       videoId = item.getString("id");


                   else if(FLAG==1)
                   {
                       JSONObject jo= item.getJSONObject("id");
                       videoId=jo.getString("videoId");
                       Log.d("id"+i, videoId);
                   }



                   JSONObject snippet = item.getJSONObject("snippet");
                   String title = snippet.getString("title");
                   JSONObject thumbnails = snippet.getJSONObject("thumbnails");
                   JSONObject medium_thumb = thumbnails.getJSONObject("medium");
                   String thumb_urlString  = medium_thumb.getString("url");

                   VideoItem Viditem  = new VideoItem(title, thumb_urlString, videoId);
                   VidItems.add(Viditem);
               }

           }
           catch (JSONException joe)
           {}

           rv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
           rv.setAdapter(new RecyclerViewAdapter(VidItems));




        }

    }


    class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

        ArrayList<VideoItem> items;
        RecyclerViewAdapter(ArrayList<VideoItem> items)
        {
            this.items=items;
        }


        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            View v= inflater.inflate(R.layout.list_item_layout,parent,  false);
            return new MyViewHolder(v);
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            holder.setTitle_text(items.get(position).getTitle());
            holder.setThumbnail_img(items.get(position).getImg_url());

        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            private TextView title_text;
            private ImageView thumbnail_img;


            public void setTitle_text(String title_text) {
                this.title_text.setText(title_text);
            }

            public void setThumbnail_img(String url) {
                Picasso.get().load(url).into(this.thumbnail_img);
                //this.thumbnail_img.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.exo_controls_play));
            }

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                title_text = itemView.findViewById(R.id.vidTitle);
                thumbnail_img = itemView.findViewById(R.id.vidThumb);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(SearchActivity.this, MainActivity.class);
                        i.putExtra("VideoId", items.get(getAdapterPosition()).getVideoId());
                        i.putExtra("ttl", items.get(getAdapterPosition()).getTitle());
                        startActivity(i);
                    }
                });
            }


        }
    }
}
