package com.example.derek.colorselector.namedcolors;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.derek.colorselector.R;
import com.example.derek.colorselector.contentprovider.ColorContentProvider;
import com.example.derek.colorselector.db.ColorTable;

/**
 * Created by Derek on 4/18/15.
 */
public class NamedColorsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private ColorCursorAdapter mAdapter;

    private float mLeftHue;
    private float mRightHue;
    private float mSaturation;
    private float mValue;

    private float mLeftSaturation;
    private float mRightValue;
    private float mRightSaturation;
    private float mLeftValue;

    ListView mListView;

    private String ORDER_BY_PREF = "orderbypref";
    private String ORDER_BY = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bundle = this.getArguments();
        mLeftHue = bundle.getFloat("leftHue");
        mRightHue = bundle.getFloat("rightHue");
        mSaturation = bundle.getFloat("saturation");
        mValue = bundle.getFloat("value");

        // to match database
        mSaturation /= 100f;
        mValue /= 100f;

        // create the range
        float delta = 0.05f;
        mLeftSaturation = mSaturation - delta;
        mRightSaturation = mSaturation + delta;
        mLeftValue = mValue - delta;
        mRightValue = mValue + delta;

        // use this layout
        return inflater.inflate(R.layout.namedcolors_view, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        // using previous count by getPreference
        ORDER_BY = getActivity().getPreferences(Context.MODE_PRIVATE).getString(ORDER_BY_PREF, null);

        Button button = (Button) getActivity().findViewById(R.id.sorting_order_button);

        mListView = (ListView) getActivity().findViewById(R.id.namedcolors_list);

        fillData();

        // shows the AlertDialog
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] order = {
                                  "Hue, Saturation, Value", "Hue, Value, Saturation",
                                  "Saturation, Hue, Value", "Saturation, Value, Hue",
                                  "Value, Hue, Saturation", "Value, Saturation, Hue",
                                  "Name"};

                new AlertDialog.Builder(getActivity())
                        .setTitle("Configure Sorting Order")
                        .setSingleChoiceItems(order, -1,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                    ORDER_BY = order[which];
                                    ORDER_BY = ORDER_BY.toLowerCase();
                                    Toast.makeText(getActivity(), "Sort: " + ORDER_BY, Toast.LENGTH_SHORT).show();
                                    updateAfterSort();
                            }
                        })
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        //.setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });

        // shows the Toast of info
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {
                // get a cursor at this position
                Cursor entry = (Cursor) mListView.getItemAtPosition(position);

                // retrieve values
                String name = entry.getString(ColorCursorAdapter.NAME);
                String hue = entry.getString(ColorCursorAdapter.HUE);
                String saturation = entry.getString(ColorCursorAdapter.SATURATION);
                String value = entry.getString(ColorCursorAdapter.VALUE);

                // make Toast
                Toast.makeText(getActivity(), name + ":\n" + "Hue: " + hue + "\nSaturation: " +
                        saturation + "\nValue: " + value, Toast.LENGTH_SHORT).show();
            }
        });
    }

    // populate the list with task data from the db
    private void fillData() {
        getLoaderManager().initLoader(0, null, this);

        mAdapter = new ColorCursorAdapter(getActivity(), // context
                                            null,        // cursor
                                            0            // flags
        );
        mListView.setAdapter(mAdapter);
    }

    private void updateAfterSort() {
        getLoaderManager().restartLoader(0, null, this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // retain this Fragment across configuration changes
        setRetainInstance(true);
    }

    /****************************************
     ** LoaderManager.LoaderCallbacks
     *****************************************/

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader cursorLoader;

        // to resolve the wrap around i.e. 345 - 15
        if(mRightHue < mLeftHue) {
            cursorLoader
                    = new CursorLoader(getActivity(),
                    ColorContentProvider.CONTENT_URI,
                    ColorCursorAdapter.PROJECTION,
                    // selection
                    "((" + ColorTable.COLUMN_HUE + " >=? AND " + ColorTable.COLUMN_HUE + " <=?) OR " +
                            "(" + ColorTable.COLUMN_HUE + " >=? AND " + ColorTable.COLUMN_HUE + " <=?)) AND " +
                            "(" + ColorTable.COLUMN_SATURATION + " >=? AND " + ColorTable.COLUMN_SATURATION + " <=?) AND " +
                            "(" + ColorTable.COLUMN_VALUE + " >=? AND " + ColorTable.COLUMN_VALUE + " <=?)",
                    // args
                    new String[]{Float.toString(mLeftHue), "360.0",
                            "0.0", Float.toString(mRightHue),
                            Float.toString(mLeftSaturation), Float.toString(mRightSaturation),
                            Float.toString(mLeftValue), Float.toString(mRightValue)},
                    // order-by
                    ORDER_BY);
        } else {
            cursorLoader
                    = new CursorLoader(getActivity(),
                    ColorContentProvider.CONTENT_URI,
                    ColorCursorAdapter.PROJECTION,
                    // selection
                    "(" + ColorTable.COLUMN_HUE + " >=? AND " + ColorTable.COLUMN_HUE + " <=?) AND " +
                            "(" + ColorTable.COLUMN_SATURATION + " >=? AND " + ColorTable.COLUMN_SATURATION + " <=?) AND " +
                            "(" + ColorTable.COLUMN_VALUE + " >=? AND " + ColorTable.COLUMN_VALUE + " <=?)",
                    // args
                    new String[]{Float.toString(mLeftHue), Float.toString(mRightHue),
                            Float.toString(mLeftSaturation), Float.toString(mRightSaturation),
                            Float.toString(mLeftValue), Float.toString(mRightValue)},
                    // order-by
                    ORDER_BY);
        }
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
          mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    // using this to saved preferences
    @Override
    public void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = getActivity().getPreferences(Context.MODE_PRIVATE).edit();
        // storing mCurrentCount
        editor.putString(ORDER_BY_PREF, ORDER_BY);
        editor.commit();
    }
}
