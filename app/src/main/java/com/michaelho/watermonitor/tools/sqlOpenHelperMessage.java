package com.michaelho.watermonitor.tools;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.michaelho.watermonitor.constants.MessageConstants;
import com.michaelho.watermonitor.objects.SingleMessage;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2015/8/17.
 */
public class sqlOpenHelperMessage extends SQLiteOpenHelper implements MessageConstants {
    public static final String DBNAME = "messageboxdb.sqlite";
    public static final int VERSION = 1;
    public static final String TABLENAME = "table_inbox";

    public sqlOpenHelperMessage(Context context) {
        super(context, DBNAME, null, VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        createDatabase(db);
    }

    private void createDatabase(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLENAME + "(" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                MESSAGE_TITLE + " VARCHAR(60)," +
                MESSAGE_CONTENT + " TEXT," +
                MESSAGE_SENDER + " VARCHAR(60), " +
                MESSAGE_TIME + " VARCHAR(60), " +
                MESSAGE_IFREAD + " VARCHAR(10)" + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLENAME);
        onCreate(db);
    }

    public long insertDB(SingleMessage singleMessage) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MESSAGE_TITLE, singleMessage.getTitle());
        values.put(MESSAGE_CONTENT, singleMessage.getDetail());
        values.put(MESSAGE_SENDER, singleMessage.getSender());
        values.put(MESSAGE_TIME, singleMessage.getTime());
        values.put(MESSAGE_IFREAD, singleMessage.getIfRead());
        long rowId = db.insert(TABLENAME, null, values);
        db.close();
        return rowId;
    }

    public int UpdateIfRead(String detail) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MESSAGE_IFREAD, "1");
        String whereClause = MESSAGE_CONTENT + "='" + detail + "'";
        int count = db.update(TABLENAME, values, whereClause, null);
        db.close();
        return count;
    }

    public SingleMessage getFullDetail(int id) {
        SQLiteDatabase db = getWritableDatabase();
        String[] columns = { MESSAGE_TITLE, MESSAGE_CONTENT, MESSAGE_SENDER, MESSAGE_TIME, MESSAGE_IFREAD};
        String whereClause = ID + " = ?;";
        String[] whereArgs = { Integer.toString(id) };
        Cursor cursor = db.query(TABLENAME, columns, whereClause, whereArgs,
                null, null, null);
        cursor.moveToNext();
        String title = cursor.getString(0);
        String detail = cursor.getString(1);
        String sender = cursor.getString(2);
        String time = cursor.getString(3);
        String ifread = cursor.getString(4);
        SingleMessage singleMessage = new SingleMessage(title, detail, sender, time, ifread);
        db.close();
        return singleMessage;
    }



    public ArrayList<HashMap<String, Object>> getAllMessageDetail() {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<HashMap<String, Object>> locations = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> item = new HashMap<String, Object>();
        String sql = "SELECT * FROM " + TABLENAME;
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            item.put(MESSAGE_TITLE, cursor.getString(0));
            item.put(MESSAGE_CONTENT, cursor.getString(1));
            item.put(MESSAGE_SENDER, cursor.getString(2));
            item.put(MESSAGE_TIME, cursor.getString(3));
            item.put(MESSAGE_IFREAD, cursor.getString(4));
            locations.add(item);
        }

        cursor.close();
        db.close();
        return locations;
    }

    public boolean deleteDataById(String id){
        SQLiteDatabase db = getWritableDatabase();
        String WhereClause = ID + "='" + id + "'";
        int count = db.delete(TABLENAME, WhereClause, null);
        db.close();
        return count > 0;
    }
}
