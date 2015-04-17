package com.example.derek.colorselector;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Derek on 4/17/15.
 */
public class ResultsFragment extends Fragment {

    private int mPositionHue;
    private int mPositionSat;
    private int mPositionVal;

    private TextView hueResult;
    private TextView satResult;
    private TextView valResult;

    public ResultsFragment(int positionHue, int positionSat, int positionVal) {
        super();
        mPositionHue = positionHue;
        mPositionSat = positionSat;
        mPositionVal = positionVal;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // use this layout
        return inflater.inflate(R.layout.results_view, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        // get the views
        hueResult = (TextView) getActivity().findViewById(R.id.hue_text);
        satResult = (TextView) getActivity().findViewById(R.id.sat_text);
        valResult = (TextView) getActivity().findViewById(R.id.val_text);

        // calculate values to put in the views
        int leftHue = 345 + (mPositionHue * 30);
        leftHue %= 360;
        int rightHue = leftHue + 30;
        rightHue %= 360;

        int sat = 100 - (mPositionSat * 10);
        int val = 100 - (mPositionVal * 10);

        // set the views
        hueResult.setText("The hue ranges from " + leftHue + "° to " + rightHue + "°.");
        satResult.setText("The saturation is at " + sat + "%.");
        valResult.setText("The value is at " + val + "%.");

//        // create the button listener
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {
//
//                // Create fragment
//                ResultsFragment newFragment = new ResultsFragment(mPositionHue, mPositionSat, position);
//
////                // use this to send info
////                Bundle args = new Bundle();
////
////                args.putInt(SaturationFragment.ARG_POSITION, position);
////                newFragment.setArguments(args);
//
//                FragmentTransaction transaction = getFragmentManager().beginTransaction();
//
//                // Replace whatever is in the fragment_container view with this fragment,
//                // and add the transaction to the back stack so the user can navigate back
//                transaction.replace(R.id.fragment_container, newFragment);
//                transaction.addToBackStack("valueFragment");
//
//                // Commit the transaction
//                transaction.commit();
//            }
//        });
    }
}
