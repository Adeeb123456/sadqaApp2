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
import com.geniteam.SadqaApp.utils.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

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
            if(AppPref.getBooleanByKey(AppConstants.DAILY_SMS_key)){
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
            }

            else if(AppPref.getBooleanByKey(AppConstants.BI_MONTHLY_key)){
                if(com.geniteam.SadqaApp.utils.DateUtils.isTwoDatesAreEqual(
                        AppPref.getStringByKey(AppConstants.FUTURE_DATE_key),
                        com.geniteam.SadqaApp.utils.DateUtils.getCurrentDate(context) )){
                    Log.i("debug","dates are equal");
                    Toast.makeText(context,"sms is sending... ",Toast.LENGTH_LONG).show();

                    SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                    Date lastDate ;
                    Date futureDate = null;
                    Calendar lastDateCalndr = null,futureDateCalndr = null;

addDaysToLatDate(14,AppPref.getStringByKey(AppConstants.LAST_DATE_key));
addDaysToFutureDate(14,AppPref.getStringByKey(AppConstants.LAST_DATE_key));
                    //  futureDate=simpleDateFormat.parse()

                  //  Log.i("debug","future date "+futureDateStr);



                }else {
                    Log.i("deug","dates are not equal");
                    Toast.makeText(context,"sms is sechdule bi monthly ",Toast.LENGTH_LONG).show();

                    //  sendSms();
                }
            }
            } catch (ParseException e1) {
            e1.printStackTrace();
        }



     //   Intent intent1=new Intent(context, SendSmsIntentService.class);
      // context.startService(intent1);


    }

    public void addDaysToLatDate(int days,String dateStr){
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        Date futureDate;
        Calendar calendarFuture;
        SimpleDateFormat sdf;
        String dateInString;
        try {
            futureDate=   simpleDateFormat.parse(dateStr);
            calendarFuture=Calendar.getInstance();
            calendarFuture.setTime(futureDate);
            //  calendarFuture.
            calendarFuture.add(Calendar.DAY_OF_MONTH,days);
            sdf = new SimpleDateFormat("dd-MM-yyyy");
            Date resultdateFuture = new Date(calendarFuture.getTimeInMillis());
            dateInString = sdf.format(resultdateFuture);
            System.out.println("String date:"+dateInString);
            //  int day=calendarFuture.get(Calendar.DAY_OF_MONTH);
            /////  int month=calendarFuture.get(Calendar.MONTH);
            // int year=calendarFuture.get(Calendar.YEAR);
            // String futureDateStr=day+"-"+month+"-"+year;
            Log.d("debug","future date 1st time "+dateInString);
            Log.d("debug","current date "+ DateUtils.getCurrentDate(context));

            AppPref.putValueByKey(AppConstants.LAST_DATE_key,dateInString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
    public void addDaysToFutureDate(int days,String dateStr){
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        Date futureDate;
        Calendar calendarFuture;
        SimpleDateFormat sdf;
        String dateInString;
        try {
            futureDate=   simpleDateFormat.parse(dateStr);
            calendarFuture=Calendar.getInstance();
            calendarFuture.setTime(futureDate);
            //  calendarFuture.
            calendarFuture.add(Calendar.DAY_OF_MONTH,days);
            sdf = new SimpleDateFormat("dd-MM-yyyy");
            Date resultdateFuture = new Date(calendarFuture.getTimeInMillis());
            dateInString = sdf.format(resultdateFuture);
            System.out.println("String date:"+dateInString);
            //  int day=calendarFuture.get(Calendar.DAY_OF_MONTH);
            /////  int month=calendarFuture.get(Calendar.MONTH);
            // int year=calendarFuture.get(Calendar.YEAR);
            // String futureDateStr=day+"-"+month+"-"+year;
            Log.d("debug","future date 1st time "+dateInString);
            Log.d("debug","current date "+ DateUtils.getCurrentDate(context));

            AppPref.putValueByKey(AppConstants.FUTURE_DATE_key,dateInString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }



    public void sendSmsInService(){
        Intent intent1=new Intent(context, SendSmsIntentService.class);
        context.startService(intent1);

    }
}
