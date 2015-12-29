package org.esiea.he_wan.mybasicapp;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

public class MainActivity extends AppCompatActivity{

    private static final String TAG = "org.esiea.MainActivity";

    private EditText et_search_youtube;
    private static Button btn_display;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et_search_youtube = (EditText) findViewById(R.id.et_search_youtube);
        btn_display = (Button) findViewById(R.id.btn_display);
        int a = getBiersFromFile().length();
        if(!(a>0)){
            btn_display.setEnabled(false);
        }
//        ImageView imagetest = (ImageView) findViewById(R.id.imagetest);
//        new LoadImageFromURL("https://lh5.googleusercontent.com/-3cniePjY4sM/AAAAAAAAAAI/AAAAAAAAAAA/waj9exLokp8/photo.jpg", imagetest).execute();



//        try{
//            InputStream is = new FileInputStream(getCacheDir() + "/"  + "searches.json");
//            byte[] buffer = new byte[is.available()];
//            is.read(buffer);
//            is.close();
//            String file = new String(buffer, "UTF-8");
//            JSONObject js_object = new JSONObject(file);
//            JSONArray items = new JSONArray(js_object.getString("items"));
//            js_object = items.getJSONObject(0).getJSONObject("snippet");
//            System.out.println(js_object.getString("title"));
//        }catch(IOException e){
//            e.printStackTrace();
//        }catch(JSONException e){
//            e.printStackTrace();
//        }




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




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void startDownload(View view){
        Log.i(TAG, "download clicked");
        Toast.makeText(view.getContext(),R.string.downloading,Toast.LENGTH_LONG).show();
//        Intent intent = new Intent(this, DownloadJson.class);
//        startService(intent);
        DownloadJson.startActionBiers(this);
        return;
    }

    public void loadYoutubeData(View view){
        Log.i(TAG, "download clicked");
        if(!et_search_youtube.getText().toString().equals("")){
            Toast.makeText(view.getContext(),R.string.loading_yt_data,Toast.LENGTH_LONG).show();
            DownloadJson.startActionYoutube(this, et_search_youtube.getText().toString());
        }
        return;
    }

    public void displayJson(View view){
        Log.i(TAG, "display clicked");
        Toast.makeText(view.getContext(),R.string.display_beer_list,Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, ProfActivity.class);
        startActivity(intent);
        return;
    }

    public void translate(View view){
        String languageToLoad ="en";
        switch(view.getId()){
            case R.id.btn_english:
                languageToLoad  = "en"; // your language
                break;
            case R.id.btn_french:
                languageToLoad  = "fr"; // your language
                break;
        }
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
        Intent i = new Intent(this,MainActivity.class);
        finish();
        startActivity(i);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        String languageToLoad ="en";

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.english) {
            languageToLoad  = "en";
//            return true;
        }

        if (id == R.id.french) {
            languageToLoad  = "fr";
//            return true;
        }

        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
        Intent i = new Intent(this,MainActivity.class);
        finish();
        startActivity(i);

        return super.onOptionsItemSelected(item);
    }

    public JSONArray getBiersFromFile(){
        try{
            InputStream is = new FileInputStream(getCacheDir() + "/"  + "bieres.json");
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            is.close();
            return new JSONArray(new String(buffer, "UTF-8"));
        }catch(IOException e){
            e.printStackTrace();
            return new JSONArray();
        }catch(JSONException e){
            e.printStackTrace();
            return new JSONArray();
        }
    }

    public static final String BUTTON_DISPLAY = "com.esiea.button.update";

    public static class BtnDisplayUpdt extends BroadcastReceiver {
        public BtnDisplayUpdt() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
//            Log.d(BUTTON_DISPLAY, intent.getAction());
//            Toast.makeText(BierUpdate.this, "inside update biere class", Toast.LENGTH_SHORT).show();
            btn_display.setEnabled(true);
        }
    }

}

