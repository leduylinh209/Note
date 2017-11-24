package com.example.leduylinh96.receiver;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.widget.Toast;

import com.example.leduylinh96.activity.ActivityShowNote;
import com.example.leduylinh96.activity.MainActivity;
import com.example.leduylinh96.MainScreen.R;
import com.example.leduylinh96.model.MyNote;
import java.util.ArrayList;

public class NoteBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context,"Notification created",Toast.LENGTH_SHORT).show();
        Bundle bundle = intent.getExtras();
        MyNote myNote = (MyNote) bundle.getSerializable("myNote");
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context);
        notificationBuilder.setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(myNote.getTitle())
                .setContentText(myNote.getNote()).setAutoCancel(true);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);
        Intent intent1 = new Intent(context, ActivityShowNote.class);
        ArrayList<MyNote> arrayList = new ArrayList<>();
        arrayList.add(myNote);
        Bundle bundle1 = new Bundle();
        bundle1.putSerializable("ARRAYLIST", arrayList);
        bundle1.putInt("POSITION", 0);
        intent1.putExtra("BUNDLE", bundle1);
        stackBuilder.addNextIntent(intent1);

        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        notificationBuilder.setContentIntent(resultPendingIntent);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(bundle.getInt("int"), notificationBuilder.build());
    }
}
