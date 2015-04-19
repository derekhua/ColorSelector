package com.example.derek.colorselector.colorswatches;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.derek.colorselector.R;

import java.util.ArrayList;

/**
 * Created by Derek on 4/16/15.
 */
// contains the color swatches
public class HueFragment extends Fragment {

    FragmentManager fm = getFragmentManager();

    public final String SAT_FRAGMENT = "saturationFragment";

    // holds the color pairs
    private ArrayList<Integer[]> mColorList = null;

    private ColorAdapter mAdapter = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // use this layout
        return inflater.inflate(R.layout.hue_view, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        // get the hsv array
        mColorList = ColorCreator.getColorListHue(345, 1, 1, 30, 11);
        mAdapter = new ColorAdapter(getActivity(), mColorList);

        // get the list view and set the adapter
        final ListView listView = (ListView) getActivity().findViewById(R.id.hue_list);
        listView.setAdapter(mAdapter);

        // create the button listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {

                // Create fragment
                SaturationFragment newFragment = new SaturationFragment();

                // use this to send info
                Bundle args = new Bundle();

                args.putInt("position", position);
                newFragment.setArguments(args);

                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack so the user can navigate back
                transaction.replace(R.id.fragment_container, newFragment, SAT_FRAGMENT);
                transaction.addToBackStack("hueFragment");

                // Commit the transaction
                transaction.commit();
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // retain this Fragment across configuration changes
        setRetainInstance(true);
    }

}