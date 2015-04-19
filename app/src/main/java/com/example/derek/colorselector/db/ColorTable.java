package com.example.derek.colorselector.db;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.Arrays;
import java.util.HashSet;

/**
 * Created by Derek on 4/18/15.
 */
public class ColorTable {

    // Column names
    public static final String TABLE_COLOR = "colors";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_HUE = "hue";
    public static final String COLUMN_SATURATION = "saturation";
    public static final String COLUMN_VALUE = "value";

    // SQL statement to create the table
    private static final String DATABASE_CREATE = "create table "
            + TABLE_COLOR
            + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_NAME + " text not null, "
            + COLUMN_HUE + " real not null,"
            + COLUMN_SATURATION + " real not null, "
            + COLUMN_VALUE + " real not null"
            + ");";

    private static HashSet<String> VALID_COLUMN_NAMES;

    public static void onCreate( SQLiteDatabase database ) {

        database.execSQL( DATABASE_CREATE );
    }

    public static void onUpgrade( SQLiteDatabase database,
                                  int oldVersion,
                                  int newVersion) {
        Log.d(ColorTable.class.getName(),
                "Upgrading database from version "
                        + oldVersion + " to " + newVersion
                        + ", which destroyed all existing data");

        database.execSQL( "DROP TABLE IF EXISTS " + TABLE_COLOR );
        onCreate( database );

        Log.d( "TableTask.onUpgrade()", "complete");
    }

    static {
        String[] validNames = {
                TABLE_COLOR,
                COLUMN_ID,
                COLUMN_NAME,
                COLUMN_HUE,
                COLUMN_SATURATION,
                COLUMN_VALUE
        };

        VALID_COLUMN_NAMES = new HashSet<String>(Arrays.asList(validNames));
    }

    public static void validateProjection(String[] projection) {

        if (projection != null) {
            HashSet<String> requestedColumns = new HashSet<String>(Arrays.asList(projection));

            // check if all columns which are requested are available
            if ( !VALID_COLUMN_NAMES.containsAll( requestedColumns ) ) {
                throw new IllegalArgumentException("Unknown columns in projection");
            }
        }
    }
}
