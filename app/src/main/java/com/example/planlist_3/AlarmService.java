package com.example.planlist_3;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

public class AlarmService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Foreground에서 실행 > Notification 보여줘야
        // Oreo 버전(26) 이후부터 channel 필요
        String channelId=createNotificationChannel();

        NotificationCompat.Builder builder=new NotificationCompat.Builder(this, channelId);
        Notification notification=builder.setOngoing(true).setCategory(Notification.CATEGORY_SERVICE).build();

        startForeground(1, notification);
        stopForeground(true);

        stopSelf();
        return START_NOT_STICKY;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String createNotificationChannel() {
        String channelId="Alarm";
        String channelName=getString(R.string.app_name);
        NotificationChannel channel=new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_NONE);
        channel.setDescription(channelName);
        channel.setSound(null, null);
        channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager manager=(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.createNotificationChannel(channel);

        return channelId;
    }
}
