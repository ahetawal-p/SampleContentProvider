package com.sampleapp.dbaccess;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.sampleapp.application.MyPocApplication;

/**
 * Created by ahetawal on 1/1/16.
 * All the below overriden methods are called on UI Thread once the query is complete
 */
public class MyAsyncQueryHandler extends AsyncQueryHandler {

    public MyAsyncQueryHandler(ContentResolver cr) {
        super(cr);
    }

    @Override
    protected void onQueryComplete(int token, Object cookie, Cursor mCursor) {
        Log.i(MyPocApplication.LOG_TAG, "## OnQueryComplete for token ## : " + token);
        if (mCursor != null) {
            while (mCursor.moveToNext()) {
                String id = mCursor.getString(0);
                String name = mCursor.getString(1);
                Log.i(MyPocApplication.LOG_TAG, "Entry is " + id + ": " + name);
            }
            mCursor.close();
        }
    }

    @Override
    protected void onInsertComplete(int token, Object cookie, Uri uri) {
        Log.i(MyPocApplication.LOG_TAG, "## onInsertComplete for token ## : " + token);
        Log.i(MyPocApplication.LOG_TAG, "Inserted URI: " + uri.toString());

    }

    @Override
    protected void onUpdateComplete(int token, Object cookie, int result) {
        Log.i(MyPocApplication.LOG_TAG, "## onUpdateComplete for token ## : " + token);
        Log.i(MyPocApplication.LOG_TAG, "Data Updated rows ..." + result);
    }

    @Override
    protected void onDeleteComplete(int token, Object cookie, int result) {
        Log.i(MyPocApplication.LOG_TAG, "## onDeleteComplete for token ## : " + token);
        Log.i(MyPocApplication.LOG_TAG, "Data Deleted number of rows ..." + result);

    }

}
