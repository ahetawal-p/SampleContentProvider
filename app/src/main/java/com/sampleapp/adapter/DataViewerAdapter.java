package com.sampleapp.adapter;

import android.database.Cursor;
import android.database.DataSetObserver;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sampleapp.R;
import com.sampleapp.application.MyPocApplication;

/**
 * Created by ahetawal on 1/1/16.
 */
public class DataViewerAdapter extends RecyclerView.Adapter<DataViewerAdapter.DataViewerHolder> {


    private Cursor mCursor;
    private boolean mDatasetValid;
    private DataViewSetObserver mObserver;

    public DataViewerAdapter(){
        mObserver = new DataViewSetObserver();
    }

    @Override
    public DataViewerHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_data_viewer, viewGroup, false);
        DataViewerHolder viewholder = new DataViewerHolder(v);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(DataViewerHolder holder, int position) {
        if(mDatasetValid){
            if(mCursor.moveToPosition(position)){
                holder.dataIdTextView.setText(mCursor.getString(0));
                holder.dataNameTextView.setText(mCursor.getString(1).trim());
            }else {
                Log.e(MyPocApplication.LOG_TAG, "Can't bind view; Cursor is not valid.");
            }
        }else {
            Log.e(MyPocApplication.LOG_TAG, "Dataset is not valid.");
        }
    }

    @Override
    public int getItemCount() {
        if (mDatasetValid && mCursor != null) {
            return mCursor.getCount();
        }
        Log.e(MyPocApplication.LOG_TAG, "Dataset is not valid.");
        return 0;
    }


    public void swapCursor(Cursor cursor) {
        // Sanity check.
        if (cursor == mCursor) {
            return;
        }

        // Before getting rid of the old cursor, disassociate it from the Observer.
        final Cursor oldCursor = mCursor;
        if (oldCursor != null && mObserver != null) {
            oldCursor.unregisterDataSetObserver(mObserver);
        }

        mCursor = cursor;
        if (mCursor != null) {
            // Attempt to associate the new Cursor with the Observer.
            if (mObserver != null) {
                mCursor.registerDataSetObserver(mObserver);
            }
            mDatasetValid = true;
        } else {
            mDatasetValid = false;
        }
        Log.i(MyPocApplication.LOG_TAG, "SwapCursor Notifier");
        notifyDataSetChanged();
    }


    public static class DataViewerHolder extends RecyclerView.ViewHolder {

        TextView dataIdTextView;
        TextView dataNameTextView;

        DataViewerHolder(View itemView) {
            super(itemView);
            dataIdTextView = (TextView)itemView.findViewById(R.id.dataId);
            dataNameTextView = (TextView)itemView.findViewById(R.id.dataName);
        }

    }


    private final class DataViewSetObserver extends DataSetObserver {

        @Override
        public void onChanged() {
            super.onChanged();

            mDatasetValid = true;
            Log.i(MyPocApplication.LOG_TAG, "DataViewSetObserver CHANGED");

            notifyDataSetChanged();
        }

        /**
         * //getLoaderManager().getLoader(1).reset(); triggers this method
         */
        @Override
        public void onInvalidated() {
            super.onInvalidated();

            mDatasetValid = false;
            Log.i(MyPocApplication.LOG_TAG, "DataViewSetObserver INVALIDATED");
            notifyDataSetChanged();
        }
    }
}
