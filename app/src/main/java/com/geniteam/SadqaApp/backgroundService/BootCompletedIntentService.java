package com.geniteam.SadqaApp.backgroundService;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.Context;
import android.icu.util.Calendar;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class BootCompletedIntentService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_FOO = "com.geniteam.SadqaApp.backgroundService.action.FOO";
    private static final String ACTION_BAZ = "com.geniteam.SadqaApp.backgroundService.action.BAZ";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "com.geniteam.SadqaApp.backgroundService.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.geniteam.SadqaApp.backgroundService.extra.PARAM2";

    public BootCompletedIntentService() {
        super("BootCompletedIntentService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionFoo(Context context, String param1, String param2) {
        Intent intent = new Intent(context, BootCompletedIntentService.class);
        intent.setAction(ACTION_FOO);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionBaz(Context context, String param1, String param2) {
        Intent intent = new Intent(context, BootCompletedIntentService.class);
        intent.setAction(ACTION_BAZ);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_FOO.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionFoo(param1, param2);
            } else if (ACTION_BAZ.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionBaz(param1, param2);
            }




        }
    }

    public void alarmTimes() {
        //Intent myIntent = new Intent(SMS.this, com.pakdata.fakesms.NotifyService.class);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
      //PendingIntent pendingIntent = PendingIntent.getService(SMS.this, 0, myIntent, 0);
        java.util.Calendar calendar = java.util.Calendar.getInstance();

      Date str=  calendar.getTime();

        calendar.set(java.util.Calendar.HOUR_OF_DAY, 22);
        calendar.set(java.util.Calendar.MINUTE, 22);
        calendar.set(java.util.Calendar.SECOND, 00);
     //   alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 60 * 60, pendingIntent);  //set repeating every 24 hours
    }



public String getFutureDateStr(){
    String futuredate="7/2/2018";
    return futuredate;
}

public String getLastDateStr(){
String lastdate="7/2/2018";
    return lastdate;
}

    public static String getCurrentDateStr(Context context) {


        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());


        return currentDate;

    }


    private void getCurrentDateOld() {
        int day,month,year;
        GregorianCalendar cal = new GregorianCalendar();
        cal.add(java.util.Calendar.MONTH, 1);
        Log.d("debug",cal.get(java.util.Calendar.DAY_OF_MONTH) + "-" + cal.getDisplayName(java.util.Calendar.MONTH, java.util.Calendar.LONG, Locale.US) + "-" + cal.get(java.util.Calendar.YEAR));
        day = cal.get(java.util.Calendar.DAY_OF_MONTH);
        month = cal.get(java.util.Calendar.MONTH);
        year = cal.get(java.util.Calendar.YEAR);


    }















    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFoo(String param1, String param2) {
        // TODO: Handle action Foo
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionBaz(String param1, String param2) {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
