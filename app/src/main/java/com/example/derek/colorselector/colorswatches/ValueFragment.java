package com.example.derek.colorselector.colorswatches;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.derek.colorselector.R;

import java.util.ArrayList;

/**
 * Created by Derek on 4/17/15.
 */
public class ValueFragment extends Fragment{

    public final String RES_FRAGMENT = "resultsFragment";

    private ArrayList<Integer[]> mColorList = null;

    private ColorAdapter mAdapter = null;

    private ListView mListView;

    private Integer VAL_SWATCH_NUMBER;
    private String VAL_SWATCH_NUMBER_PREF = "valswatchnumber";

    private TextView mValSeekerbarNumberText;

    private float mHue;
    private float mSaturation;
    private float mSaturationDelta;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bundle = this.getArguments();
        mHue = bundle.getFloat("hue");
        mSaturation = bundle.getFloat("saturation");
        mSaturationDelta = bundle.getFloat("saturationdelta");
        // use this layout
        return inflater.inflate(R.layout.value_view, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        VAL_SWATCH_NUMBER = getActivity().getPreferences(Context.MODE_PRIVATE).getInt(VAL_SWATCH_NUMBER_PREF, 11);

        Button button = (Button) getActivity().findViewById(R.id.val_swatch_number_button);

        float delta = 1.0f / (float)(VAL_SWATCH_NUMBER - 1);

        // get the hsv array
        mColorList = ColorCreator.getColorListValue(mHue, mSaturation, 1, delta, VAL_SWATCH_NUMBER);
        mAdapter = new ColorAdapter(getActivity(), mColorList);

        // get the list view and set the adapter
        mListView = (ListView) getActivity().findViewById(R.id.value_list);
        mListView.setAdapter(mAdapter);

        // create the button listener
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {

                // Create fragment
                ResultsFragment newFragment = new ResultsFragment();

                // use this to send info
                Bundle args = new Bundle();

                args.putFloat("hue", mHue);
                args.putFloat("saturation", mSaturation);
                args.putFloat("saturationdelta", mSaturationDelta);

                float value = 1f;
                float delta = 1.0f / (float)(VAL_SWATCH_NUMBER - 1);
                value -= (delta * position);

                args.putFloat("value", value);
                args.putFloat("valuedelta", delta);
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

        // shows AlertDialog
        // shows the AlertDialog
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setTitle("Configure Color Swatches");
                alert.setMessage("Set number of swatches");

                LinearLayout linearLayout = new LinearLayout(getActivity());

                linearLayout.setOrientation(LinearLayout.VERTICAL);
                mValSeekerbarNumberText = new TextView(getActivity());
                mValSeekerbarNumberText.setText(VAL_SWATCH_NUMBER.toString());
                mValSeekerbarNumberText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                mValSeekerbarNumberText.setPadding(10, 10, 10, 10);

                SeekBar seekBar = new SeekBar(getActivity());
                seekBar.setMax(256);
                seekBar.setProgress(VAL_SWATCH_NUMBER);
                seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        mValSeekerbarNumberText.setText(Integer.toString(seekBar.getProgress()));
                    }
                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }
                    // dynamically show the number
                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        VAL_SWATCH_NUMBER = seekBar.getProgress();
                    }
                });
                // add to layout
                linearLayout.addView(seekBar);
                linearLayout.addView(mValSeekerbarNumberText);
                // set the layout
                alert.setView(linearLayout);

                alert.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(getActivity(), "Swatch Number: " + VAL_SWATCH_NUMBER, Toast.LENGTH_SHORT).show();
                        updateAfterAlert();
                    }
                });

                alert.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });

                alert.show();
            }
        });
    }

    // update after alert dialog
    private void updateAfterAlert() {
        float delta = 1.0f / (float)(VAL_SWATCH_NUMBER-1);

        // get the hsv array
        mColorList = ColorCreator.getColorListValue(mHue, mSaturation, 1, delta, VAL_SWATCH_NUMBER);
        mAdapter = new ColorAdapter(getActivity(), mColorList);
        mListView.setAdapter(mAdapter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // retain this Fragment across configuration changes
        setRetainInstance(true);
    }

    // using this to saved preferences
    @Override
    public void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = getActivity().getPreferences(Context.MODE_PRIVATE).edit();
        // storing order preference
        editor.putInt(VAL_SWATCH_NUMBER_PREF, VAL_SWATCH_NUMBER);
        // immediately
        editor.commit();
    }
}
