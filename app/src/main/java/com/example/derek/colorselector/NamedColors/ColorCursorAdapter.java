package com.example.derek.colorselector.NamedColors;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.derek.colorselector.R;
import com.example.derek.colorselector.db.ColorTable;

/**
 * Created by Derek on 4/18/15.
 */
public class ColorCursorAdapter extends CursorAdapter {

    // Fields from the database (projection)
    // Must include the _id column for the adapter to work
    static private final int ID = 0;
    static private final int NAME = 1;
    static private final int HUE = 2;
    static private final int SATURATION= 3;
    static private final int VALUE = 4;

    private LayoutInflater mInflater;

    static public final String[] PROJECTION
            = new String[] { ColorTable.COLUMN_ID,
            ColorTable.COLUMN_HUE,
            ColorTable.COLUMN_SATURATION,
            ColorTable.COLUMN_VALUE
    };

    static public final String ORDER_BY
            = ColorTable.COLUMN_NAME + "," +
            ColorTable.COLUMN_HUE + "," +
            ColorTable.COLUMN_SATURATION;


    public ColorCursorAdapter(Context context, Cursor cursor, int flag) {
        super(context, cursor, flag);

        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    static private final int TRUE = 1;

    static private class ViewHolder {
        TextView preview;
        TextView name;
        TextView hue;
        TextView saturation;
        TextView value;
    }



//    public ColorCursorAdapter( Context context, Cursor cursor, int flags ) {
//        super(context, cursor, flags);
//
//        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        View row = mInflater.inflate(R.layout.namedcolors_entry, parent, false);

        // cache the row's Views in a ViewHolder
        ViewHolder viewHolder = new ViewHolder();

        // set views
        viewHolder.preview = (TextView) row.findViewById(R.id.preview);
        viewHolder.name = (TextView) row.findViewById(R.id.color_name);
        viewHolder.hue = (TextView) row.findViewById(R.id.hue);
        viewHolder.saturation = (TextView) row.findViewById(R.id.saturation);
        viewHolder.value = (TextView) row.findViewById(R.id.value);

        row.setTag( viewHolder );

        return row;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ViewHolder viewHolder = updateViewHolderValues(view, cursor);
        decorateView( viewHolder, cursor );
    }

    private ViewHolder updateViewHolderValues(View view, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();

        viewHolder.name.setText(cursor.getString(NAME));
        viewHolder.hue.setText(Float.toString(cursor.getFloat(HUE)));
        viewHolder.saturation.setText(Float.toString(cursor.getFloat(SATURATION)));
        viewHolder.value.setText(Float.toString(cursor.getFloat(VALUE)));

        return viewHolder;
    }

    private void decorateView( ViewHolder color, Cursor cursor ) {
        color.preview.setBackgroundColor(Color.HSVToColor(new float[] {
                cursor.getFloat(HUE), cursor.getFloat(SATURATION), cursor.getFloat(VALUE)
        }));
    }
}
