package com.example.ward.tuner;

/**
 * Created by tyler on 12/3/2014.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.ion.Ion;

import java.util.WeakHashMap;

public class ChosenArtistsAdapter extends ArrayAdapter<String>
{
    private WeakHashMap<String,String> artists;
    private String[] keys;


    public ChosenArtistsAdapter(Context context,String[] keys, WeakHashMap<String,String> artists) {
        super(context, 0, keys);
        this.artists = artists;
        this.keys = keys;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        String artistNameString = getItem(position);
        String artistCollectionString = artists.get(artistNameString);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.artist_card, parent, false);
        }
        // Lookup view for data population
        TextView artistName = (TextView) convertView.findViewById(R.id.cardTitle);
        ImageView albumCover = (ImageView) convertView.findViewById(R.id.cardImage);

        // Populate the data into the template view using the data object
        artistName.setText(artistNameString);

        Ion.with(albumCover)
                .error(android.R.drawable.stat_notify_error)
                .load(artists.get(artistNameString));

        // Return the completed view to render on screen
        return convertView;
    }
}