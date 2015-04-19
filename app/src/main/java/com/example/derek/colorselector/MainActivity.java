package com.example.derek.colorselector;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.derek.colorselector.colorswatches.HueFragment;


public class MainActivity extends ActionBarActivity {

    FragmentManager fm;
    HueFragment firstFragment;

    public final String HUE_FRAGMENT = "hueFragment";
    public final String SAT_FRAGMENT = "saturationFragment";
    public final String VAL_FRAGMENT = "valueFragment";
    public final String RES_FRAGMENT = "resultsFragment";

    public String currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Check that the activity is using the layout version with the fragment_container FrameLayout
        if (findViewById(R.id.fragment_container) != null) {
            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            fm = getFragmentManager();
            firstFragment = (HueFragment) fm.findFragmentByTag(HUE_FRAGMENT);

            // if mFragment is non-null, then its currently being retain across configuration changes
            if(firstFragment == null) {
                // Create a new Fragment to be placed in the activity layout
                firstFragment = new HueFragment();
                // In case this activity was started with special instructions from an
                // Intent, pass the Intent's extras to the fragment as arguments
//            firstFragment.setArguments(getIntent().getExtras());

                // Add the fragment to the 'fragment_container' FrameLayout
                fm.beginTransaction().add(R.id.fragment_container, firstFragment, HUE_FRAGMENT).addToBackStack(null).commit();
            }
        }
    }

    // handle the back button press
    @Override
    public void onBackPressed() {
        if(fm.getBackStackEntryCount() != 1) {
            fm.popBackStack();
        } else {
            Toast.makeText(this, "fuck you", Toast.LENGTH_SHORT).show();
            super.onBackPressed();
//            fm = getFragmentManager();
//            firstFragment = (HueFragment) fm.findFragmentByTag(HUE_FRAGMENT);
//
//            // if mFragment is non-null, then its currently being retain across configuration changes
//            if(firstFragment == null) {
//                // Create a new Fragment to be placed in the activity layout
//                firstFragment = new HueFragment();
//                // In case this activity was started with special instructions from an
//                // Intent, pass the Intent's extras to the fragment as arguments
////            firstFragment.setArguments(getIntent().getExtras());
//
//                // Add the fragment to the 'fragment_container' FrameLayout
//                fm.beginTransaction().add(R.id.fragment_container, firstFragment, HUE_FRAGMENT).addToBackStack(null).commit();
//            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        SharedPreferences prefs = this.getSharedPreferences("com.example.derek.colorselector", Context.MODE_PRIVATE);
//
//        // keep track of current frag
//        String CURRENT_FRAG = null;
//
//        HueFragment myFragment1 = (HueFragment)getFragmentManager().findFragmentByTag(HUE_FRAGMENT);
//        SaturationFragment myFragment2 = (SaturationFragment)getFragmentManager().findFragmentByTag(SAT_FRAGMENT);
//        ValueFragment myFragment3 = (ValueFragment)getFragmentManager().findFragmentByTag(VAL_FRAGMENT);
//        ResultsFragment myFragment4 = (ResultsFragment)getFragmentManager().findFragmentByTag(RES_FRAGMENT);
//
//        if (myFragment1.isVisible()) {
//            prefs.edit().putString(CURRENT_FRAG, HUE_FRAGMENT).apply();
//        } else if (myFragment2.isVisible()) {
//            prefs.edit().putString(CURRENT_FRAG, SAT_FRAGMENT).apply();
//        } else if (myFragment3.isVisible()) {
//            prefs.edit().putString(CURRENT_FRAG, VAL_FRAGMENT).apply();
//        } else if (myFragment4.isVisible()) {
//            prefs.edit().putString(CURRENT_FRAG, RES_FRAGMENT).apply();
//        }
//    }
}
