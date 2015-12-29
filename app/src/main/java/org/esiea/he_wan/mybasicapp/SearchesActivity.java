package org.esiea.he_wan.mybasicapp;


import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class SearchesActivity extends AppCompatActivity {

    private static final String TAG = "SearchesActivity";

    private RecyclerView rv;

    public RecyclerView getRV(){
        return rv;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searches);
        rv = (RecyclerView) findViewById(R.id.rv_searches);
        if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        }
        else{
            rv.setLayoutManager(new GridLayoutManager(this.getApplicationContext(), 2));
        }
//        rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        SearchesAdapter searchesAdapter = new SearchesAdapter(getSearchesFromFile());

        rv.setAdapter(searchesAdapter);
//        IntentFilter intentFilter = new IntentFilter(SEARCHES_UPDATE);
//        LocalBroadcastManager.getInstance(this).registerReceiver(new BierUpdate(), intentFilter);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private JSONArray getSearchesFromFile() {
        try{
            InputStream is = new FileInputStream(getCacheDir() + "/"  + "searches.json");
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            is.close();
            JSONObject jsobj = new JSONObject(new String(buffer, "UTF-8"));
            return new JSONArray(jsobj.getString("items"));
        }catch(IOException e){
            e.printStackTrace();
            return new JSONArray();
        }catch(JSONException e){
            e.printStackTrace();
            return new JSONArray();
        }
    }

    private class SearchesAdapter extends RecyclerView.Adapter<SearchesAdapter.SearchHolder>{
        private JSONArray searches;

        public SearchesAdapter(JSONArray searches) {
            this.searches = searches;
//            System.out.println(searches.toString());
        }

        @Override
        public SearchHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemLayoutView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.rv_searches, parent, false);
            SearchHolder searchHolder = new SearchHolder(itemLayoutView);
            Log.d(TAG, "onCreateViewHolder");
            return searchHolder;
        }

        @Override
        public void onBindViewHolder(SearchHolder holder, int position) {
            try {
                JSONObject js_obj = searches.getJSONObject(position);
                JSONObject id = js_obj.getJSONObject("id");
                String html_link="#";
                String kind = id.getString("kind");
                if(kind.equals("youtube#channel")){
                    html_link = "https://www.youtube.com/channel/"+id.getString("channelId");
                }
                if(kind.equals("youtube#video")){
                    html_link = "https://www.youtube.com/watch?v="+id.getString("videoId");
                }

                JSONObject snippet = js_obj.getJSONObject("snippet");
                String image_link = snippet.getJSONObject("thumbnails").getJSONObject("default").getString("url");
                holder.video_title.setText(Html.fromHtml("<a href='"+html_link+"'>"+snippet.getString("title")+"</a>"));
                holder.video_title.setMovementMethod(LinkMovementMethod.getInstance());
//                Bitmap bitmap = this.getBitmapFromURL(image_link);
//                holder.video_image.setImageDrawable(this.LoadImageFromWebOperations(image_link));
                LoadImageFromURL loadImage = new LoadImageFromURL(image_link, holder.video_image);
                loadImage.execute();

                Log.d(TAG, "onBindViewHolder");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        @Override
        public int getItemCount() {
            return searches.length();
        }



        class SearchHolder extends RecyclerView.ViewHolder{

            public ImageView video_image;
            public TextView video_title;

            public SearchHolder(View itemView) {
                super(itemView);
                video_image = (ImageView) itemView.findViewById(R.id.video_image);
                video_title = (TextView) itemView.findViewById(R.id.video_title);
            }
        }
    }


    public class LoadImageFromURL extends AsyncTask<String, Void, Bitmap> {
        String image_link;
        ImageView iv;

        public LoadImageFromURL(String url, ImageView iv) {
            this.image_link = url;
            this.iv = iv;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            // TODO Auto-generated method stub

            try {
                URL url = new URL(image_link);
                InputStream is = url.openConnection().getInputStream();
                Bitmap bitMap = BitmapFactory.decodeStream(is);
                return bitMap;

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(Bitmap result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            iv.setImageBitmap(result);
        }

    }

}
