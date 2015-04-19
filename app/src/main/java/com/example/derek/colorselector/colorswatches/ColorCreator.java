package com.example.derek.colorselector.colorswatches;

import android.graphics.Color;

import java.util.ArrayList;

/**
 * Created by Derek on 4/16/15.
 */
public class ColorCreator {

    // create a List of colors pairs (hue changing)
    public static ArrayList<Integer[]> getColorListHue( float h, float s, float v,    // starting values
                                                        float hueChange,              // degree varying
                                                        int size ) {                  // size of list
        ArrayList<Integer[]> list = new ArrayList<>();
        for(int i = 0; i < size; ++i) {
            int colorLeft = Color.HSVToColor(new float[] {h, s, v});
            int colorRight = Color.HSVToColor(new float[] {(h+hueChange)%360, s, v});
            h += hueChange;
            h %= 360;
            list.add(new Integer[] {colorLeft, colorRight});
        }
        return list;
    }

    // create a List of colors pairs (saturation changing)
    public static ArrayList<Integer[]> getColorListSaturation( float h, float s, float v,    // starting values
                                                               float satChange,              // degree varying
                                                               int size ) {                  // size of list
        ArrayList<Integer[]> list = new ArrayList<>();
        for(int i = 0; i < size; ++i) {
            int colorLeft = Color.HSVToColor(new float[] {h, s, v});
            int colorRight = Color.HSVToColor(new float[] {(h+30)%360, s, v});

            s += satChange;
            list.add(new Integer[] {colorLeft, colorRight});
        }
        return list;
    }

    // create a List of colors pairs (value changing)
    public static ArrayList<Integer[]> getColorListValue( float h, float s, float v,    // starting values
                                                          float valChange,              // degree varying
                                                          int size ) {                  // size of list
        ArrayList<Integer[]> list = new ArrayList<>();
        for(int i = 0; i < size; ++i) {
            int colorLeft = Color.HSVToColor(new float[] {h, s, v});
            int colorRight = Color.HSVToColor(new float[] {(h+30)%360, s, v});

            v += valChange;
            list.add(new Integer[] {colorLeft, colorRight});
        }
        return list;
    }
}
