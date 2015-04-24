package com.example.derek.colorselector.colorswatches;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.derek.colorselector.R;
import com.example.derek.colorselector.namedcolors.NamedColorsFragment;

/**
 * Created by Derek on 4/17/15.
 */
public class ResultsFragment extends Fragment {

    private TextView hueResult;
    private TextView satResult;
    private TextView valResult;

    private float rightHue;

    private float mHue;
    private float mHueDelta;
    private float mSaturation;
    private float mSaturationDelta;
    private float mValue;
    private float mValueDelta;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bundle = this.getArguments();
        mHue = bundle.getFloat("hue");
        mHueDelta = bundle.getFloat("huedelta");
        mSaturation = bundle.getFloat("saturation");
        mSaturationDelta = bundle.getFloat("saturationdelta");
        mValue = bundle.getFloat("value");
        mValueDelta = bundle.getFloat("valuedelta");

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

        rightHue = (mHue + mHueDelta)%360;

        // in percent format
        float saturation = mSaturation * 100f;
        float value = mValue * 100f;

        // set the views
        hueResult.setText(getResources().getString(R.string.hue_ranges) + " " + mHue + getResources().getString(R.string.degree) + " "
                + getResources().getString(R.string.to) + " "
                + rightHue
                + getResources().getString(R.string.degree));

        satResult.setText(getResources().getString(R.string.saturation_is) + " " + saturation + getResources().getString(R.string.percent) + " "
        + getResources().getString(R.string.plusminus) + mSaturationDelta/2f*100);
        valResult.setText((getResources().getString(R.string.value_is) + " " + value + getResources().getString(R.string.percent) + " "
        + getResources().getString(R.string.plusminus) + mValueDelta/2f*100));

        // change button color
        Button nextButton = (Button) getActivity().findViewById(R.id.next_button);
        nextButton.setTextColor(Color.HSVToColor(new float[] {mHue, mSaturation, mValue}));

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create fragment
                NamedColorsFragment newFragment = new NamedColorsFragment();

                // use this to send info
                Bundle args = new Bundle();

                args.putFloat("leftHue", mHue);
                args.putFloat("rightHue", rightHue);
                args.putFloat("saturation", mSaturation);
                args.putFloat("saturationdelta", mSaturationDelta);
                args.putFloat("value", mValue);
                args.putFloat("valuedelta", mValueDelta);
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
