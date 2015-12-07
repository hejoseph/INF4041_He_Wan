package org.esiea.he_wan.mybasicapp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class RVJsonFile extends AppCompatActivity {

    private static final String TAG = "org.esiea.RVJsonFile";
    private JSONObject biere;

    public JSONArray getDataFromJson(){
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            JSONObject tab = getDataFromJson().getJSONObject(0);
            Log.i(TAG, tab.getString("id"));
        } catch (JSONException e ) {
            e.printStackTrace();
        }
        setContentView(R.layout.activity_rvjson_file);
        RecyclerView rv = (RecyclerView) findViewById(R.id.rv_biere);
        rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false));

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

    public void readFromJSON() {
        // Read root from JSON file
        try {
            BufferedReader bRead = new BufferedReader(new InputStreamReader(
                    openFileInput("bieres.json")));
            biere = new JSONObject(bRead.readLine());

        } catch (FileNotFoundException e) {
            e.printStackTrace();

            biere = new JSONObject();

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

//                   // Read settings, or put JSON object in root if it doesn't exist
//            try {
//                settings = root.getJSONObject("settings");
//
//        } catch (JSONException e) {
//            try {
//                settings = new JSONObject();
//                root.put("settings", settings);
//
//            } catch (JSONException e1) {
//                e1.printStackTrace();
//            }
//        }
//
//        // Read settings, or put if they don't exist
//        try {
//            Boolean lockscreen = settings.getBoolean("lockscreen");
//            String pin = settings.getString("pin");
//            String tag = settings.getString("tag");
//        } catch (JSONException e) {
//            try {
//
//                settings.put("lockscreen", false);
//                settings.put("pin", "");
//                settings.put("tag", "");
//            } catch (JSONException e1) {
//                e1.printStackTrace();
//            }
//        }

    }

}
