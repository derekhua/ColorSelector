package com.example.derek.colorselector.namedcolors;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.derek.colorselector.R;
import com.example.derek.colorselector.contentprovider.ColorContentProvider;
import com.example.derek.colorselector.db.ColorTable;

/**
 * Created by Derek on 4/18/15.
 */
public class NamedColorsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private ColorCursorAdapter mAdapter;

    Cursor mCursor = null;

    FragmentManager fm = getFragmentManager();

    public final String NAMEDCOLORS_FRAGMENT = "namedColorsFragment";

    private float mLeftHue;
    private float mRightHue;
    private float mSaturation;
    private float mValue;

    ListView mListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bundle = this.getArguments();
        mLeftHue = bundle.getFloat("lefthue");
        mRightHue = bundle.getFloat("rightHue");
        mSaturation = bundle.getFloat("saturation");
        mValue = bundle.getFloat("value");

        // use this layout
        return inflater.inflate(R.layout.namedcolors_view, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        mListView = (ListView) getActivity().findViewById(R.id.namedcolors_list);

        fillData();

        mListView.setOnItemLongClickListener( new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int pos, long id) {
//
//                if (ColorCursorAdapter.isTaskCompleted(view))
//                    deleteTask( id );
//                else
//                    updateTask( id );

                return true; // consume the click event
            }
        });
    }

    // populate the list with task data from the db
    private void fillData() {
        getLoaderManager().initLoader(0, null, this);

//            if (mCursor != null) {
                mAdapter = new ColorCursorAdapter(getActivity(), // context
                        null, // cursor
                        0       // flags
                );
                mListView.setAdapter(mAdapter);
//            }
//            else {
//                Toast.makeText(getActivity(), "no cursor!", Toast.LENGTH_SHORT).show();
//            }


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

        CursorLoader cursorLoader
                = new CursorLoader( getActivity(),
                ColorContentProvider.CONTENT_URI,
                ColorCursorAdapter.PROJECTION,
//                "hue >=? AND hue <=? AND SATURATION =? AND value =?",
                null,
//                new String[] {Float.toString(mLeftHue), Float.toString(mRightHue), Float.toString(mSaturation), Float.toString(mValue)},
                null,
                ColorTable.COLUMN_NAME);

        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
//        mCursor = data;
          mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }
}
