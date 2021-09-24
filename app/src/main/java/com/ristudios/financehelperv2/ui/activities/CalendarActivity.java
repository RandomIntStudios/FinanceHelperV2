package com.ristudios.financehelperv2.ui.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Toast;

import com.kizitonwose.calendarview.CalendarView;
import com.kizitonwose.calendarview.model.CalendarDay;
import com.kizitonwose.calendarview.model.CalendarMonth;
import com.kizitonwose.calendarview.model.DayOwner;
import com.kizitonwose.calendarview.model.ScrollMode;
import com.kizitonwose.calendarview.ui.DayBinder;
import com.kizitonwose.calendarview.ui.MonthHeaderFooterBinder;
import com.ristudios.financehelperv2.R;
import com.ristudios.financehelperv2.ui.adapter.DayViewContainer;
import com.ristudios.financehelperv2.ui.adapter.MonthViewContainer;
import com.ristudios.financehelperv2.utils.Utils;

import org.jetbrains.annotations.NotNull;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.WeekFields;
import java.util.Locale;

public class CalendarActivity extends BaseActivity implements DayViewContainer.CalendarClickListener {

    private ActivityResultLauncher<Intent> launcher;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_calendar);
        super.onCreate(savedInstanceState);
        initBurgerMenu();
        getSupportActionBar().setTitle(R.string.calendar);
        initData();
        setUpOnActivityResult();
        setUpCalendar(this);
    }

    private void initData() {
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    }

    /**
     * Registers this Activity to receive ActivityResults.
     */
    private void setUpOnActivityResult() {
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    Intent data = result.getData();
                    if (result.getResultCode() == Activity.RESULT_OK){

                        int y, m;
                        if (data != null) {
                            y = data.getIntExtra("resultYear", 0);
                            m = data.getIntExtra("resultMonth", 0);
                        }
                        else{
                            y = Utils.getCurrentZonedTime().getYear();
                            m = Utils.getCurrentZonedTime().getMonthValue();
                        }
                        YearMonth last = YearMonth.of(y, m);
                        CalendarView calendarView = findViewById(R.id.calendar_view);
                        calendarView.scrollToMonth(last);
                    }
                    else if (result.getResultCode() == Activity.RESULT_CANCELED && data != null){
                        if (data.getBooleanExtra("finishParent", false)) {
                            finish();
                        }
                    }
                });
    }

    /**
     * Sets up the calendar by creating and binding a {@link DayViewContainer} to display texts for single days and also a
     * {@link MonthViewContainer} to display a Header for the current selected month.
     * @param context Application context.
     */
    private void setUpCalendar(Context context) {
        CalendarView calendarView = findViewById(R.id.calendar_view);
        calendarView.setDayBinder(new DayBinder<DayViewContainer>() {

            @Override
            public @NotNull DayViewContainer create(@NotNull View view) {
                return new DayViewContainer(view, CalendarActivity.this);
            }

            @Override
            public void bind(@NotNull DayViewContainer dayViewContainer, @NotNull CalendarDay calendarDay) {
                dayViewContainer.day = calendarDay;
                dayViewContainer.calendarDayText.setText(String.valueOf(calendarDay.getDate().getDayOfMonth()));
                ZonedDateTime check = ZonedDateTime.now(ZoneId.systemDefault());
                if (Utils.getMillisForDate(check.with(LocalDate.now())) == Utils.getMillisForDate(check.with(calendarDay.getDate())))
                {
                    dayViewContainer.calendarDayText.setTextColor(getColor(R.color.bright_red));
                }
                if (calendarDay.getOwner() != DayOwner.THIS_MONTH){
                    dayViewContainer.calendarDayText.setText("");
                }

            }


        });
        String[] weekdays_short = context.getResources().getStringArray(R.array.weekdays_short);
        calendarView.setMonthHeaderBinder(new MonthHeaderFooterBinder<MonthViewContainer>() {
            @Override
            public @NotNull MonthViewContainer create(@NotNull View view) {
                return new MonthViewContainer(view);
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void bind(@NotNull MonthViewContainer viewContainer, @NotNull CalendarMonth calendarMonth) {
                String localMonthName = Utils.getLocalMonthName(calendarMonth.getYearMonth().getMonth().name().toLowerCase(), context);
                viewContainer.txtHeader.setText(localMonthName + " " + calendarMonth.getYear());
                viewContainer.txtMon.setText(weekdays_short[0]);
                viewContainer.txtTue.setText(weekdays_short[1]);
                viewContainer.txtWed.setText(weekdays_short[2]);
                viewContainer.txtThu.setText(weekdays_short[3]);
                viewContainer.txtFri.setText(weekdays_short[4]);
                viewContainer.txtSat.setText(weekdays_short[5]);
                viewContainer.txtSat.setTextColor(Color.RED);
                viewContainer.txtSun.setText(weekdays_short[6]);
                viewContainer.txtSun.setTextColor(Color.RED);
            }
        });
        calendarView.setScrollMode(ScrollMode.PAGED);
        int y = preferences.getInt(Utils.PREFS_FIRST_LAUNCH_YEAR_KEY, 2021);
        int m = preferences.getInt(Utils.PREFS_FIRST_LAUNCH_MONTH_KEY, 8);
        YearMonth firstMonth = YearMonth.of(y, m).minusMonths(1);
        YearMonth lastMonth = YearMonth.now().plusMonths(1);
        DayOfWeek firstDayOfWeek = WeekFields.of(Locale.getDefault()).getFirstDayOfWeek();
        calendarView.setup(firstMonth, lastMonth, firstDayOfWeek);
        calendarView.scrollToMonth(YearMonth.now(ZoneId.systemDefault()));
    }

    @Override
    public void onDateClicked(CalendarDay day) {
        Intent intent = new Intent(this, CalendarDayActivity.class);
        intent.putExtra("year", day.getDate().getYear());
        intent.putExtra("month", day.getDate().getMonthValue());
        intent.putExtra("day", day.getDate().getDayOfMonth());
        launcher.launch(intent);
    }
}