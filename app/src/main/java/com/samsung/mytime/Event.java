package com.samsung.mytime;


import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class Event{
    public static ArrayList<Event> eventsList = new ArrayList<>();


    public static ArrayList<Event> eventsForDate(LocalDate date){
        ArrayList<Event> events = new ArrayList<>();

        for(Event event : eventsList){
            if(event.getDate().equals(date))
                events.add(event);
        }

        return events;
    }

    private int id;
    private String name;
    private LocalDate date;
    private LocalTime time;

    public Event(int id, String name, LocalDate date, LocalTime time) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
    }

    public Event(String name, LocalDate date, LocalTime time){
        this.name = name;
        this.date = date;
        this.time = time;
    }

    public String getName(){
        return name;
    }

    public LocalDate getDate(){
        return date;
    }

    public LocalTime getTime(){
        return time;
    }

    public void setTime(LocalTime time){
        this.time = time;
    }
}
