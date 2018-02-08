package com.geniteam.SadqaApp.frags;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.geniteam.SadqaApp.BroadCastReceivers.AlarmReceiver;
import com.geniteam.SadqaApp.R;
import com.geniteam.SadqaApp.base.BaseFragment;
import com.geniteam.SadqaApp.databinding.ChooseSmsFrequencyBinding;
import com.geniteam.SadqaApp.sms.AlarmManagerLocal;
import com.geniteam.SadqaApp.sms.SadqaSmsManager;
import com.geniteam.SadqaApp.utils.AppConst;
import com.geniteam.SadqaApp.utils.AppConstants;
import com.geniteam.SadqaApp.utils.AppPref;
import com.geniteam.SadqaApp.utils.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static android.content.Context.ALARM_SERVICE;

/**
 * Created by 7CT on 2/6/2018.
 */

public class ChoseSmsFrequency extends BaseFragment implements View.OnClickListener{

    ChooseSmsFrequencyBinding binding;
    PendingIntent  pendingIntent;
    AlarmManager alarmManager;
    long interval;


    @Override
    public String getTagText() {
        return null;
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

      if(binding==null){
          binding= DataBindingUtil.inflate(inflater, R.layout.choose_sms_frequency,container,false);

          binding.daily.setOnClickListener(this);
          binding.weekly.setOnClickListener(this);
          binding.biMonthly.setOnClickListener(this);
          binding.monthly.setOnClickListener(this);
          binding.setIsbiSelected(false);
          binding.setIsdailySelected(false);
          binding.setIsmonthlySelected(false);
          binding.setIsweeklySelected(false);

      }

        return binding.getRoot();
    }

    public void resetPrefValuesFrequencySms(){
        AppPref.putValueByKey(AppConstants.DAILY_SMS_key,false);
        AppPref.putValueByKey(AppConstants.WEEKLY_SMS_key,false);
        AppPref.putValueByKey(AppConstants.BI_MONTHLY_key,false);
        AppPref.putValueByKey(AppConstants.MONTHLYSMS_key,false);
    }
    public void setAllSelectedIconsFalse(View view){
        binding.setIsbiSelected(false);
        binding.setIsdailySelected(false);
        binding.setIsmonthlySelected(false);
        binding.setIsweeklySelected(false);
    }

    public void setSelectedIconTrue(View view){
       if(view==binding.daily){
           binding.setIsdailySelected(true);
       }else if(view==binding.weekly){
           binding.setIsweeklySelected(true);
       }else if(view==binding.biMonthly){
           binding.setIsbiSelected(true);
       }else if(view==binding.monthly){
           binding.setIsmonthlySelected(true);
       }
       binding.executePendingBindings();
    }

    public void checkedUnChecked(final View view){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setAllSelectedIconsFalse(view);
                setSelectedIconTrue(view);
            }
        },20);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        try {
            Intent alarmIntent=new Intent(getContext(),AlarmReceiver.class);
            pendingIntent=PendingIntent.getBroadcast(getContext(),0,alarmIntent,0);
        } catch (Exception e) {
  e.printStackTrace();
        }
    }

    @Override
    public void onClick(final View view) {
        if (view.getId() == binding.daily.getId()) {
            AppConstants.isSendSms=true;
            checkedUnChecked(binding.daily);
             resetPrefValuesFrequencySms();
            AppPref.putValueByKey(AppConstants.DAILY_SMS_key,true);
            AppPref.putValueByKey(AppConstants.TO_DAY_DATE_key,DateUtils.getCurrentDate(getContext()));
            AlarmManagerLocal.cancelAlarm2(getContext());
            AlarmManagerLocal.setAlarmDaily(getContext());


        }else if(view.getId()==binding.weekly.getId()){
            checkedUnChecked(binding.weekly);
            interval= TimeUnit.MILLISECONDS.convert(7, TimeUnit.DAYS);
            Log.d("debug","day "+DateUtils.getDayOFWeek());

            resetPrefValuesFrequencySms();
            AppPref.putValueByKey(AppConstants.WEEKLY_SMS_key,true);
            AppPref.putValueByKey(AppConstants.TO_DAY_DATE_key,DateUtils.getCurrentDate(getContext()));
            AppPref.putValueByKey(AppConstants.DAY_OF_WEEK_key,DateUtils.getDayOFWeek());
            int dayOfweek= AppPref.getIntegerByKey(AppConstants.DAY_OF_WEEK_key);
            Log.d("debug","day "+dayOfweek);
            AlarmManagerLocal.cancelAlarm2(getContext());
           AlarmManagerLocal.scheduleAlarmWeekly(getContext(),dayOfweek);

        }else if(view.getId()==binding.biMonthly.getId()){
            checkedUnChecked(binding.biMonthly);
            Log.d("debug","day "+DateUtils.getDayOFWeek());
            resetPrefValuesFrequencySms();
            AppPref.putValueByKey(AppConstants.BI_MONTHLY_key,true);
            AppPref.putValueByKey(AppConstants.DAY_OF_MONTH_key,DateUtils.getDayOFMonth());
            AppPref.putValueByKey(AppConstants.LAST_DATE_key,DateUtils.getCurrentDate(getContext()));
            addDaysToCurrentDate(14,DateUtils.getCurrentDate(getContext()));
            final int dayOfweek= AppPref.getIntegerByKey(AppConstants.DAY_OF_MONTH_key);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    AlarmManagerLocal.cancelAlarm2(getContext());

                    AlarmManagerLocal.scheduleAlarmBiMonthly(getContext(),dayOfweek);

                }
            },500);

        }else if(view.getId()==binding.monthly.getId()){
            checkedUnChecked(binding.monthly);
            Log.d("debug","current date "+DateUtils.getCurrentDate(getContext()));
            resetPrefValuesFrequencySms();
            AppPref.putValueByKey(AppConstants.MONTHLYSMS_key,true);
            AppPref.putValueByKey(AppConstants.DAY_OF_MONTH_key,DateUtils.getDayOFMonth());
            AppPref.putValueByKey(AppConstants.LAST_DATE_key,DateUtils.getCurrentDate(getContext()));
            addDaysToCurrentDate(14,DateUtils.getCurrentDate(getContext()));
            int dayOfMonth= AppPref.getIntegerByKey(AppConstants.DAY_OF_MONTH_key);
            AlarmManagerLocal.cancelAlarm2(getContext());
            AlarmManagerLocal.scheduleAlarmMonthly(getContext(),dayOfMonth);

        }
    }

    public void addDaysToCurrentDate(int days,String dateStr){
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
            Log.d("debug","current date "+DateUtils.getCurrentDate(getContext()));

            AppPref.putValueByKey(AppConstants.FUTURE_DATE_key,dateInString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

public void sendSms(){
    try{
        new SadqaSmsManager().sendSadqaThroughSms(getContext(), AppPref.getIntegerByKey(AppConstants.NUMBER_OF_SMS_TO_SEND),
                AppConstants.SADQA_SMS_NUMBER, "", null);
    }catch (Exception e){
      e.printStackTrace();
    }

}
    public void setAlarm(){
        try{
            cancelAlarm2();
            alarmManager=(AlarmManager)getActivity().getSystemService(ALARM_SERVICE);

            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,System.currentTimeMillis(),interval,pendingIntent);
            Toast.makeText(getActivity(), "Alarm Set", Toast.LENGTH_SHORT).show();
        }catch (Exception e){
           e.printStackTrace();
        }


    }


    public void cancelAlarm1() {
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);

            Toast.makeText(getContext(), "Alarm Canceled", Toast.LENGTH_SHORT).show();
        }
    }

    public void cancelAlarm2(){
        try {
            Intent alarmIntent = new Intent(getContext(), AlarmReceiver.class);
            pendingIntent = PendingIntent.getBroadcast(getContext(), 0, alarmIntent, 0);
            alarmManager= (AlarmManager)getContext().getSystemService(ALARM_SERVICE);
            alarmManager.cancel(pendingIntent);
            Toast.makeText(getContext(), "Alarm Canceled", Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
