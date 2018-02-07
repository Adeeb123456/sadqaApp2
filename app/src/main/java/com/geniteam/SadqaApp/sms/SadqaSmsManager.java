package com.geniteam.SadqaApp.sms;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.telephony.SmsManager;

import com.geniteam.SadqaApp.utils.AppConstants;

/**
 * Created by 7CT on 1/26/2018.
 */

public class SadqaSmsManager {
    String  smsResponseMessage;
    int countSendSms=0;
    boolean isAllSmsSended;
    boolean isSendSms;

    public interface SadqaSmsResponse{
        public void onSadqaSmsResponse(String statusMessage);
    }
    public void sendSadqaThroughSms(Context context, final int numberOfsmsToSend, String sendingNumber,
                                    String smsBody, final SadqaSmsResponse sadqaSmsResponse){
        countSendSms=0;
        PendingIntent pendingIntent=PendingIntent.getBroadcast(context,0,new Intent("SMS_SENT"),0);
      context.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:

                        smsResponseMessage= AppConstants.SADQA_SMS_SENDED_SUCCESS;
                        countSendSms++;
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        isSendSms=false;
                        smsResponseMessage=AppConstants.SADQA_SMS_FAIL;
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        isSendSms=false;
                        smsResponseMessage=AppConstants.SADQA_SMS_FAIL_NO_MOBILE_SERVICE;
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        isSendSms=false;
                        smsResponseMessage=AppConstants.SADQA_SMS_FAIL_NO_MOBILE_SERVICE;
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        isSendSms=false;
                        smsResponseMessage=AppConstants.SADQA_SMS_FAIL;

                        break;
                }
           if(sadqaSmsResponse!=null){
                  sadqaSmsResponse.onSadqaSmsResponse(smsResponseMessage);
                   }



                if(countSendSms==numberOfsmsToSend){
                 if(sadqaSmsResponse!=null){
                     sadqaSmsResponse.onSadqaSmsResponse(smsResponseMessage);
                 }
                }

            }
        },new IntentFilter("SMS_SENT"));




SmsManager smsManager=SmsManager.getDefault();
      try{
for(int i=0;i<numberOfsmsToSend;i++){

    smsManager.sendTextMessage(sendingNumber,null,smsBody,pendingIntent,null);
}



      }catch (Exception e){
          e.printStackTrace();
      }





    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    public  void sendSmsThroughSelectedSim(
            Context context, int subscriptionId, String destinationAddress,
            final int numberOfSmsToSend, String text, final SadqaSmsResponse sadqaSmsResponse) {


        countSendSms=0;

       PendingIntent pendingIntentSmsSend=PendingIntent.getBroadcast(context,0,new Intent("SMS_SENT"),0);
       context.registerReceiver(new BroadcastReceiver() {
          @Override
          public void onReceive(Context context, Intent intent) {
              switch (getResultCode()) {
                  case Activity.RESULT_OK:
                      smsResponseMessage=AppConstants.SADQA_SMS_SENDED_SUCCESS;
                      countSendSms++;

                      break;
                  case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                      smsResponseMessage=AppConstants.RESULT_ERROR_GENERIC_FAILURE;

                      break;
                  case SmsManager.RESULT_ERROR_NO_SERVICE:
                      smsResponseMessage=AppConstants.SADQA_SMS_FAIL_NO_MOBILE_SERVICE;
                      break;
                  case SmsManager.RESULT_ERROR_NULL_PDU:
                      smsResponseMessage=AppConstants.RESULT_ERROR_NULL_PDU;

                      break;
                  case SmsManager.RESULT_ERROR_RADIO_OFF:
                      smsResponseMessage=AppConstants.RESULT_ERROR_RADIO_OFF;

                      break;
              }

              if(countSendSms==numberOfSmsToSend){
                  if(sadqaSmsResponse!=null)
                      sadqaSmsResponse.onSadqaSmsResponse(AppConstants.SADQA_SMS_SENDED_SUCCESS);
              }
          }
      },new IntentFilter("SMS_SENT"));

        for(int i=0;i<numberOfSmsToSend;i++){
            SmsManager.getSmsManagerForSubscriptionId(subscriptionId).sendTextMessage( destinationAddress, null,  text,pendingIntentSmsSend, null);

        }

    }





}
