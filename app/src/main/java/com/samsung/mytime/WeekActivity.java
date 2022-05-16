package com.samsung.mytime;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.util.ArrayList;

public class WeekActivity extends AppCompatActivity implements CalendarAdapter.OnItemListener{
    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private ListView eventListView;
    public static int eventPosition;
    ArrayList<Event> dailyEvents;
    public static int op_count = 0;
    OpenHelper openHelper = new OpenHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week_view);
        initWidgets();
        setWeekView();
        if (op_count == 0) {
            openHelper.findAllEvents();
            op_count += 1;
        }
    }

    private void initWidgets(){
        calendarRecyclerView = findViewById(R.id.calendarRecyclerView);
        monthYearText = findViewById(R.id.monthYearTV);
        eventListView = findViewById(R.id.eventListView);
    }

    private void setWeekView(){
        monthYearText.setText(CalendarUtils.monthYearFromDate(CalendarUtils.selectedDate));
        ArrayList<LocalDate> days = CalendarUtils.daysInWeekArray(CalendarUtils.selectedDate);

        CalendarAdapter calendarAdapter = new CalendarAdapter(days, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
        setEventAdapter();
    }


    public void previousWeekAction(View view){
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusWeeks(1);
        setWeekView();
    }

    public void nextWeekAction(View view){
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusWeeks(1);
        setWeekView();
    }

    @Override
    public void onItemClick(int position, LocalDate date){
        CalendarUtils.selectedDate = date;
        setWeekView();
    }

    @Override
    protected void onResume(){
        super.onResume();
        setEventAdapter();
    }

    private void setEventAdapter(){
        dailyEvents = Event.eventsForDate(CalendarUtils.selectedDate);
        EventAdapter eventAdapter = new EventAdapter(getApplicationContext(), dailyEvents);
        eventListView.setAdapter(eventAdapter);
        eventListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                eventPosition = i;
                eventDetailDialog();
            }
        });
        eventListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                eventDeleteDialog();
                return true;
            }
        });
    }

    private void eventDeleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        Event event = dailyEvents.get(eventPosition);
        String eventName = event.getName();
        String eventDate = event.getDate().toString();
        String eventTime = event.getTime().toString();
        String eventPrice = event.getPrice();
        String eventEquipment = event.getEquipment();
        builder.setTitle("Event delete").setMessage("Are you sure? Your event will be deleted with no restore option!").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                openHelper.deleteCurrentEvent(event);
                setWeekView();
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        builder.create().show();
    }

    public void eventDetailDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        Event event = dailyEvents.get(eventPosition);
        String eventName = event.getName();
        String eventDate = event.getDate().toString();
        String eventTime = event.getTime().toString();
        String eventPrice = event.getPrice();
        String eventEquipment = event.getEquipment();
        builder.setTitle("Event Detail").setMessage("Name:\n" + eventName + "\nDate:\n"
                + eventDate + "\nTime:\n" + eventTime + "\nPrice:\n"
                + eventPrice + "\nEquipment:\n" + eventEquipment).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                onResume();
            }
        });
        builder.create().show();
    }

    public void newEventAction(View view){
        startActivity(new Intent(this, EventEditActivity.class));
    }

    public void deleteAllEventsAction(View view) {
        AlertDialog deleteDialog = new AlertDialog.Builder(this).setTitle("Delete Events")
                .setMessage("Are you sure? Your events will be deleted with no restore option!")
                .setPositiveButton("Ok", null)
                .setNegativeButton("Cancel", null)
                .show();

        Button positive = deleteDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openHelper.deleteAll();
                deleteDialog.dismiss();
                setWeekView();
            }
        });
    }

}