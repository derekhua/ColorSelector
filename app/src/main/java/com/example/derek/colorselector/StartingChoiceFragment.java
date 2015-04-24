package com.example.derek.colorselector;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.example.derek.colorselector.colorswatches.HueFragment;
import com.example.derek.colorselector.identifycolor.PixelFragment;

import java.io.ByteArrayOutputStream;

/**
 * Created by Derek on 4/22/15.
 */
public class StartingChoiceFragment extends Fragment {

    public final String HUE_FRAGMENT = "hueFragment";
    public final String PIXEL_FRAGMENT = "pixelFragment";

    ImageView imageView;

    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1888;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // use this layout
        return inflater.inflate(R.layout.choice_layout, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        Button buttonExplorer = (Button) getActivity().findViewById(R.id.color_explorer_button);

        // listeners
        buttonExplorer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check that the activity is using the layout version with the fragment_container FrameLayout
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                HueFragment newFragment = new HueFragment();

                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack so the user can navigate back
                transaction.replace(R.id.fragment_container, newFragment, HUE_FRAGMENT);
                transaction.addToBackStack("startingChoiceFragment");

                // Commit the transaction
                transaction.commit();
            }
        });

        Button buttonIdentify = (Button) getActivity().findViewById(R.id.color_identifier_button);
        imageView = (ImageView) getActivity().findViewById(R.id.test_image);

        // starts the camera
        buttonIdentify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
            }
        });


    }

    // camera buttons pressed
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {

            if (resultCode == Activity.RESULT_OK) {
                Bitmap bmp = (Bitmap) data.getExtras().get("data");

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                // compress
                bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                // to byte array
                byte[] byteArray = stream.toByteArray();

                // Create fragment
                PixelFragment newFragment = new PixelFragment();

                // use this to send info
                Bundle args = new Bundle();

                args.putByteArray("bytearray", byteArray);
                newFragment.setArguments(args);

                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack so the user can navigate back
                transaction.replace(R.id.fragment_container, newFragment, PIXEL_FRAGMENT);
                transaction.addToBackStack("startingChoiceFragment");

                // Commit the transaction
                transaction.commit();
            }
        }
    }
}
