package com.example.mobiletermproject;

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

    public EventDecorator(){

    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        day.copyTo(calendar);
        //이벤트 있는 날짜 db에서 받아오기 // 지금은 확인을 위해 수요일로 때려밖음
        int eventday = calendar.get(calendar.DAY_OF_WEEK);
        return eventday == Calendar.WEDNESDAY;
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.setBackgroundDrawable(new Drawable() {
            @Override
            public void draw(@NonNull Canvas canvas) {
                Paint paint = new Paint();
                paint.setColor(Color.RED);
                canvas.drawCircle(0,0,10,paint);
            }

            @Override
            public void setAlpha(int alpha) {

            }

            @Override
            public void setColorFilter(@Nullable ColorFilter colorFilter) {

            }

            @Override
            public int getOpacity() {
                return 0;
            }
        });
    }
}
