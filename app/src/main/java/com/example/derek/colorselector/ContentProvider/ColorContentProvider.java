package com.example.derek.colorselector.ContentProvider;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import com.example.derek.colorselector.db.ColorDBHelper;
import com.example.derek.colorselector.db.ColorTable;

/**
 * Created by Derek on 4/17/15.
 */
public class ColorContentProvider extends ContentProvider {

    // database
    private ColorDBHelper database;

    private static final String AUTHORITY
            = "com.example.derek.colorselector.provider";

    private static final String BASE_PATH
            = "colors";

    public static final String CONTENT_TYPE
            = ContentResolver.CURSOR_DIR_BASE_TYPE + "/colors";

    public static final String CONTENT_ITEM_TYPE
            = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/color";

    public static final String CONTENT_URI_PREFIX
            = "content://" + AUTHORITY + "/" + BASE_PATH + "/";


    public static final Uri CONTENT_URI
            = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);

    // URI Matcher
    private static final UriMatcher sURIMatcher = new UriMatcher( UriMatcher.NO_MATCH );
    private static final int TASKS = 1;
    private static final int TASK_ID = 2;
    static {
        sURIMatcher.addURI(AUTHORITY, BASE_PATH, TASKS);
        sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/#", TASK_ID);
    }


    @Override
    public boolean onCreate() {
        database = new ColorDBHelper(getContext());
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        // check if the caller has requested a column which does not exists
        ColorTable.validateProjection(projection);

        // Using SQLiteQueryBuilder instead of query() method
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        queryBuilder.setTables( ColorTable.TABLE_COLOR );

        switch ( sURIMatcher.match(uri) ) {
            case TASKS:
                break;
            case TASK_ID:
                // add the task ID to the original query
                queryBuilder.appendWhere( ColorTable.COLUMN_ID + "=" + uri.getLastPathSegment() );
                break;
            default:
                throw new IllegalArgumentException("Invalid URI: " + uri);
        }

        SQLiteDatabase db = database.getWritableDatabase();
        Cursor cursor = queryBuilder.query( db, projection, selection,
                selectionArgs, null, null, sortOrder);

        // notify listeners
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

}
