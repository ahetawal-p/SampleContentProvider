package com.sampleapp.dbaccess;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.sampleapp.provider.DataEntryColumns;

/**
 * Created by ahetawal on 12/31/15.
 */
public class MySQLiteOpenHelper extends SQLiteOpenHelper {

    public static final String DATABASE_FILE_NAME = "sample.db";
    private static final int DATABASE_VERSION = 1;
    private static MySQLiteOpenHelper sInstance;
    private final Context mCxt;


    public static final String SQL_CREATE_TABLE_DATA_ENTRY = "CREATE TABLE IF NOT EXISTS "
            + DataEntryColumns.TABLE_NAME + " ( "
            + DataEntryColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + DataEntryColumns.NAME + " TEXT NOT NULL );";



    public MySQLiteOpenHelper(Context context) {
        super(context, DATABASE_FILE_NAME, null, DATABASE_VERSION);
        this.mCxt = context;
    }


    public static MySQLiteOpenHelper getInstance(Context ctx) {
        /**
         * use the application context as suggested by CommonsWare.
         * this will ensure that you dont accidentally leak an Activitys
         * context (see this article for more information:
         * http://android-developers.blogspot.nl/2009/01/avoiding-memory-leaks.html)
         */
        if (sInstance == null) {
            //NOTE: Use the application context instead of activity context to prevent leak
            sInstance = new MySQLiteOpenHelper(ctx.getApplicationContext());
        }
        return sInstance;
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_DATA_ENTRY);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
