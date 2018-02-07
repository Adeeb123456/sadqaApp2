package com.geniteam.SadqaApp.sms;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.geniteam.SadqaApp.BroadCastReceivers.AlarmReceiver;

/**
 * Created by 7CT on 2/7/2018.
 */

public class AlarmManager {

    public void setAlarm(Context context, long timeMili)
    {
        android.app.AlarmManager am =(android.app.AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);
        am.setRepeating(android.app.AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000 * 60 * 10, pi); // Millisec * Second * Minute
    }

    public void cancelAlarm(Context context,long timeMili)
    {
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        android.app.AlarmManager alarmManager = (android.app.AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }
}
