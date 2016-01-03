package com.sampleapp.activity;

import android.app.DialogFragment;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
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
import com.sampleapp.dbaccess.MyAsyncQueryHandler;
import com.sampleapp.provider.DataEntryColumns;

import java.util.Date;

public class DataViewerActivity extends AppCompatActivity implements DataEntryFragment.DataEntryListener, LoaderManager.LoaderCallbacks<Cursor>{


    private DataViewerAdapter mAdapter;
    public static final int DATA_LOADER_ID = 1;

    //These tokens are used by AsyncHandler for making request, can be used with cancelOperation(tokenId)
    public static final int ASYNC_QUERY_TOKEN = 1;
    public static final int ASYNC_INSERT_TOKEN = 2;
    public static final int ASYNC_UPDATE_TOKEN = 3;
    public static final int ASYNC_DELETE_TOKEN = 4;

    private MyAsyncQueryHandler asyncQuery;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // init async handler
        asyncQuery = new MyAsyncQueryHandler(getContentResolver());

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

        setUpTempDataDisplay();
        setUpTempDataUpdate_Del();
    }


    @Override
    public void onDialogSaveButtonClick(String name) {
        Log.i(MyPocApplication.LOG_TAG, "Received the data: " + name);
        ContentValues mNewValues = new ContentValues();
        mNewValues.put(DataEntryColumns.NAME, name);
        asyncQuery.startInsert(ASYNC_INSERT_TOKEN, null, DataEntryColumns.CONTENT_URI, mNewValues);
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

    /**
     * Method used for a sample use of direct query() call through contentresolver.
     * Although we use loaders in this app for the making the query calls for populating the recyclerview
     */
    private void setUpTempDataDisplay() {
        FloatingActionButton tempShow = (FloatingActionButton) findViewById(R.id.tempshow);
        tempShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                asyncQuery.startQuery(ASYNC_QUERY_TOKEN, null, DataEntryColumns.CONTENT_URI, DataEntryColumns.ALL_COLUMNS, null, null, null);
                // Note: Look at the AsyncHandler onQueryComplete for the return result of this query
            }
        });
    }


    /**
     * Method used for performing a sample UPDATE and a DELETE on the content provider dataset
     * This caused the loader to update the recyclerview automatically with the updates
     */
    private void setUpTempDataUpdate_Del() {

        FloatingActionButton tempUpdate = (FloatingActionButton) findViewById(R.id.tempupdate);
        tempUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Sample UPDATE call
                ContentValues mNewValues = new ContentValues();
                mNewValues.put(DataEntryColumns.NAME, "my change at: " + new Date());
                String selectionClause = DataEntryColumns._ID + " = ?";
                String[] mSelectionArgs = {"2"};
                asyncQuery.startUpdate(ASYNC_UPDATE_TOKEN, null, DataEntryColumns.CONTENT_URI, mNewValues, selectionClause, mSelectionArgs);

                // Sample DELETE call. Deletes any rows with name containing "test"
                String delSelectionClause = DataEntryColumns.NAME + " LIKE ?";
                String[] delSelectionArgs = {"%test%"};
                asyncQuery.startDelete(ASYNC_DELETE_TOKEN, null, DataEntryColumns.CONTENT_URI, delSelectionClause, delSelectionArgs);

            }
        });
    }
}
