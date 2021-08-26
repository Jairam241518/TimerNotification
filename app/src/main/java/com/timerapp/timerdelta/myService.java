package com.timerapp.timerdelta;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import java.util.Timer;
import java.util.TimerTask;

public class myService extends Service {

    private static final String CHANNEL_ID = "NotificationChannelId";
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {





        final Integer[] timeRemaining = {intent.getIntExtra("TimerValue", 0)};
        final Integer startTime = timeRemaining[0];
        final Timer[] timer = {new Timer()};
        final Integer[] timeRemainingPercent = {100};
        timer[0].scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {


                Intent intent1local = new Intent();
                intent1local.setAction("Counter");


                timeRemaining[0]--;
                notificationUpdate(timeRemaining[0]);

                timeRemainingPercent[0] = (timeRemaining[0]*100)/startTime;
                if (timeRemaining[0] <= 0){
                    timer[0].cancel();
                    // i wrote this code for clearing notifications
                    //NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
                    //notificationManager.cancel(Integer.parseInt(CHANNEL_ID));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        stopForeground(Service.STOP_FOREGROUND_REMOVE);
                    }

                }

                intent1local.putExtra("TimeRemaining", timeRemaining[0]);
                intent1local.putExtra("remainingPercent", timeRemainingPercent[0]);
                sendBroadcast(intent1local);

            }
        }, 0, 1000);

        return super.onStartCommand(intent, flags, startId);


    }

    private void notificationUpdate(Integer timeLeft){

        Intent notification = new Intent(this, MainActivity.class);
        final PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notification, 0);
        final Notification[] notifications = {new NotificationCompat.Builder(this, CHANNEL_ID)
        .setContentTitle("Timer")
        .setContentText("TimeRemaining: " + timeLeft)
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setContentIntent(pendingIntent)
        .setPriority(NotificationCompat.PRIORITY_LOW)
        .setAutoCancel(true)
        .build()};

        startForeground(1, notifications[0]);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, "My counter service", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);

        }
    }
}
