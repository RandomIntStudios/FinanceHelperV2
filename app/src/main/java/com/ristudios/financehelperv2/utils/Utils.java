package com.ristudios.financehelperv2.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.ristudios.financehelperv2.R;
import com.ristudios.financehelperv2.data.Item;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.MonthDay;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Locale;

public final class Utils {

    //region constants

    public static final String PREFS_CURRENT_BUDGET_KEY = "prefs:currentBudgetKey";
    public static final String PREFS_MAXIMUM_BUDGET_KEY = "prefs:maximumBudgetKey";
    public static final String PREFS_HAS_BEEN_LAUNCHED_KEY = "prefs:hasBeenLaunchedBefore";
    public static final String PREFS_FIRST_LAUNCH_YEAR_KEY = "prefs:firstLaunchYear";
    public static final String PREFS_FIRST_LAUNCH_MONTH_KEY = "prefs:firstLaunchMonth";
    public static final String INTENT_ACTION_BUDGET_RESET = "com.ristudios.financehelpeerv2.BUDGET_RESET";
    public static final int REQUEST_CODE_BUDGET_RESET = -1;
    public static final int REQUEST_CODE_NOTIFICATION_ONCLICK = -2;
    public static final int ID_NOTIFICATION_RESET = 1;
    public static final String INTENT_KEY_MONTH = "key_month";
    public static final String INTENT_KEY_YEAR = "key_year";

    //endregion

    public static float roundToTwoDecimals(float f){
        DecimalFormat format = (DecimalFormat) DecimalFormat.getInstance(Locale.getDefault());
        format.applyPattern("#.00");
        format.setRoundingMode(RoundingMode.HALF_EVEN);
        String formatted = format.format(f);
        formatted= formatted.replace(",", ".");
        return Float.parseFloat(formatted);
    }

    //region java.time utils

    public static ZonedDateTime getCurrentZonedTime()
    {
        return ZonedDateTime.now(ZoneId.systemDefault());
    }

    public static long getMillisForDate(ZonedDateTime zonedDateTime){
        return zonedDateTime.toInstant().toEpochMilli();
    }

    public static String getLocalizedFormattedDate(ZonedDateTime toFormat){
        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG);
        return formatter.format(toFormat);
    }

    public static String getLocalizedFormattedShortDate(ZonedDateTime toFormat){
        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT);
        return formatter.format(toFormat);
    }

    public static String getLocalizedFormattedDate(LocalDate toFormat){
        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG);
        return formatter.format(toFormat);
    }

    public static ZonedDateTime getDateFromMillis(long millis){
        ZonedDateTime date = ZonedDateTime.now(ZoneId.systemDefault());
        return date.with(Instant.ofEpochMilli(millis));
    }

    public static String getLocalizedFormattedDate(long millis){
        ZonedDateTime zonedDateTime = getDateFromMillis(millis);
        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT);
        return formatter.format(zonedDateTime);
    }
    public static String getLocalizedFormattedTime(long millis){
        ZonedDateTime zonedDateTime = getDateFromMillis(millis);
        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT);
        return formatter.format(zonedDateTime);
    }

    public static String getLocalizedFormattedDateTime(ZonedDateTime zonedDateTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM);
        return formatter.format(zonedDateTime);
    }

    public static long[] getSearchTimesForCurrentDay()
    {
        long start = Utils.getCurrentZonedTime().with(LocalTime.of(0,0,0)).toInstant().toEpochMilli();
        long end = Utils.getCurrentZonedTime().with(LocalTime.of(23, 59, 59)).toInstant().toEpochMilli();
        return new long[] {start, end};
    }

    public static long[] getSearchTimesForDate(int year, int monthValue, int dayOfMonth)
    {
        long start = Utils.getCurrentZonedTime().with(LocalDate.of(year, monthValue, dayOfMonth))
                .with(LocalTime.of(0,0,0)).toInstant().toEpochMilli();
        long end = Utils.getCurrentZonedTime().with(LocalDate.of(year, monthValue, dayOfMonth))
                .with(LocalTime.of(23,59,59)).toInstant().toEpochMilli();
        return new long[] {start, end};
    }

    public static long[] getSearchTimesForMonth(int year, int monthValue)
    {
        LocalDate monthStart = LocalDate.of(year, monthValue, 1);
        long start = getCurrentZonedTime().with(monthStart).toInstant().toEpochMilli();
        LocalDate monthEnd = monthStart.withDayOfMonth(monthStart.lengthOfMonth());
        long end = getCurrentZonedTime().with(monthEnd).toInstant().toEpochMilli();
        return new long[] {start, end};
    }

    public static long getTimeForAlarm(){
        ZonedDateTime current = Utils.getCurrentZonedTime();
        return current.with(LocalDate.of(current.getYear(), current.getMonth(), 1)).with(LocalTime.of(0,0,0)).plusMonths(1).toInstant().toEpochMilli();
    }

    //endregion

    public static ArrayList<Item> sortListByDate(ArrayList<Item> listToSort){
        listToSort.sort(new Comparator<Item>() {
            @Override
            public int compare(Item o1, Item o2) {
                return Long.compare(o1.getDateMillis(), o2.getDateMillis());
            }
        });
        return listToSort;
    }

    public static ArrayList<Item> sortListByPrice(ArrayList<Item> listToSort){
        ArrayList<Item> incomes = new ArrayList<>();
        ArrayList<Item> outgoings = new ArrayList<>();
        for (Item item: listToSort){
            if (item.isIncome()){
                incomes.add(item);
            }
            else{
                outgoings.add(item);
            }
        }
        incomes.sort(new Comparator<Item>() {
            @Override
            public int compare(Item o1, Item o2) {
                if (o1.getPriceTotal() < o2.getPriceTotal()){
                    return 1;
                }
                if (o1.getPriceTotal() > o2.getPriceTotal()){
                    return -1;
                }
                return 0;
            }
        });
        outgoings.sort(new Comparator<Item>() {
            @Override
            public int compare(Item o1, Item o2) {
                if (o1.getPriceTotal() > o2.getPriceTotal()){
                    return 1;
                }
                if (o1.getPriceTotal() < o2.getPriceTotal()){
                    return -1;
                }
                return 0;
            }
        });
        listToSort.clear();
        listToSort.addAll(incomes);
        listToSort.addAll(outgoings);
        return listToSort;
    }

    /**
     * Switches the name of a month with a string resource to display text in the correct language.
     * @param name original name of month (by month.name()).
     * @param context ApplicationContext.
     * @return the name of month from stringRes.
     */
    public static String getLocalMonthName(String name, Context context){
        String localName = "";
        String[] months = context.getResources().getStringArray(R.array.months);
        switch (name){
            case "january": localName = months[0];
                break;
            case "february": localName = months[1];
                break;
            case "march":localName = months[2];
                break;
            case "april":localName = months[3];
                break;
            case "may":localName = months[4];
                break;
            case "june":localName = months[5];
                break;
            case "july":localName = months[6];
                break;
            case "august":localName = months[7];
                break;
            case "september":localName = months[8];
                break;
            case "october":localName = months[9];
                break;
            case "november":localName = months[10];
                break;
            case "december":localName = months[11];
                break;
        }
        return localName;
    }

    /**
     * Converts a drawable into a bitmap so it can be used in methods which require bitmap objects.
     * @param drawable The drawable to convert.
     * @return Bitmap object displaying the drawable.
     */
    public static Bitmap drawableToBitmap(Drawable drawable) {

        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }


}
