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
 * Created by Derek on 4/17/15.
 */
public class ValueFragment extends Fragment{

    FragmentManager fm = getFragmentManager();

    public final String RES_FRAGMENT = "resultsFragment";

    private ArrayList<Integer[]> mColorList = null;

    private ColorAdapter mAdapter = null;

    // holds the positions chosen
    private int mPositionHue;
    private int mPositionSat;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bundle = this.getArguments();
        mPositionHue = bundle.getInt("hueposition");
        mPositionSat = bundle.getInt("saturationposition");
        // use this layout
        return inflater.inflate(R.layout.value_view, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        // position 0: 345° to 15°
        // position 1: 15° to 45° ...

        float hue = 345;
        hue += (30 * mPositionHue);
        hue %= 360;

        float sat = 1f;
        sat -= (0.1f * mPositionSat);

        // get the hsv array
        mColorList = ColorCreator.getColorListValue(hue, sat, 1, -0.1f, 11);
        mAdapter = new ColorAdapter(getActivity(), mColorList);

        // get the list view and set the adapter
        final ListView listView = (ListView) getActivity().findViewById(R.id.value_list);
        listView.setAdapter(mAdapter);

        // create the button listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {

                // Create fragment
                ResultsFragment newFragment = new ResultsFragment();

                // use this to send info
                Bundle args = new Bundle();

                args.putInt("hueposition", mPositionHue);
                args.putInt("saturationposition", mPositionSat);
                args.putInt("valueposition", position);
                newFragment.setArguments(args);

                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack so the user can navigate back
                transaction.replace(R.id.fragment_container, newFragment, RES_FRAGMENT);
                transaction.addToBackStack("valueFragment");

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
