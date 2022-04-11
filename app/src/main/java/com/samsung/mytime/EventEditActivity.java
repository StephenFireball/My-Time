package com.samsung.mytime;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.time.LocalTime;

public class EventEditActivity extends AppCompatActivity{
    private EditText eventNameET;
    private TextView eventDateTV, eventTimeTV;

    private LocalTime time;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_edit);
        initWidgets();
        time = LocalTime.now();
        eventDateTV.setText("Date: " + CalendarUtils.formattedDate(CalendarUtils.selectedDate));
        eventTimeTV.setText("Time: " + CalendarUtils.formattedTime(time));
    }

    private void initWidgets(){
        eventNameET = findViewById(R.id.eventNameET);
        eventDateTV = findViewById(R.id.eventDateTV);
        eventTimeTV = findViewById(R.id.eventTimeTV);
    }

    public void saveEventAction(View view){
        String eventName = eventNameET.getText().toString();
        Event newEvent = new Event(eventName, CalendarUtils.selectedDate, time);
        Event.eventsList.add(newEvent);
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("app.db", MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS events (eventname TEXT NOT NULL, eventtime TEXT NOT NULL)");
        db.execSQL("INSERT INTO events VALUES ('" + eventName + "', '" + CalendarUtils.formatedDatedb(CalendarUtils.selectedDate) + "')");
        db.close();
        finish();
    }
}