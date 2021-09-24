package com.ristudios.financehelperv2.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class NotificationHelper {
    private Context context;

    public static final String MAIN_NOTIFICATION_CHANNEL = "main_notification_channel";

    public NotificationHelper(Context context){
        this.context = context;
    }

    public Notification createNotification(String title, String message, int icon, boolean autoCancel, @Nullable PendingIntent pendingIntent){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, MAIN_NOTIFICATION_CHANNEL)
                .setSmallIcon(icon)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        if (autoCancel){
            builder.setAutoCancel(true);
        }
        if (pendingIntent != null){
            builder.setContentIntent(pendingIntent);
        }
        return builder.build();
    }

    public PendingIntent createContentIntent(Intent intent, int requestCode){
        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);
        taskStackBuilder.addNextIntentWithParentStack(intent);
        return taskStackBuilder.getPendingIntent(requestCode, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public void showNotification(int id, Notification notification){
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(id, notification);
    }

    public void createNotificationChannel(Context context, String name, String description, String id)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(id, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}
