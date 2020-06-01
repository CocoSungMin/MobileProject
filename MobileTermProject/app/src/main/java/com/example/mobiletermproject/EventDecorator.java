package com.example.mobiletermproject;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;

public class EventDecorator implements DayViewDecorator {
    private final Drawable drawble;
    ArrayList<Schedule> list;

    public EventDecorator(Activity context, ArrayList<Schedule> list) {
        drawble = context.getResources().getDrawable(R.drawable.event_dot);
        this.list = list;
        Log.d("dbtest", list.toString() + "2");
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean shouldDecorate(CalendarDay day) {
        LocalDate d = LocalDate.of(day.getYear(), day.getMonth() + 1, day.getDay());

        for (Schedule s : list) {
            if (s.containsDate(d)) {
                Log.d("dbtest", "event true");
                return true;
            }
        }
        return false;
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.setBackgroundDrawable(drawble);
    }


}
