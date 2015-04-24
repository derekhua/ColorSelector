package com.example.derek.colorselector.identifycolor;

import android.app.Fragment;
import android.app.FragmentManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.derek.colorselector.R;
import com.example.derek.colorselector.namedcolors.NamedColorsFragment;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Derek on 4/23/15.
 */
public class PixelFragment extends Fragment {

    ImageButton mImageButton;
    byte[] byteArray;

    int[] mPixels;

    int mResult;

    TextView mDominantColor;

    boolean toggle = false;
    int widthButton;
    int heightButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Bundle bundle = this.getArguments();
        byteArray = bundle.getByteArray("bytearray");

        // use this layout
        return inflater.inflate(R.layout.pixel_layout, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

        mDominantColor = (TextView) getActivity().findViewById(R.id.swatch);
        mImageButton = (ImageButton) getActivity().findViewById(R.id.pixel_image);
        mImageButton.setImageBitmap(bitmap);

        int[] pixels = new int[bitmap.getWidth()*bitmap.getHeight()];

        bitmap.getPixels(pixels, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());

        findDominant(pixels);


        widthButton = mImageButton.getWidth();
        heightButton = mImageButton.getHeight();

        mImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!toggle) {
                    mImageButton.setLayoutParams(new LinearLayout.LayoutParams(3000, 1000));
                    toggle = !toggle;
                } else {
                    mImageButton.setLayoutParams(new LinearLayout.LayoutParams(widthButton, heightButton));
                    toggle = !toggle;
                }
            }
        });
    }

    // finds dominant color and creates the fragments
    private void findDominant(int[] a) {
        mPixels = a;

        // thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                Map<Integer, Integer> map = new HashMap<Integer, Integer>();

                for (int i :  mPixels ) {
                    Integer freq = map.get(i);
                    // put in map
                    map.put(i, (freq == null) ? 1 : freq + 1);
                }

                int max = -1;
                mResult = -1;

                // iterate through map, finding max
                for (Map.Entry<Integer, Integer> e : map.entrySet()) {
                    if (e.getValue() > max) {
                        mResult = e.getKey();
                        max = e.getValue();
                    }
                }

                // run on ui
                // fill the frame layouts
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        int alpha = Color.alpha(mResult);
                        int red = Color.red(mResult);
                        int green= Color.green(mResult);
                        int blue = Color.blue(mResult);

                        int rgb = Color.argb(alpha, red, green, blue);

                        mDominantColor.setBackgroundColor(rgb);
                        
                        float[] hsv = new float[3];
                        Color.colorToHSV(rgb, hsv);


                        float hue = hsv[0];
                        float saturation = hsv[1];
                        float value = hsv[2];

                        FragmentManager fm = getFragmentManager();

                        ExactColorMatchFragment exactColorMatchFragment = new ExactColorMatchFragment();

                        // exact
                        Bundle exactMatchArgs = new Bundle();
                        exactMatchArgs.putFloat("hue", hue);
                        exactMatchArgs.putFloat("saturation", saturation);
                        exactMatchArgs.putFloat("value", value);

                        exactColorMatchFragment.setArguments(exactMatchArgs);

                        fm.beginTransaction().add(R.id.pixel_frame1, exactColorMatchFragment, "exactColorMatchFragment").addToBackStack(null).commit();


                        //error for similar colors
                        float leftHue;
                        float hueError = 15f;

                        float satAndValError = 0.5f;
                        if(hueError > hue) {
                            leftHue = 360 - (hueError-hue);
                        } else {
                            leftHue = hue - hueError;
                        }

                        NamedColorsFragment namedColorsFragment = new NamedColorsFragment();

                        // similar
                        Bundle similarMatchArgs = new Bundle();
                        similarMatchArgs.putFloat("leftHue", leftHue);
                        similarMatchArgs.putFloat("rightHue", hue+hueError);

                        similarMatchArgs.putFloat("saturation", saturation);
                        similarMatchArgs.putFloat("saturationdelta", satAndValError);

                        similarMatchArgs.putFloat("value", value);
                        similarMatchArgs.putFloat("valuedelta", satAndValError);

                        namedColorsFragment.setArguments(similarMatchArgs);

                        fm.beginTransaction().add(R.id.pixel_frame2, namedColorsFragment, "namedColorsFragment").addToBackStack(null).commit();
                    }
                });
            }
        }).start();


        

    }
}



