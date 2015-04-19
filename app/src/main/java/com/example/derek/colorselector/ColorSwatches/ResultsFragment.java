package com.example.derek.colorselector.ColorSwatches;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.derek.colorselector.NamedColors.NamedColorsFragment;
import com.example.derek.colorselector.R;

/**
 * Created by Derek on 4/17/15.
 */
public class ResultsFragment extends Fragment {

    FragmentManager fm = getFragmentManager();

    private int mPositionHue;
    private int mPositionSat;
    private int mPositionVal;

    private TextView hueResult;
    private TextView satResult;
    private TextView valResult;

    private float leftHue;
    private float rightHue;
    private float saturation;
    private float value;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bundle = this.getArguments();
        mPositionHue = bundle.getInt("hueposition");
        mPositionSat = bundle.getInt("saturationposition");
        mPositionVal = bundle.getInt("valueposition");
        // use this layout
        return inflater.inflate(R.layout.results_view, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        Button button = (Button) getActivity().findViewById(R.id.next_button);
        // get the views
        hueResult = (TextView) getActivity().findViewById(R.id.hue_text);
        satResult = (TextView) getActivity().findViewById(R.id.sat_text);
        valResult = (TextView) getActivity().findViewById(R.id.val_text);

        // calculate values to put in the views
        leftHue = 345 + (mPositionHue * 30);
        leftHue %= 360;
        rightHue = leftHue + 30;
        rightHue %= 360;

        saturation = 100 - (mPositionSat * 10);
        value = 100 - (mPositionVal * 10);

        // set the views
        hueResult.setText("The hue ranges from " + leftHue + "° to " + rightHue + "°.");
        satResult.setText("The saturation is at " + saturation + "%.");
        valResult.setText("The value is at " + value + "%.");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create fragment
                NamedColorsFragment newFragment = new NamedColorsFragment();

                // use this to send info
                Bundle args = new Bundle();

                args.putFloat("leftHue", leftHue);
                args.putFloat("rightHue", rightHue);
                args.putFloat("saturation", saturation);
                args.putFloat("value", value);
                newFragment.setArguments(args);

                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack so the user can navigate back
                transaction.replace(R.id.fragment_container, newFragment);
                transaction.addToBackStack("resultsFragment");

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
