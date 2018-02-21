package com.example.ward.tuner;

/**
 * Created by 15wardj on 10/6/2014.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.ion.Ion;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class AlbumAdapter extends ArrayAdapter<Album> {
    public AlbumAdapter(Context context, ArrayList<Album> albums) {
        super(context, 0, albums);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Album album = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.album_card, parent, false);
        }
        // Lookup view for data population
        TextView albumName = (TextView) convertView.findViewById(R.id.cardTitle);
        TextView releaseDate = (TextView) convertView.findViewById(R.id.cardText);
        ImageView albumCover = (ImageView) convertView.findViewById(R.id.cardImage);
        // Populate the data into the template view using the data object
        albumName.setText(album.getCollectionCensoredName());
        albumName.setTag(position);

        Calendar releaseDateCalendar = Calendar.getInstance();
        try
        {
            releaseDateCalendar.setTime(
                    (new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)).parse(
                            album.getReleaseDate().substring(0, 10)));

            releaseDate.setText((new SimpleDateFormat("MMMM dd, yyyy", Locale.ENGLISH)).format
                    (releaseDateCalendar.getTime()));
        } catch (ParseException e)
        {
            e.printStackTrace();
        }

        Ion.with(albumCover)
                .error(android.R.drawable.stat_notify_error)
                .load(album.getArtworkUrl100());

        // Return the completed view to render on screen
        return convertView;
    }
}