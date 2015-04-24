package com.example.derek.colorselector.colorswatches;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.derek.colorselector.R;

import java.util.List;

/**
 * Created by Derek on 4/16/15.
 */
public class ColorAdapter extends ArrayAdapter<Integer[]>{

    private final List<Integer[]> colorsList;

    // constructor
    public ColorAdapter(Context context, List<Integer[]> objects) {
        super(context, 0, objects);
        colorsList = objects;
    }

    // ViewHolder
    private static class ViewHolder {
        TextView preview;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder; // cache views to avoid future lookups

        // check if an existing view is being recycled; if not
        // inflate a new one
        if(convertView == null) {
            viewHolder = new ViewHolder();

            // inflate the color.xml file
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.color, parent, false);

            // Setting the Views
            viewHolder.preview = (TextView) convertView.findViewById(R.id.preview);

            // cache the views
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


//        // create gradient
//        GradientDrawable drawable = new GradientDrawable( GradientDrawable.Orientation.LEFT_RIGHT,
//                                                          new int[] {colorsList.get(position)[0],
//                                                                  colorsList.get(position)[1]});
//        // set background
//        viewHolder.preview.setBackground(drawable);
//        return convertView;

        // create gradient
        GradientDrawable drawable = new GradientDrawable();
        if(colorsList.size() == 1) {
            drawable.setColors(colorsBetween(colorsList.get(position)[0], (colorsList.get(position)[0]), 15));
        } else {
            drawable.setColors(colorsBetween(colorsList.get(position)[0], colorsList.get(position)[1], 15));
        }
        drawable.setOrientation(GradientDrawable.Orientation.LEFT_RIGHT);
        viewHolder.preview.setBackground(drawable);
        return convertView;
    }

    // function to better interpolate
    private int[] colorsBetween(int left,       // left color
                                int right,      // right color
                                int amount) {   // amount of intermediates
        float[] hsvLeft = new float[3];
        Color.colorToHSV(left, hsvLeft);

        float[] hsvRight = new float[3];
        Color.colorToHSV(right, hsvRight);

        // get the difference
        float difference;
        if(hsvRight[0] > hsvLeft[0]) {
            // normal
            difference = hsvRight[0] - hsvLeft[0];
        }
        else if (hsvRight[0] == hsvLeft[0])
            difference  = 360;
        else {
            difference  = (360 - hsvLeft[0]) + hsvRight[0];
        }

        // find the step size
        float step = difference/amount;

        int[] colors = new int[amount];
        float current = hsvLeft[0];
        for(int i = 0; i < amount; ++i) {
            colors[i] = Color.HSVToColor(new float[] {current, hsvLeft[1], hsvRight[2]});
            current += step;
            current%= 360;
        }
        return colors;
    }
}
