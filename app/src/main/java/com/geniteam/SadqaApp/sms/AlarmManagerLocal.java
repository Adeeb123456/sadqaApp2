package com.geniteam.SadqaApp.sms;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.geniteam.SadqaApp.BroadCastReceivers.AlarmReceiver;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static android.content.Context.ALARM_SERVICE;

/**
 * Created by 7CT on 2/7/2018.
 */

public class AlarmManagerLocal {

    public void setAlarm(Context context,long timeMili)
    {
        AlarmManager am =(AlarmManager)context.getSystemService(ALARM_SERVICE);
        Intent i = new Intent(context, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000 * 60 * 10, pi); // Millisec * Second * Minute
    }

    public void cancelAlarm(Context context,long timeMili)
    {
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        alarmManager.cancel(sender);
    }


   public static String getCurrentDate(Context context) {


        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());



        return currentDate;

    }


   public static void scheduleAlarmWeekly(Context context ,int dayOfWeek) {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, dayOfWeek);

        // Check we aren't setting it in the past which would trigger it to fire instantly
        if(calendar.getTimeInMillis() < System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 7);
        }

        // Set this to whatever you were planning to do at the given time
        PendingIntent yourIntent;
        Intent i = new Intent(context, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY * 7, pi);
    }

public static void scheduleAlarmBiMonthly(Context context,int dayOfmonth){
    Calendar calendar = Calendar.getInstance();
    calendar.set(Calendar.DAY_OF_MONTH, dayOfmonth);

    // Check we aren't setting it in the past which would trigger it to fire instantly
    if(calendar.getTimeInMillis() < System.currentTimeMillis()) {
        calendar.add(Calendar.DAY_OF_YEAR, 14);
    }

    // Set this to whatever you were planning to do at the given time
    PendingIntent yourIntent;
    Intent i = new Intent(context, AlarmReceiver.class);
    PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);
    AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY * 14, pi);
}

    public static void scheduleAlarmMonthly(Context context,int dayOfmonth){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, dayOfmonth);

        // Check we aren't setting it in the past which would trigger it to fire instantly
        if(calendar.getTimeInMillis() < System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 28);
        }

        // Set this to whatever you were planning to do at the given time
        PendingIntent yourIntent;
        Intent i = new Intent(context, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY * dayOfmonth, pi);
    }



    public static  void setAlarmDaily(Context context){
        PendingIntent pendingIntent;
        AlarmManager alarmManager;
        Intent alarmIntent=new Intent(context,AlarmReceiver.class);
        pendingIntent=PendingIntent.getBroadcast(context,0,alarmIntent,0);
       // cancelAlarm2();
        alarmManager=(AlarmManager)context.getSystemService(ALARM_SERVICE);
        long interval = 0;
        TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,System.currentTimeMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);
        Toast.makeText(context, "Alarm Set", Toast.LENGTH_SHORT).show();

    }





    public static void cancelAlarm2(Context context){
        PendingIntent pendingIntent;
        AlarmManager alarmManager;
        Intent alarmIntent=new Intent(context,AlarmReceiver.class);
        pendingIntent=PendingIntent.getBroadcast(context,0,alarmIntent,0);
        pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);
        alarmManager= (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
        Toast.makeText(context, "Alarm Canceled", Toast.LENGTH_SHORT).show();
    }


    public void daysFromDates(){
        // Creates two calendars instances
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();

        // Set the date for both of the calendar instance
        cal1.set(2006, Calendar.DECEMBER, 30);
        cal2.set(2007, Calendar.MAY, 3);

        // Get the represented date in milliseconds
        long millis1 = cal1.getTimeInMillis();
        long millis2 = cal2.getTimeInMillis();

        // Calculate difference in milliseconds
        long diff = millis2 - millis1;

        // Calculate difference in seconds
        long diffSeconds = diff / 1000;

        // Calculate difference in minutes
        long diffMinutes = diff / (60 * 1000);

        // Calculate difference in hours
        long diffHours = diff / (60 * 60 * 1000);

        // Calculate difference in days
        long diffDays = diff / (24 * 60 * 60 * 1000);

        System.out.println("In milliseconds: " + diff + " milliseconds.");
        System.out.println("In seconds: " + diffSeconds + " seconds.");
        System.out.println("In minutes: " + diffMinutes + " minutes.");
        System.out.println("In hours: " + diffHours + " hours.");
        System.out.println("In days: " + diffDays + " days.");
    }
}
