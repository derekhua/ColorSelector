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
 * Created by Derek on 4/16/15.
 */
public class SaturationFragment extends Fragment {

    public final String VAL_FRAGMENT = "valueFragment";

    private ArrayList<Integer[]> mColorList = null;

    private ColorAdapter mAdapter = null;

    ListView mListView;

    private float mHue;
    private float mHueDelta;

    private Integer SAT_SWATCH_NUMBER;
    private String SAT_SWATCH_NUMBER_PREF = "swatchnumberpref";
    private TextView mSatSeekbarNumberText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bundle = this.getArguments();
        mHue = bundle.getFloat("hue");
        mHueDelta = bundle.getFloat("huedelta");

        // use this layout
        return inflater.inflate(R.layout.saturation_view, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        SAT_SWATCH_NUMBER = getActivity().getPreferences(Context.MODE_PRIVATE).getInt(SAT_SWATCH_NUMBER_PREF, 11);

        Button button = (Button) getActivity().findViewById(R.id.sat_swatch_number_button);

        // 100 / (SAT_SWATCH_NUMBER-1) = delta
        float delta = 1.0f / (float)(SAT_SWATCH_NUMBER - 1);

        // get the hsv array
        mColorList = ColorCreator.getColorListSaturation(mHue, 1, 1, delta, SAT_SWATCH_NUMBER, mHueDelta);
        mAdapter = new ColorAdapter(getActivity(), mColorList);

        // get the list view and set the adapter
        mListView = (ListView) getActivity().findViewById(R.id.saturation_list);
        mListView.setAdapter(mAdapter);

        // create the button listener
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {

                // Create fragment
                ValueFragment newFragment = new ValueFragment();

                // use this to send info
                Bundle args = new Bundle();
                args.putFloat("hue", mHue);
                args.putFloat("huedelta", mHueDelta);

                float saturation = 1f;
                float delta;
                if(SAT_SWATCH_NUMBER == 1) {
                    delta = 2.0f;
                } else {
                    delta = 1.0f / (float) (SAT_SWATCH_NUMBER - 1);
                }
                saturation -= (delta * position);

                args.putFloat("saturation", saturation);
                args.putFloat("saturationdelta", delta);
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

        // shows the AlertDialog
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setTitle(getResources().getString(R.string.set_swatch_number));

                LinearLayout linearLayout = new LinearLayout(getActivity());

                linearLayout.setOrientation(LinearLayout.VERTICAL);
                mSatSeekbarNumberText = new TextView(getActivity());
                mSatSeekbarNumberText.setText(SAT_SWATCH_NUMBER.toString());
                mSatSeekbarNumberText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                mSatSeekbarNumberText.setPadding(10, 10, 10, 10);

                // set initial values
                SeekBar seekBar = new SeekBar(getActivity());
                seekBar.setMax(255);
                seekBar.setProgress(SAT_SWATCH_NUMBER);
                seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        mSatSeekbarNumberText.setText(Integer.toString(seekBar.getProgress() + 1));
                    }
                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }
                    // dynamically show the number
                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        SAT_SWATCH_NUMBER = seekBar.getProgress() + 1;
                    }
                });
                // add to layout
                linearLayout.addView(seekBar);
                linearLayout.addView(mSatSeekbarNumberText);
                // set the layout
                alert.setView(linearLayout);

                alert.setPositiveButton(getResources().getString(R.string.ok),new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(getActivity(), getResources().getString(R.string.swatch_number) + ": " + SAT_SWATCH_NUMBER,
                                Toast.LENGTH_SHORT).show();
                        updateAfterAlert();
                    }
                });

                alert.setNegativeButton(getResources().getString(R.string.cancel),new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });

                alert.show();
            }
        });
    }

    // update after alert dialog
    private void updateAfterAlert() {
        float delta = 1.0f / (float)(SAT_SWATCH_NUMBER-1);
//        float hue = 345;
//        hue += (30 * mPosition);
//        hue %= 360;

        // get the hsv array
        mColorList = ColorCreator.getColorListSaturation(mHue, 1, 1, delta, SAT_SWATCH_NUMBER, mHueDelta);
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
        editor.putInt(SAT_SWATCH_NUMBER_PREF, SAT_SWATCH_NUMBER);
        // immediately
        editor.commit();
    }
}
