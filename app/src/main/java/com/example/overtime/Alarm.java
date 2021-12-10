package com.example.overtime;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;

public class Alarm {
    private String title;
    private Date time;
    private ArrayList<String> daysOfWeek;
    private String amOrPm;
    private boolean onOrOff;
    private boolean expanded;

    public String getTitle(){
        return title;
    }
    public void setTitle(String title){
        this.title = title;
    }
    public Date getTime(){
        return time;
    }
    public void setTime(Date time){
        this.time = time;
    }
    public ArrayList<String> getDaysOfWeek(){
        return daysOfWeek;
    }
    public void setDaysOfWeek(ArrayList<String> daysOfWeek){
        this.daysOfWeek = daysOfWeek;
    }
    public String getAmOrPm(){
        return amOrPm;
    }
    public void setAmOrPm(String amOrPm){
        this.amOrPm = amOrPm;
    }
    public boolean isOnOrOff(){
        return onOrOff;
    }
    public void setOnOrOff(boolean onOrOff){
        this.onOrOff = onOrOff;
    }
    public boolean isExpanded(){
        return expanded;
    }
    public void setExpanded(boolean expanded){
        this.expanded = expanded;
    }

    public Alarm(String title, Date time, String amOrPm, ArrayList<String> daysOfWeek, boolean onOrOff){
        this.title = title;
        this.time = time;
        this.amOrPm = amOrPm;
        this.daysOfWeek = daysOfWeek;
        this.onOrOff = onOrOff;
        this.expanded = false;
    }

    @Override
    public String toString() {
        return "Alarm{" +
                "title='" + title + '\'' +
                ", time=" + time +
                ", daysOfWeek='" + daysOfWeek + '\'' +
                ", onOrOff=" + onOrOff +
                ", expanded=" + expanded +
                '}';
    }
}
