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
public class SaturationFragment extends Fragment {

    FragmentManager fm = getFragmentManager();

    public final String VAL_FRAGMENT = "valueFragment";

    private ArrayList<Integer[]> mColorList = null;

    private ColorAdapter mAdapter = null;

    private int mPosition;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bundle = this.getArguments();
        mPosition = bundle.getInt("position");
        // use this layout
        return inflater.inflate(R.layout.saturation_view, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        // position 0: 345° to 15°
        // position 1: 15° to 45° ...

        float hue = 345;
        hue += (30 * mPosition);
        hue %= 360;

        // get the hsv array
        mColorList = ColorCreator.getColorListSaturation(hue, 1, 1, -0.1f, 11);
        mAdapter = new ColorAdapter(getActivity(), mColorList);

        // get the list view and set the adapter
        final ListView listView = (ListView) getActivity().findViewById(R.id.saturation_list);
        listView.setAdapter(mAdapter);

        // create the button listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {

                // Create fragment
                ValueFragment newFragment = new ValueFragment();

                // use this to send info
                Bundle args = new Bundle();

                args.putInt("hueposition", mPosition);
                args.putInt("saturationposition", position);
                newFragment.setArguments(args);

                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack so the user can navigate back
                transaction.replace(R.id.fragment_container, newFragment, VAL_FRAGMENT);
                transaction.addToBackStack("saturationFragment");

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
