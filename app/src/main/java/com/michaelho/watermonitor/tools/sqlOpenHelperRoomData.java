package com.michaelho.watermonitor.tools;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.michaelho.watermonitor.constants.RoomConstants;
import com.michaelho.watermonitor.objects.Room;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2015/8/17.
 */
public class sqlOpenHelperRoomData extends SQLiteOpenHelper implements RoomConstants {

    public static final String DBNAME = "tasksdb.sqlite";
    public static final int VERSION = 1;
    public static final String TABLENAME = "room_stats";

    public sqlOpenHelperRoomData(Context context) {
        super(context, DBNAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createDatabase(db);
    }

    private void createDatabase(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLENAME + "(" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                ROOM_NAME + " VARCHAR(15)," +
                ROOM_DATA + " VARCHAR(50)," +
                ROOM_FLOOR + " VARCHAR(10)," +
                ROOM_DATE + " VARCHAR(30)," +
                ROOM_TIME + " VARCHAR(50)" + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLENAME);
        onCreate(db);
    }

    public void deletAll() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("delete from "+ TABLENAME);
    }

    public long insertDB(Room room) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ROOM_NAME, room.getName());
        values.put(ROOM_DATA, room.getWaterData());
        values.put(ROOM_FLOOR, room.getFloor());
        values.put(ROOM_DATE, room.getTimeDate());
        values.put(ROOM_TIME, room.getTime());
        long rowId = db.insert(TABLENAME, null, values);
        db.close();
        return rowId;
    }

    public int UpdateDB(Room room) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ROOM_NAME, room.getName());
        values.put(ROOM_DATA, room.getWaterData());
        values.put(ROOM_FLOOR, room.getFloor());
        values.put(ROOM_DATE, room.getTimeDate());
        values.put(ROOM_TIME, room.getTime());
        String whereClause = ROOM_NAME + "='" + room.getName() + "'";
        int count = db.update(TABLENAME, values, whereClause, null);
        db.close();
        return count;
    }

    public Room getDetailById(int id) {
        SQLiteDatabase db = getWritableDatabase();
        String[] columns = { ROOM_NAME, ROOM_DATA, ROOM_FLOOR, ROOM_DATE, ROOM_TIME};
        String whereClause = ID + " = ?;";
        String[] whereArgs = { Integer.toString(id) };
        Cursor cursor = db.query(TABLENAME, columns, whereClause, whereArgs,
                null, null, null);
        cursor.moveToNext();
        String name = cursor.getString(0);
        String data = cursor.getString(1);
        String floor = cursor.getString(2);
        String date = cursor.getString(3);
        String time = cursor.getString(4);
        Room room = new Room(name, data, floor, date, time);
        return room;
    }

    public ArrayList<HashMap<String, Object>> getRoomListByFloor(String floor) {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<HashMap<String, Object>> rooms = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> item = new HashMap<String, Object>();
        String sql = "SELECT * FROM " + TABLENAME + " WHERE "+ROOM_FLOOR+" = " + floor;
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            item.put(ROOM_NAME, cursor.getString(0));
            item.put(ROOM_DATA, cursor.getString(1));
            item.put(ROOM_FLOOR, cursor.getString(2));
            item.put(ROOM_DATE, cursor.getString(3));
            item.put(ROOM_TIME, cursor.getString(4));
            rooms.add(item);
        }
        cursor.close();
        db.close();
        return rooms;
    }
}
