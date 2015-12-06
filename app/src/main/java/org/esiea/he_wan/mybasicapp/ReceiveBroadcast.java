package org.esiea.he_wan.mybasicapp;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

public class ReceiveBroadcast extends BroadcastReceiver {

    NotificationCompat.Builder notification;
    private static final int uniqueID = 13132;

    public ReceiveBroadcast() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context,"Download finished !", Toast.LENGTH_LONG).show();
        notification =  new NotificationCompat.Builder(context);
        notification.setAutoCancel(true);
        notification.setSmallIcon(R.drawable.ic_launcher);
        notification.setTicker(context.getString(R.string.finished_downloading));
        notification.setWhen(System.currentTimeMillis());
        notification.setContentTitle(context.getString(R.string.json_downloaded));
        notification.setContentText(context.getString(R.string.view_json_file));


        Intent i = new Intent(context, RVJsonFile.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
        notification.setContentIntent(pendingIntent);

        //issue it
        NotificationManager nm = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        nm.notify(uniqueID,notification.build());

    }
}
