package com.geniteam.SadqaApp.BroadCastReceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.geniteam.SadqaApp.backgroundService.SendSmsIntentService;
import com.geniteam.SadqaApp.sms.SadqaSmsManager;
import com.geniteam.SadqaApp.utils.AppConstants;
import com.geniteam.SadqaApp.utils.AppPref;

import java.text.ParseException;

/**
 * Created by 7CT on 2/6/2018.
 */

public class AlarmReceiver extends BroadcastReceiver {
    Context context;
    @Override
    public void onReceive(Context context, Intent intent) {
        this.context=context;
        Log.d("debug","alaram triggered");
      //  Toast.makeText(context,"sms sending reeiver ",Toast.LENGTH_LONG).show();
        Toast.makeText(context,"alrm recever ",Toast.LENGTH_LONG).show();

        try {
            if(com.geniteam.SadqaApp.utils.DateUtils.isTwoDatesAreEqual(
                    AppPref.getStringByKey(AppConstants.TO_DAY_DATE_key),
                    com.geniteam.SadqaApp.utils.DateUtils.getCurrentDate(context) )){
                Log.i("debug","dates are equal");
    if(AppConstants.isSendSms){
        sendSmsInService();
          }else {
          Toast.makeText(context,"Already sended sms ",Toast.LENGTH_LONG).show();
           }



            }else {
                Log.i("deug","dates are not equal");
                Toast.makeText(context,"Sended sms ",Toast.LENGTH_LONG).show();

             //  sendSms();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

     //   Intent intent1=new Intent(context, SendSmsIntentService.class);
      // context.startService(intent1);


    }
    public void sendSmsInService(){
        Intent intent1=new Intent(context, SendSmsIntentService.class);
        context.startService(intent1);

    }
}
