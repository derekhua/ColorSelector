package com.example.derek.colorselector.colorswatches;

import android.content.Context;
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


        // create gradient
        GradientDrawable drawable = new GradientDrawable( GradientDrawable.Orientation.LEFT_RIGHT,
                                                          new int[] {colorsList.get(position)[0], colorsList.get(position)[1]});
        // set background
        viewHolder.preview.setBackground(drawable);

        return convertView;
    }
}
