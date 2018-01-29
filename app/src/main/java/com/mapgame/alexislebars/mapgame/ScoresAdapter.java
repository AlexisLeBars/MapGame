package com.mapgame.alexislebars.mapgame;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by alexislebars on 29/01/2018.
 */

public class ScoresAdapter extends ArrayAdapter<Scores> {
    public ScoresAdapter(Context context, List<Scores> scores) {
        super(context, 0, scores);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Scores score = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_layout, parent, false);
        }
        // Lookup view for data population
        TextView tvScore = convertView.findViewById(R.id.tvScore);
        TextView tvLevel = convertView.findViewById(R.id.tvLevel);
        TextView tvMode = convertView.findViewById(R.id.tvMode);
        TextView tvDate = convertView.findViewById(R.id.tvDate);
        // Populate the data into the template view using the data object
        tvScore.setText(Math.round(Double.parseDouble(score.getScore()))+"");
        tvMode.setText(score.getMode());
        tvDate.setText(score.getDate());
        tvLevel.setText(score.getLevel());
        // Return the completed view to render on screen
        return convertView;
    }

}
