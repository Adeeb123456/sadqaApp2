package com.geniteam.SadqaApp.sms;

/**
 * Created by 7CT on 2/7/2018.
 */
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtils {


    public void differenceOfTwoDates(String lastDateStr,String futureDateStr) throws ParseException {
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        Date lastDate=simpleDateFormat.parse(lastDateStr);

        Date futureDate=simpleDateFormat.parse(futureDateStr);

        Calendar calendar1=Calendar.getInstance();
        Calendar calendar2=Calendar.getInstance();

        calendar1.setTime(lastDate);
        calendar2.setTime(futureDate);

        System.out.println("Compare Result : " + calendar2.compareTo(calendar1));
        System.out.println("Compare Result : " + calendar1.compareTo(calendar2));


        //calendar.

    }
}
