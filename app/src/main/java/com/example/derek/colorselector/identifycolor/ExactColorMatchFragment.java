package com.example.derek.colorselector.identifycolor;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.derek.colorselector.R;
import com.example.derek.colorselector.contentprovider.ColorContentProvider;
import com.example.derek.colorselector.db.ColorTable;
import com.example.derek.colorselector.namedcolors.ColorCursorAdapter;

/**
 * Created by Derek on 4/24/15.
 */

// a dumbed down version of NamedColorFragment

public class ExactColorMatchFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private ColorCursorAdapter mAdapter;

    ListView mListView;
    TextView textView;

    private float mHue;
    private float mSaturation;
    private float mValue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bundle = this.getArguments();
        mHue = bundle.getFloat("hue");
        mSaturation = bundle.getFloat("saturation");
        mValue = bundle.getFloat("value");

        // use this layout
        return inflater.inflate(R.layout.exact_color_match_layout, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        mListView = (ListView) getActivity().findViewById(R.id.exact_match_list);

        fillData();

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
                Toast.makeText(getActivity(), name + ":\n" + getResources().getString(R.string.hue) + ": " + hue + "\n" +
                        getResources().getString(R.string.saturation) + ": " +
                        saturation + "\n" +
                        getResources().getString(R.string.value) + ": " + value, Toast.LENGTH_SHORT).show();
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

        cursorLoader
                = new CursorLoader(getActivity(),
                ColorContentProvider.CONTENT_URI,
                ColorCursorAdapter.PROJECTION,
                // selection
                ColorTable.COLUMN_HUE + " =? AND " + ColorTable.COLUMN_SATURATION + " =? AND " +
                ColorTable.COLUMN_VALUE + " =?",
                // args
                new String[]{
                        Float.toString(mHue), Float.toString(mSaturation),
                        Float.toString(mValue)},
                // order-by
                null);

        return cursorLoader;
    }

    // swaps the cursor with the loaded cursor
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);

        if(mListView.getCount() == 0) {
            textView = new TextView(getActivity());
            textView.setText("No match found");
            textView.setTextColor(Color.WHITE);
            mListView.addHeaderView(textView);
        } else {
            textView = new TextView(getActivity());
            textView.setText("Exact match found: ");
            textView.setTextColor(Color.WHITE);
            mListView.addHeaderView(textView);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }
}