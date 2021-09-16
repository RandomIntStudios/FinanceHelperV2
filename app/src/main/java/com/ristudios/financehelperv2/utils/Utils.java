package com.ristudios.financehelperv2.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

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
import java.util.Locale;

public final class Utils {

    //region Preference Keys

    public static final String PREFS_CURRENT_BUDGET_KEY = "prefs:currentBudgetKey";
    public static final String PREFS_MAXIMUM_BUDGET_KEY = "prefs:maximumBudgetKey";

    //endregion

    public static float roundToTwoDecimals(float f){
        DecimalFormat format = (DecimalFormat) DecimalFormat.getInstance(Locale.getDefault());
        format.applyPattern("#.00");
        format.setRoundingMode(RoundingMode.HALF_EVEN);
        String formatted = format.format(f);
        formatted= formatted.replace(",", ".");
        return Float.parseFloat(formatted);
    }

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
