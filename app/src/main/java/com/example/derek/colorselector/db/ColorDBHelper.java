package com.example.derek.colorselector.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Derek on 4/18/15.
 */
public class ColorDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "colors.db";
    private static final int DATABASE_VERSION = 1;

    public ColorDBHelper( Context context ) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate( SQLiteDatabase database ) {
        ColorTable.onCreate(database);
    }

    @Override
    public void onUpgrade( SQLiteDatabase database,
                           int oldVersion,
                           int newVersion) {
        ColorTable.onUpgrade( database, oldVersion, newVersion );
    }
}
