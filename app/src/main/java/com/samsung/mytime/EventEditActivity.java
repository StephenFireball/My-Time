package com.samsung.mytime;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public class EventEditActivity extends AppCompatActivity{
    private EditText eventNameET;
    private TextView eventDateTV;
    private Button eventTimeButton;
    int hour, minute;
    String strTime;
    private LocalTime time;
    private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
    OpenHelper openHelper = new OpenHelper(this);

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_edit);
        initWidgets();
        time = LocalTime.now();
        eventDateTV.setText("Date: " + CalendarUtils.formattedDate(CalendarUtils.selectedDate));
    }

    private void initWidgets(){
        eventNameET = findViewById(R.id.eventNameET);
        eventDateTV = findViewById(R.id.eventDateTV);
        eventTimeButton = findViewById(R.id.eventTimeButton);
    }
    public void popOutTimePicker(View view){
        eventTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                hour = calendar.get(Calendar.HOUR);
                minute = calendar.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(EventEditActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minuteOfDay) {
                        Toast.makeText(EventEditActivity.this, ""+hourOfDay+":"+minuteOfDay, Toast.LENGTH_LONG);
                        strTime = ""+hourOfDay+":"+minuteOfDay;
                    }
                }, hour, minute, true);
                timePickerDialog.show();
            }
        });
    }

    public void saveEventAction(View view){
        String eventName = eventNameET.getText().toString();
        time = LocalTime.parse(strTime, timeFormatter);
        Event newEvent = new Event(eventName, CalendarUtils.selectedDate, time);
        Event.eventsList.add(newEvent);
        openHelper.insert(newEvent);
        finish();
    }
}