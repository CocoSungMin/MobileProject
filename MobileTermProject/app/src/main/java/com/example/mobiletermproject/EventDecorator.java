package com.example.mobiletermproject;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.time.LocalDate;
import java.util.ArrayList;

public class EventDecorator implements DayViewDecorator {
    private final Drawable drawble;
    ArrayList<Schedule> list;

    public EventDecorator(Activity context, ArrayList<Schedule> list) {
        drawble = context.getResources().getDrawable(R.drawable.event_dot);
        this.list = list;
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        LocalDate d = LocalDate.of(day.getYear(), day.getMonth() + 1, day.getDay());

        for (Schedule s : list) {
            if (s.containsDate(d)) {
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
