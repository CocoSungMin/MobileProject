package com.example.mobiletermproject;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.time.LocalDateTime;

public class Schedule {
    private String title;
    private String content;

    //시작, 끝 날짜, 시간
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    //시간

    public Schedule(){
    }

    public Schedule(String title, String content, LocalDateTime start, LocalDateTime end){
        this.title = title;
        this.content = content;
        this.startTime = start;
        this.endTime = end;
    }

    public void setTitle(String title){this.title = title;}
    public void setContent(String content){this.content = content;}
    public void setStartDate(LocalDateTime start){this.startTime = start;}
    public void setEndDate(LocalDateTime end){this.endTime = end;}

    public String getTitle(){return title;}
    public String getContent(){return content;}
    public LocalDateTime getStartDate(){return startTime;}
    public LocalDateTime getEndDate(){return endTime;}

    /*뭐뭐 필요한지 보고 추가해야 할듯 싶습니다.*/
    public String toString(){
        String str = title;
        str += " " + content;
        str += " " + startTime.toString();
        str += " " + endTime.toString();
        return str;
    }
}
