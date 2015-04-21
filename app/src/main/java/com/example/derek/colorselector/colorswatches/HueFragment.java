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
// contains the color swatches
public class HueFragment extends Fragment {

    public final String SAT_FRAGMENT = "saturationFragment";

    // holds the color pairs
    private ArrayList<Integer[]> mColorList = null;

    private ColorAdapter mAdapter = null;
        
    // for shared prefs
    private Integer HUE_SWATCH_NUMBER;    
    private String HUE_SWATCH_NUMBER_PREF = "hueswatchnumber";
    private Integer HUE_CENTER_DEGREE;
    private String HUE_CENTER_DEGREE_PREF = "huecenterdegree";

    private TextView mHueSwatchNumberText;
    private TextView mHueCenterDegreeNumberText;
    private ListView mListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // use this layout
        return inflater.inflate(R.layout.hue_view, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();        

        HUE_SWATCH_NUMBER = getActivity().getPreferences(Context.MODE_PRIVATE).getInt(HUE_SWATCH_NUMBER_PREF, 12);
        HUE_CENTER_DEGREE = getActivity().getPreferences(Context.MODE_PRIVATE).getInt(HUE_CENTER_DEGREE_PREF, 0);

        Button button = (Button) getActivity().findViewById(R.id.hue_color_swatch_button);

        // the size of the gradient
        float delta = 360f / (float)(HUE_SWATCH_NUMBER);

        // the first left point degree
        float hueStartingPoint;

        // for the wrap around
        if((delta/2) > HUE_CENTER_DEGREE) {
            float temp = (delta/2) - HUE_CENTER_DEGREE;
            hueStartingPoint = 360f - temp;
        } else {
            hueStartingPoint = HUE_CENTER_DEGREE - (delta/2);
        }

        // get the hsv array
        mColorList = ColorCreator.getColorListHue(hueStartingPoint, 1, 1, delta, HUE_SWATCH_NUMBER);
        mAdapter = new ColorAdapter(getActivity(), mColorList);
        
        // get the list view and set the adapter
        mListView = (ListView) getActivity().findViewById(R.id.hue_list);
        mListView.setAdapter(mAdapter);

        // create the button listener
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {

                // Create fragment
                SaturationFragment newFragment = new SaturationFragment();

                // use this to send info
                Bundle args = new Bundle();

                float delta = 360f / (float)(HUE_SWATCH_NUMBER);
                float hueStartingPoint;

                // for the wrap around
                if((delta/2) > HUE_CENTER_DEGREE) {
                    float temp = (delta/2) - HUE_CENTER_DEGREE;
                    hueStartingPoint = 360f - temp;
                } else {
                    hueStartingPoint = HUE_CENTER_DEGREE - (delta/2);
                }

                float hue = hueStartingPoint;
                hue += (delta * position);
                hue %= 360;

                args.putFloat("hue", hue);
                args.putFloat("huedelta", delta);

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

        // create the AlertDialog
        // shows the AlertDialog
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setTitle("Set number of swatches & first center degree");

                LinearLayout linearLayout = new LinearLayout(getActivity());
                linearLayout.setOrientation(LinearLayout.VERTICAL);

                mHueSwatchNumberText = new TextView(getActivity());
                mHueSwatchNumberText.setText(HUE_SWATCH_NUMBER.toString());
                mHueSwatchNumberText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                mHueSwatchNumberText.setPadding(10, 10, 10, 10);

                SeekBar seekBar = new SeekBar(getActivity());
                seekBar.setMax(35);
                seekBar.setProgress(HUE_SWATCH_NUMBER);
                seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        mHueSwatchNumberText.setText(Integer.toString(seekBar.getProgress() + 1));
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    // dynamically show the number
                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        HUE_SWATCH_NUMBER = seekBar.getProgress() + 1;
                    }
                });

                // for other SeekBar
                mHueCenterDegreeNumberText = new TextView(getActivity());
                mHueCenterDegreeNumberText.setText(HUE_CENTER_DEGREE.toString() + "°");
                mHueCenterDegreeNumberText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                mHueCenterDegreeNumberText.setPadding(10, 10, 10, 10);

                final SeekBar seekBarColorCenter = new SeekBar(getActivity());
                seekBarColorCenter.setMax(360);
                seekBarColorCenter.setProgress(HUE_CENTER_DEGREE);
                seekBarColorCenter.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        mHueCenterDegreeNumberText.setText(Integer.toString(seekBarColorCenter.getProgress()) + "°");
                    }
                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }
                    // dynamically show the number
                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        HUE_CENTER_DEGREE = seekBar.getProgress();
                    }
                });

                // add to layout
                linearLayout.addView(seekBar);
                linearLayout.addView(mHueSwatchNumberText);
                linearLayout.addView(seekBarColorCenter);
                linearLayout.addView(mHueCenterDegreeNumberText);

                // set the layout
                alert.setView(linearLayout);

                alert.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(getActivity(), "Swatch Number: " + HUE_SWATCH_NUMBER +
                                        "\nCenter Degree: " + HUE_CENTER_DEGREE + "°", Toast.LENGTH_SHORT).show();
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
        float delta = 360f / (float)(HUE_SWATCH_NUMBER);
        float hueStartingPoint = HUE_CENTER_DEGREE - (delta/2);

        // get the hsv array
        mColorList = ColorCreator.getColorListHue(hueStartingPoint, 1, 1, delta, HUE_SWATCH_NUMBER);
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
        editor.putInt(HUE_SWATCH_NUMBER_PREF, HUE_SWATCH_NUMBER);
        editor.putInt(HUE_CENTER_DEGREE_PREF, HUE_CENTER_DEGREE);
        // immediately
        editor.commit();
    }
}