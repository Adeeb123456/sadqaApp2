package com.geniteam.SadqaApp.BroadCastReceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.geniteam.SadqaApp.sms.AlarmManagerLocal;
import com.geniteam.SadqaApp.utils.AppConst;
import com.geniteam.SadqaApp.utils.AppConstants;
import com.geniteam.SadqaApp.utils.AppPref;

/**
 * Created by 7CT on 2/7/2018.
 */

public class BootCompleteReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)){
            Toast.makeText(context,"bootcomplete sdqa app",Toast.LENGTH_LONG).show();
            if (AppPref.getBooleanByKey(AppConstants.DAILY_SMS_key)) {
                Toast.makeText(context,"daily",Toast.LENGTH_LONG).show();

                AlarmManagerLocal.setAlarmDaily(context);

            }else  if (AppPref.getBooleanByKey(AppConstants.WEEKLY_SMS_key)){
                Toast.makeText(context,"weelkly",Toast.LENGTH_LONG).show();

                AlarmManagerLocal.scheduleAlarmWeekly(context,AppPref.getIntegerByKey(AppConstants.DAY_OF_WEEK_key));

            }else  if (AppPref.getBooleanByKey(AppConstants.BI_MONTHLY_key)){
                Toast.makeText(context,"bi monthly",Toast.LENGTH_LONG).show();

                AlarmManagerLocal.scheduleAlarmBiMonthly(context,AppPref.getIntegerByKey(AppConstants.DAY_OF_MONTH_key));

            }else  if (AppPref.getBooleanByKey(AppConstants.MONTHLYSMS_key)){
                Toast.makeText(context,"monthly",Toast.LENGTH_LONG).show();

                AlarmManagerLocal.scheduleAlarmMonthly(context,AppPref.getIntegerByKey(AppConstants.DAY_OF_MONTH_key));

            }
        }
    }
}
