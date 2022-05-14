package com.samsung.mytime;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class OpenHelper extends SQLiteOpenHelper {

    public static final String TABLE_NAME = "event";
    public static final String DB_NAME = "eventDB";
    public static final int DB_VERSION = 2;
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_TIME = "time";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_EQUIPMENT = "equipment";

    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    public OpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query = "CREATE TABLE " + TABLE_NAME + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_DATE + " DATE, " +
                COLUMN_TIME + " TIME, " +
                COLUMN_PRICE + " TEXT, " +
                COLUMN_EQUIPMENT + " TEXT)";
        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void insert(Event event) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME, event.getName());
        contentValues.put(COLUMN_DATE, String.valueOf(event.getDate()));
        String time = event.getTime().truncatedTo(ChronoUnit.SECONDS).toString();
        contentValues.put(COLUMN_TIME, time);
        contentValues.put(COLUMN_PRICE, event.getPrice());
        contentValues.put(COLUMN_EQUIPMENT, event.getEquipment());
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_NAME, null, contentValues);
    }

    public void deleteAll() {
        SQLiteDatabase database = getReadableDatabase();
        database.delete(TABLE_NAME, null, null);
        Event.eventsList.clear();
    }
    public void deleteCurrentEvent(Event event){
        SQLiteDatabase database = getWritableDatabase();
        String eventName = event.getName();
        String eventDate = event.getDate().toString();
        String eventTime = event.getTime().toString();
        String eventPrice = event.getPrice();
        String eventEquipment = event.getEquipment();
        database.delete(TABLE_NAME, "name=? and date=? and time=? and price=? and equipment=?", new String[]{eventName, eventDate, eventTime, eventPrice, eventEquipment});
        Event.eventsList.remove(event);
        Event.eventsForDate(CalendarUtils.selectedDate).remove(event);
    }

    public void findAllEvents(){
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
                String price = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRICE));
                String equipment = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EQUIPMENT));
                Event.eventsList.add(new Event(id, nameId, dateId, timeId, price, equipment));
            } while (cursor.moveToNext());
        } catch (Exception e){
            Log.e("TAG", e.getMessage());
        }
        cursor.close();
    }
}
