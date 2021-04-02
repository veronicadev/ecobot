package veronicadev.ecobot.utils;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtils {
    public static String getDayName(int day, Locale locale) {
        DateFormatSymbols symbols = new DateFormatSymbols(locale);
        String[] dayNames = symbols.getWeekdays();
        return dayNames[day];
    }

    public static String getMonthName(int month, Locale locale) {
        DateFormatSymbols symbols = new DateFormatSymbols(locale);
        String[] monthNames = symbols.getMonths();
        return monthNames[month];
    }

    public static Calendar addDay(int amount, Date date){
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone("Europe/Rome"),Locale.ITALY);
        c.setTime(date);
        c.add(Calendar.DATE, amount);
        return c;
    }
}
