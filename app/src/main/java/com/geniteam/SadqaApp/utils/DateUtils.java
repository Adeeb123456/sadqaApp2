package com.geniteam.SadqaApp.utils;

/**
 * Created by 7CT on 2/7/2018.
 */
import android.content.Context;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class DateUtils {


    public static void differenceOfTwoDates(String lastDateStr,String futureDateStr) throws ParseException {
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        Date lastDate=simpleDateFormat.parse(lastDateStr);

        Date futureDate=simpleDateFormat.parse(futureDateStr);

        Calendar calendar1=Calendar.getInstance();
        Calendar calendar2=Calendar.getInstance();

        calendar1.setTime(lastDate);
        calendar2.setTime(futureDate);

        System.out.println("Compare Result : " + calendar2.compareTo(calendar1));
        System.out.println("Compare Result : " + calendar1.compareTo(calendar2));
        Log.i("debug","Compare Result : " + calendar2.compareTo(calendar1));
        Log.i("debug","Compare Result : " + calendar1.compareTo(calendar2));
/*Returns 0 if the times of the two Calendars are equal, -1 if the time of this Calendar is before the other one, 1 if the time of this Calendar is after the other one*/



    }

    public static boolean isTwoDatesAreEqual(String lastDateStr,String futureDateStr) throws ParseException {
        boolean isDatesEquals=false;
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        Date lastDate=simpleDateFormat.parse(lastDateStr);

        Date futureDate=simpleDateFormat.parse(futureDateStr);

        Calendar calendar1=Calendar.getInstance();
        Calendar calendar2=Calendar.getInstance();

        calendar1.setTime(lastDate);
        calendar2.setTime(futureDate);


        System.out.println("Compare Result : " + calendar2.compareTo(calendar1));
        System.out.println("Compare Result : " + calendar1.compareTo(calendar2));
        Log.i("debug","Compare Result : " + calendar2.compareTo(calendar1));
        Log.i("debug","Compare Result : " + calendar1.compareTo(calendar2));
/*Returns 0 if the times of the two Calendars are equal, -1 if the time of this Calendar is before the other one, 1 if the time of this Calendar is after the other one*/

if(calendar2.compareTo(calendar1)==0){
    isDatesEquals=true;
}else {
    isDatesEquals=false;
}

return isDatesEquals;

    }
    public static boolean isDate1GreaterDate2(String lastDateStr,String futureDateStr) throws ParseException {
        boolean isDatesEquals=false;
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        Date lastDate=simpleDateFormat.parse(lastDateStr);

        Date futureDate=simpleDateFormat.parse(futureDateStr);

        Calendar calendar1=Calendar.getInstance();
        Calendar calendar2=Calendar.getInstance();

        calendar1.setTime(lastDate);
        calendar2.setTime(futureDate);


        System.out.println("Compare Result : " + calendar2.compareTo(calendar1));
        System.out.println("Compare Result : " + calendar1.compareTo(calendar2));
        Log.i("debug","Compare Result : " + calendar2.compareTo(calendar1));
        Log.i("debug","Compare Result : " + calendar1.compareTo(calendar2));
/*Returns 0 if the times of the two Calendars are equal, -1 if the time of this Calendar is before the other one, 1 if the time of this Calendar is after the other one*/

        if(calendar1.compareTo(calendar2)==-1){
            isDatesEquals=true;
        }else {
            isDatesEquals=false;
        }

        return isDatesEquals;

    }

    public static String getCurrentDate(Context context) {


        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());



        return currentDate;

    }

    public static String getNextWeekDate(){
        String nextWeekDate="";
       int day;
        int month;
                int year;

        GregorianCalendar cal = new GregorianCalendar();
       // cal.add(Calendar.DAY_OF_W, 2);
       nextWeekDate=cal.get(Calendar.DAY_OF_MONTH) + "-" + cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.US) + "-" + cal.get(Calendar.YEAR);
        day = cal.get(Calendar.DAY_OF_MONTH);
        month = cal.get(Calendar.MONTH);
        year = cal.get(Calendar.YEAR);

        return nextWeekDate;
    }

   public static int   getDayOFMonth() {
       Calendar calendar = Calendar.getInstance();
       int day = calendar.get(Calendar.DAY_OF_MONTH);
        GregorianCalendar cal = new GregorianCalendar();
       cal.add(Calendar.MONTH, 1);
   //  String s=  cal.get(Calendar.DAY_OF_MONTH) + "-" + cal.get(Calendar.MONTH) + "-" + cal.get(Calendar.YEAR);
     // int  day = cal.get(Calendar.DAY_OF_MONTH);
       // int  month = cal.get(Calendar.MONTH);
      ///  int  year = cal.get(Calendar.YEAR);

        Log.d("debug","dayofmonth "+day);
      // Log.i("degug","date "+s);


return day;
    }


    public static int  getDayOFWeek() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
       // GregorianCalendar cal = new GregorianCalendar();
      //  cal.add(Calendar.MONTH, 1);
        //String s=  cal.get(Calendar.DAY_OF_MONTH) + "-" + cal.get(Calendar.MONTH) + "-" + cal.get(Calendar.YEAR);
      //  int  day = cal.get(Calendar.DAY_OF_MONTH);
      //  int  month = cal.get(Calendar.MONTH);
      //  int  year = cal.get(Calendar.YEAR);

        Log.d("debug","dayofweek "+day);
     //   Log.i("degug","date "+s);

        return day;
    }
}
