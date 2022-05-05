package com.samsung.mytime;

import static android.content.ContentValues.TAG;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;

public class OpenHelper extends SQLiteOpenHelper {

    private static OpenHelper openHelper;

    public static final String TABLE_NAME = "event";
    public static final String DB_NAME = "eventDB";
    public static final int DB_VERSION = 1;
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_TIME = "time";
    private static final String MY_LOG = "";

    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    public OpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query = "CREATE TABLE " + TABLE_NAME + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " + COLUMN_DATE + " DATE, " + COLUMN_TIME + " TIME)";
        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public long insert(Event event) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME, event.getName());
        contentValues.put(COLUMN_DATE, String.valueOf(event.getDate()));
        String time = event.getTime().truncatedTo(ChronoUnit.SECONDS).toString();
        contentValues.put(COLUMN_TIME, time);
        SQLiteDatabase db = getWritableDatabase();
        return db.insert(TABLE_NAME, null, contentValues);
    }

    public void deleteAll() {
        SQLiteDatabase database = getReadableDatabase();
        database.delete(TABLE_NAME, null, null);
        Event.eventsList.clear();
    }
    public static OpenHelper instanceOfDB(Context context){
        if (openHelper == null){
            openHelper = new OpenHelper(context);
        }
        return openHelper;
    }

    public ArrayList<Event> findAllEvents(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);
        try {
            cursor.moveToFirst();
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
                String nameId = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME));
                String date = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE));
                String time = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TIME));
                LocalDate dateId = LocalDate.parse(date, dateFormatter);
                LocalTime timeId = LocalTime.parse(time, timeFormatter);
                Event.eventsList.add(new Event(id, nameId, dateId, timeId));
            } while (cursor.moveToNext());
        } catch (Exception e){
            Log.e("TAG", e.getMessage());
        }
        return null;
    }
}
