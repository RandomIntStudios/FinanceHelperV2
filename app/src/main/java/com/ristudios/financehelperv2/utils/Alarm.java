package com.ristudios.financehelperv2.utils;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.ristudios.financehelperv2.R;
import com.ristudios.financehelperv2.ui.activities.MonthlyOverviewActivity;

import java.time.ZonedDateTime;

public class Alarm extends BroadcastReceiver {


    public Alarm(){

    }

    public void setBudgetResetForNextMonth(Context context){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, Alarm.class);
        intent.setAction(Utils.INTENT_ACTION_BUDGET_RESET);
        PendingIntent sender = PendingIntent.getBroadcast(context, Utils.REQUEST_CODE_BUDGET_RESET, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        long triggerTime = Utils.getTimeForAlarm();
        alarmManager.set(AlarmManager.RTC_WAKEUP, triggerTime, sender);

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Utils.INTENT_ACTION_BUDGET_RESET)) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            prefs.edit().putFloat(Utils.PREFS_CURRENT_BUDGET_KEY, prefs.getFloat(Utils.PREFS_MAXIMUM_BUDGET_KEY, 200)).apply();
            setBudgetResetForNextMonth(context);
            NotificationHelper notificationHelper = new NotificationHelper(context);
            Intent sender = new Intent(context, MonthlyOverviewActivity.class);
            int month = 0;
            int year = 0;
            if (Utils.getCurrentZonedTime().getMonthValue() == 1){
                month = 12;
                year = Utils.getCurrentZonedTime().getYear() - 1;
            }
            else{
                month = Utils.getCurrentZonedTime().getMonthValue() - 1;
                year = Utils.getCurrentZonedTime().getYear();
            }
            sender.putExtra(Utils.INTENT_KEY_MONTH, month);
            sender.putExtra(Utils.INTENT_KEY_YEAR, year);
            PendingIntent onNotificationClick = notificationHelper.createContentIntent(sender, Utils.REQUEST_CODE_NOTIFICATION_ONCLICK);
            Notification notification = notificationHelper.createNotification(context.getString(R.string.reset_notification_title),
                    context.getString(R.string.reset_notification_message),
                    R.drawable.currency_sign,
                    true,
                    onNotificationClick);
            notificationHelper.showNotification(Utils.ID_NOTIFICATION_RESET, notification);
        }
        else if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED) || intent.getAction().equals(Intent.ACTION_LOCKED_BOOT_COMPLETED)){
            setBudgetResetForNextMonth(context);
        }
    }
}
