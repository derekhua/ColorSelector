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

    private float mLeftSaturation;
    private float mRightValue;
    private float mRightSaturation;
    private float mLeftValue;

    ListView mListView;

    private float mSaturation;
    private float mSaturationDelta;
    private float mValue;
    private float mValueDelta;

    // sort order
    private String[] SORT_ORDERS = {
            "Hue, Saturation, Value", "Hue, Value, Saturation",
            "Saturation, Hue, Value", "Saturation, Value, Hue",
            "Value, Hue, Saturation", "Value, Saturation, Hue",
            "Name"};
    private String SORT_ORDER_NUM = "sortordernum";
    private int sortOrderNum;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bundle = this.getArguments();
        mLeftHue = bundle.getFloat("leftHue");
        mRightHue = bundle.getFloat("rightHue");
        mSaturation = bundle.getFloat("saturation");
        mSaturationDelta = bundle.getFloat("saturationdelta");
        mValue = bundle.getFloat("value");
        mValueDelta = bundle.getFloat("valuedelta");

//        // to match database
//        mSaturation /= 100f;
//        mValue /= 100f;

        // create the range
        mLeftSaturation = mSaturation - mSaturationDelta/2f;
        mRightSaturation = mSaturation + mSaturationDelta/2f;
        mLeftValue = mValue - mValueDelta/2f;
        mRightValue = mValue + mValueDelta/2f;

        // use this layout
        return inflater.inflate(R.layout.namedcolors_view, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        // using previous count by getPreference
        sortOrderNum = getActivity().getPreferences(Context.MODE_PRIVATE).getInt(SORT_ORDER_NUM, 0);

        Button button = (Button) getActivity().findViewById(R.id.sorting_order_button);

        mListView = (ListView) getActivity().findViewById(R.id.namedcolors_list);

        fillData();

        // shows the AlertDialog
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                alertDialog.setTitle("Sort by: ");
                alertDialog.setSingleChoiceItems(SORT_ORDERS, sortOrderNum,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                    sortOrderNum = which;
                                    Toast.makeText(getActivity(), "Sort by: " + SORT_ORDERS[which], Toast.LENGTH_SHORT).show();
                                    updateAfterSort();
                            }
                        });
                alertDialog.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                alertDialog.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                //.setIcon(android.R.drawable.ic_dialog_alert)
                alertDialog.show();
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

    // restarts the loader
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
                    SORT_ORDERS[sortOrderNum]);
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
                    SORT_ORDERS[sortOrderNum]);
        }
        return cursorLoader;
    }

    // swaps the cursor with the loaded cursor
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
        // storing order preference
        editor.putInt(SORT_ORDER_NUM, sortOrderNum);
        editor.apply();
    }
}
