package com.flystar.timenotes;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by flystar on 2015/3/2.
 */
public class NotesDB extends SQLiteOpenHelper
{
    public static String TABLE_NAME = "notes";
    public static String CONTENT = "content";
    public static String PATH = "path";
    public static String VIDEO = "video";
    public static String TIME = "time";
    public static String ID = "_id";

    public NotesDB(Context context) {
        super(context, "notesdb", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {

        //创建数据库
        db.execSQL("CREATE TABLE " + TABLE_NAME +"("+
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT" + "," +
                CONTENT + " TEXT NOT NULL," +
                PATH + " TEXT NOT NULL," +
                VIDEO + " TEXT NOT NULL,"+
                TIME + " TEXT NOT NULL"
        +")");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
