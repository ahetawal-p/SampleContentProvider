package com.sampleapp.provider;

import android.net.Uri;

/**
 * Created by ahetawal on 12/31/15.
 */
public class DataEntryColumns {

    public static final String TABLE_NAME = "dataentry";
    public static final Uri CONTENT_URI = Uri.parse(MyContentProvider.CONTENT_URI_BASE + "/" + TABLE_NAME);

    /**
     * Primary key.
     */
    public static final String _ID = "_ID";

    /**
     * First name of this person. For instance, John.
     */
    public static final String NAME = "name";



    public static final String[] ALL_COLUMNS = new String[] {
            _ID,
            NAME

    };



}
