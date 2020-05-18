package com.example.mobiletermproject;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.Calendar;

public class EventDecorator implements DayViewDecorator {
    private final Calendar calendar = Calendar.getInstance();
    //private final Drawable drawble;

    public EventDecorator(){

    }

    /*
    public SetBackgroundDrawableDecorator(Context context){
        drawble = context.getResources().getDrawable(R.drawable.buttonshape);
    }
    */


    @Override
    public boolean shouldDecorate(CalendarDay day) {
        day.copyTo(calendar);
        //이벤트 있는 날짜 db에서 받아오기 // 지금은 확인을 위해 수요일로 때려밖음
        int eventday = calendar.get(calendar.DAY_OF_WEEK);
        return eventday == Calendar.WEDNESDAY;
    }

    @Override
    public void decorate(DayViewFacade view) {
        //view.setBackgroundDrawable()
    }
}
