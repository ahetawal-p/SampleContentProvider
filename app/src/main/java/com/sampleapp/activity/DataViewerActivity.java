package com.sampleapp.activity;

import android.app.DialogFragment;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.sampleapp.R;
import com.sampleapp.adapter.DataViewerAdapter;
import com.sampleapp.application.MyPocApplication;
import com.sampleapp.provider.DataEntryColumns;

import java.util.Date;

public class DataViewerActivity extends AppCompatActivity implements DataEntryFragment.DataEntryListener, LoaderManager.LoaderCallbacks<Cursor>{


    private DataViewerAdapter mAdapter;
    public static final int DATA_LOADER_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_viewer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new DataEntryFragment();
                newFragment.show(getFragmentManager(), "dataentry");

            }
        });



        RecyclerView dataViewer = (RecyclerView)findViewById(R.id.viewer);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        dataViewer.setLayoutManager(llm);
        mAdapter = new DataViewerAdapter();
        dataViewer.setAdapter(mAdapter);

        // Setup the loader
        getLoaderManager().initLoader(DATA_LOADER_ID, null, this);


        tempDataDisplay();
        tempDataUpdate();
    }


    @Override
    public void onDialogPositiveClick(String name) {
        Log.i(MyPocApplication.LOG_TAG, "Received the data: " + name);
        ContentValues mNewValues = new ContentValues();
        mNewValues.put(DataEntryColumns.NAME, name);
        Uri mNewUri = getContentResolver().insert(DataEntryColumns.CONTENT_URI, mNewValues);

        Log.i(MyPocApplication.LOG_TAG, "Inserted URI: " + mNewUri.toString());
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        Log.i(MyPocApplication.LOG_TAG, "Creating loader with id: " + id);

        switch (id){

            case DATA_LOADER_ID :
                    return new CursorLoader(this, DataEntryColumns.CONTENT_URI,DataEntryColumns.ALL_COLUMNS,null,null,null);

            default :
                return null;
        }

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.i(MyPocApplication.LOG_TAG, "Loader load finished...");
        if (mAdapter != null) {
            mAdapter.swapCursor(data);
        }


    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.i(MyPocApplication.LOG_TAG, "Loader Reset...");
        if (mAdapter != null) {
            mAdapter.swapCursor(null);
        }
    }


    private void tempDataDisplay() {
        FloatingActionButton tempShow = (FloatingActionButton) findViewById(R.id.tempshow);
        tempShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor mCursor = getContentResolver().query(DataEntryColumns.CONTENT_URI, DataEntryColumns.ALL_COLUMNS, null, null, null);
                if (mCursor != null) {
                    while (mCursor.moveToNext()) {
                        String id = mCursor.getString(0);
                        String name = mCursor.getString(1);
                        Log.i(MyPocApplication.LOG_TAG, "Entry is " + id + ": " + name);
                    }
                    mCursor.close();

                }
            }
        });
    }



    private void tempDataUpdate() {

        FloatingActionButton tempUpdate = (FloatingActionButton) findViewById(R.id.tempupdate);
        tempUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Sample UPDATE call
                ContentValues mNewValues = new ContentValues();
                mNewValues.put(DataEntryColumns.NAME, "my change at: " + new Date());
                String selectionClause = DataEntryColumns._ID + " = ?";
                String[] mSelectionArgs = {"2"};
                int rowNums = getContentResolver().update(DataEntryColumns.CONTENT_URI, mNewValues, selectionClause, mSelectionArgs);
                Log.i(MyPocApplication.LOG_TAG, "Data Updated rows ..." + rowNums);

                // Sample DELETE call
                String delSelectionClause = DataEntryColumns.NAME + " = ?";
                String[] delSelectionArgs = {"test"};
                rowNums = getContentResolver().delete(DataEntryColumns.CONTENT_URI, delSelectionClause, delSelectionArgs);
                Log.i(MyPocApplication.LOG_TAG, "Data Deleted number of rows ..." + rowNums);


            }
        });
    }
}
