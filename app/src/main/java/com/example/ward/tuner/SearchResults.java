package com.example.ward.tuner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by 15wardj on 11/10/2014.
 */
public class SearchResults
{
    private ArrayList<Album> albumsList = new ArrayList<Album>();
    private ArrayList<Album> recentAlbumsList = new ArrayList<Album>();

    public SearchResults(JSONObject jsonObject, String artistName) throws JSONException
    {
        artistName = artistName.toLowerCase();

        if(jsonObject != null && jsonObject.getInt("resultCount") > 0)
        {
            JSONArray jsonArray = (JSONArray)jsonObject.get("results");

            for(int i = 0;i < jsonArray.length();i++)
            {
                JSONObject jsonAlbum = (JSONObject)jsonArray.get(i);
                if(jsonAlbum.getString("artistName") != null &&
                        jsonAlbum.getString("artistName").toLowerCase().equals(artistName))
                {
                    albumsList.add(new Album(jsonAlbum));
                }



            }
        }

        populateRecentAlbums();
    }

    private void populateRecentAlbums()
    {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Calendar thirtyDaysAgo = Calendar.getInstance();
        thirtyDaysAgo.add(Calendar.DAY_OF_MONTH,-30);


        for(Album a : albumsList)
        {
            try
            {

                Calendar releaseDateCalendar = Calendar.getInstance();
                releaseDateCalendar.setTime(
                        simpleDateFormat.parse(a.getReleaseDate().substring(0,10)));

                if(releaseDateCalendar.after(thirtyDaysAgo))
                {
                    recentAlbumsList.add(a);
                }
            } catch (ParseException e)
            {
                e.printStackTrace();
            }
        }
    }

    public ArrayList<Album> getAlbumsList()
    {
        return albumsList;
    }

    public ArrayList<Album> getRecentAlbumsList()
    {
        return recentAlbumsList;
    }

    public boolean doesArtistHaveNewAlbum()
    {
        return !recentAlbumsList.isEmpty();
    }

    public boolean doesArtistHaveAlbum()
    {
        return !albumsList.isEmpty();
    }
}
