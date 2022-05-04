package com.samsung.mytime;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDate;
import java.util.ArrayList;

public class WeekActivity extends AppCompatActivity implements CalendarAdapter.OnItemListener{
    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private ListView eventListView;
    OpenHelper openHelper = new OpenHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week_view);
        initWidgets();
        setWeekView();
        openHelper.findAllEvents();
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
        setEventAdpater();
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
        setEventAdpater();
    }

    private void setEventAdpater(){
        ArrayList<Event> dailyEvents = Event.eventsForDate(CalendarUtils.selectedDate);
        EventAdapter eventAdapter = new EventAdapter(getApplicationContext(), dailyEvents);
        eventListView.setAdapter(eventAdapter);
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