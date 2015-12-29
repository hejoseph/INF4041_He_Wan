package org.esiea.he_wan.mybasicapp;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Button;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class DownloadJson extends IntentService {

    private static final String TAG_BIERES = "intentService.bieres";
    private static final String TAG_SEARCHES = "intentService.searches";

    private static String query_string = "";

    public DownloadJson() {
        super("DownloadJsonIntentService");
    }

    public static void startActionBiers(Context context){
        Intent intent = new Intent(context, DownloadJson.class);
        intent.setAction(TAG_BIERES);
        context.startService(intent);
    }

    public static void startActionYoutube(Context context, String q){
        query_string = q;
        Intent intent = new Intent(context, DownloadJson.class);
        intent.setAction(TAG_SEARCHES);
        context.startService(intent);
    }

    private void handleActionSearch(){
        Log.i(TAG_SEARCHES, "the search has now started");
        URL url = null;
        try{
            url = new URL("https://www.googleapis.com/youtube/v3/search?part=snippet&key=AIzaSyAOc88Y218-6cRf5Z-5nK0mNV6ji0qKwvQ&maxResults=20&q="+query_string);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            if(HttpURLConnection.HTTP_OK == conn.getResponseCode()){
                copyInputStreamToFile(conn.getInputStream(), new File(getCacheDir(), "searches.json"));
                Log.d(TAG_SEARCHES, "search json file created !");
                Intent i = new Intent();
                i.setAction("com.esiea.he_wan.sendbroadcast.searches");
                i.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
                sendBroadcast(i);
            }
        } catch(MalformedURLException e) {
            e.printStackTrace();
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    private void handleActionBieres(){
        Log.i(TAG_BIERES, "the service has now started");
        URL url = null;
        try{
            url = new URL("http://binouze.fabrigli.fr/bieres.json");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            if(HttpURLConnection.HTTP_OK == conn.getResponseCode()){
                copyInputStreamToFile(conn.getInputStream(), new File(getCacheDir(), "bieres.json"));
                Log.i(TAG_BIERES, "Bieres json downloaded !");
                Intent i = new Intent();
                i.setAction("com.esiea.he_wan.sendbroadcast.bieres");
                i.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
                sendBroadcast(i);

                Intent i2 = new Intent();
                i2.setAction("com.esiea.button.update");
                i2.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
                sendBroadcast(i2);

                Log.d("hejoseph", "second broadcast");
            }
        } catch(MalformedURLException e) {
            e.printStackTrace();
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (TAG_BIERES.equals(action)) {
                handleActionBieres();
//                LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(ProfActivity.BIERS_UPDATE));
            }

            if (TAG_SEARCHES.equals(action)) {
                handleActionSearch();
            }

        }

    }

    private void copyInputStreamToFile(InputStream in, File file){
        try{
            OutputStream out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while((len=in.read(buf))>0){
                out.write(buf,0,len);
            }
            out.close();
            in.close();
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
