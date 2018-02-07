package com.geniteam.SadqaApp.backgroundService;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.geniteam.SadqaApp.sms.AlarmManagerLocal;
import com.geniteam.SadqaApp.sms.DateUtils;
import com.geniteam.SadqaApp.sms.SadqaSmsManager;
import com.geniteam.SadqaApp.utils.AppConstants;
import com.geniteam.SadqaApp.utils.AppPref;
import com.geniteam.SadqaApp.utils.*;

import java.text.ParseException;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class SendSmsIntentService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_FOO = "com.geniteam.SadqaApp.backgroundService.action.FOO";
    private static final String ACTION_BAZ = "com.geniteam.SadqaApp.backgroundService.action.BAZ";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "com.geniteam.SadqaApp.backgroundService.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.geniteam.SadqaApp.backgroundService.extra.PARAM2";

    public SendSmsIntentService() {
        super("SendSmsIntentService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionFoo(Context context, String param1, String param2) {
        Intent intent = new Intent(context, SendSmsIntentService.class);
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
        Intent intent = new Intent(context, SendSmsIntentService.class);
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

            if (AppPref.getBooleanByKey(AppConstants.DAILY_SMS_key)) {
               // Toast.makeText(getApplicationContext()," daily sms sending.. ",Toast.LENGTH_LONG).show();
                Log.i("debug","daily");

                try {
                    if(com.geniteam.SadqaApp.utils.DateUtils.isTwoDatesAreEqual(
                            AppPref.getStringByKey(AppConstants.TO_DAY_DATE_key),
                           com.geniteam.SadqaApp.utils.DateUtils.getCurrentDate(getApplicationContext()) )){
                        Log.i("debug","dates are equal");
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                              Toast.makeText(getApplicationContext(),"Already sended sms ",Toast.LENGTH_LONG).show();
                            }
                        },20);

                    }else {
                        Log.i("deug","dates are not equal");
                        Toast.makeText(getApplicationContext(),"Sended sms ",Toast.LENGTH_LONG).show();

                       /// sendSms();
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }else  if (AppPref.getBooleanByKey(AppConstants.WEEKLY_SMS_key)){
              //  Toast.makeText(getApplicationContext()," weekly sms sending.. ",Toast.LENGTH_LONG).show();

                Log.i("debug","weekly");
            }else  if (AppPref.getBooleanByKey(AppConstants.BI_MONTHLY_key)){
               // Toast.makeText(getApplicationContext()," bi monthly sms sending.. ",Toast.LENGTH_LONG).show();
                Log.i("debug","bimonthly");

            }else  if (AppPref.getBooleanByKey(AppConstants.MONTHLYSMS_key)){

                //Toast.makeText(getApplicationContext()," monthly sms sending.. ",Toast.LENGTH_LONG).show();
                Log.i("debug","monthly");

            }
sendSms();

        }
    }

    public void sendSms(){
        try{
            new SadqaSmsManager().sendSadqaThroughSms(getApplicationContext(), AppPref.getIntegerByKey(AppConstants.NUMBER_OF_SMS_TO_SEND),
                    AppConstants.SADQA_SMS_NUMBER, "", null);
        }catch (Exception e){
            e.printStackTrace();
        }

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
