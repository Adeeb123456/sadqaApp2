package com.geniteam.SadqaApp.utils;

/**
 * Created by 7CT on 1/26/2018.
 */

public class AppConstants {

    // Sms related constants

    public static final String SADQA_SMS_SENDED_SUCCESS="success";
    public static final String SADQA_SMS_FAIL="fail";
    public static  final String SADQA_SMS_FAIL_NO_MOBILE_SERVICE="No mobile service";
    public static  final String SADQA_SMS_FAIL_NO_SERVICE_PROVIDER="Fail to send sadqa sms ";
    public static  final String RESULT_ERROR_GENERIC_FAILURE="RESULT_ERROR_GENERIC_FAILURE";
    public static  final String RESULT_ERROR_NULL_PDU="RESULT_ERROR_NULL_PDU";
    public static  final String RESULT_ERROR_RADIO_OFF="RESULT_ERROR_RADIO_OFF";

    public static final String SADQA_SMS_NUMBER="03101883575";

    public static final String NUMBER_OF_SMS_TO_SEND="number_of_sms_to_send";

    //permissions request codes
    public static final int REQ_LOCATION = 100;
    public static final int SEND_SMS_PERMISSION=101;
    public static final int READ_PHONE_STATE_PERMISSION=102;
    public static final int GPS_PERMISSION_CODE=103;



    public static final String DAILY_SMS_key="dailySmsBOOL";
    public static final String WEEKLY_SMS_key ="weeklySmsBOOL";
    public static final String BI_MONTHLY_key="bimonthlySmsBOOL";
    public static final String MONTHLYSMS_key="monthly";

    public static final String DAY_OF_WEEK_key="dayOfweek";
    public static final String DAY_OF_MONTH_key="dayOfMonth";

    public static final String TO_DAY_DATE_key="TO_DAY_DATE";


    //
    public static boolean isSendSms=false;

}
