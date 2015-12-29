package org.esiea.he_wan.mybasicapp;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

public class ReceiveBroadcast extends BroadcastReceiver {

    NotificationCompat.Builder notification;
    private static final int uniqueID = 13132;
    private static final int uniqueID2 = 13133;

    private static final String TAG_BIERES = "com.esiea.he_wan.sendbroadcast.bieres";
    private static final String TAG_SEARCHES = "com.esiea.he_wan.sendbroadcast.searches";

    public ReceiveBroadcast() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("hejoseph","onReceive");
        final String action = intent.getAction();
        if (TAG_BIERES.equals(action)) {
            Log.d("hejoseph", intent.getAction());
            handleActionBieres(context);
        }
        if (TAG_SEARCHES.equals(action)) {
            Log.d("hejoseph", intent.getAction());
            handleActionSearches(context);
        }


    }

    public void handleActionSearches(Context context){
        Log.d("hejoseph","handleActionSearches");
        Toast.makeText(context,R.string.dl_search_finished, Toast.LENGTH_LONG).show();
        notification =  new NotificationCompat.Builder(context);
        notification.setAutoCancel(true);
        notification.setSmallIcon(R.drawable.youtube);
        notification.setTicker(context.getString(R.string.yt_data_ready));
        notification.setWhen(System.currentTimeMillis());
        notification.setContentTitle(context.getString(R.string.yt_searches));
        notification.setContentText(context.getString(R.string.view_yt_data));


        Intent i = new Intent(context, SearchesActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
        notification.setContentIntent(pendingIntent);

        //issue it
        NotificationManager nm = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        nm.notify(uniqueID2,notification.build());
    }

    public void handleActionBieres(Context context){

        Toast.makeText(context,R.string.dl_beer_finished, Toast.LENGTH_LONG).show();
        notification =  new NotificationCompat.Builder(context);
        notification.setAutoCancel(true);
        notification.setSmallIcon(R.drawable.ic_launcher);
        notification.setTicker(context.getString(R.string.beer_list_ready));
        notification.setWhen(System.currentTimeMillis());
        notification.setContentTitle(context.getString(R.string.beer_list));
        notification.setContentText(context.getString(R.string.view_beer));


        Intent i = new Intent(context, ProfActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
        notification.setContentIntent(pendingIntent);

        //issue it
        NotificationManager nm = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        nm.notify(uniqueID,notification.build());
    }
}
