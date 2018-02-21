package com.example.ward.tuner;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.koushikdutta.ion.Ion;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ArtistActivity extends Activity
{
    private SearchResults searchResults;
    private ProgressBar progressBar;
    private CardView artistCard;

    /* Override Methods*/

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        artistCard = (CardView) findViewById(R.id.artistCard);

        Intent intent = getIntent();
        String query = null;
        if (Intent.ACTION_SEARCH.equals(intent.getAction()))
        {
            query = intent.getStringExtra(SearchManager.QUERY);

            parseJSON(query);
        }

        Log.i("ARTIST_ACTIVITY",query);
    }

    /**
     * **
     */

    /*Set Up Methods*/

    private void parseJSON(String query)
    {

        new JSONParse().execute(query.trim());
    }

    private void populateArtistCard(Boolean doesArtistHaveAlbum)
    {
        ImageView imageView = (ImageView) artistCard.findViewById(R.id.cardImage);
        TextView textView = (TextView) artistCard.findViewById(R.id.cardText);

        if(doesArtistHaveAlbum)
        {
            Album firstAlbum = searchResults.getAlbumsList().get(0);

            Ion.with(imageView)
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .error(android.R.drawable.stat_notify_error)
                    .load(firstAlbum.getArtworkUrl100());

            textView.setText(firstAlbum.getArtistName());

            progressBar.setVisibility(View.GONE);
            artistCard.setVisibility(View.VISIBLE);
        }
        else
        {
            imageView.setImageResource(android.R.drawable.ic_menu_gallery);
            textView.setText(R.string.no_data);

            progressBar.setVisibility(View.GONE);
            artistCard.setVisibility(View.VISIBLE);
        }

    }

    public void createAddArtistDialog(View view)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        Album album = searchResults.getAlbumsList().get(0);
        final String artistName = album.getArtistName();
        final String url = album.getArtworkUrl100();

        builder.setMessage(R.string.add_artist_message)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        writeToFile(artistName + " " + url);
                        checkForRelease(artistName);
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        // do nothing
                    }
                });

        builder.create().show();

    }

    private void writeToFile(String text)
    {
        FileOutputStream outputStream;

        try
        {
            outputStream = openFileOutput
                    (com.example.ward.tuner.MainActivity.ARTIST_URL_FILE, Context.MODE_APPEND);
            outputStream.write(text.getBytes());
            outputStream.close();

        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void checkForRelease(String name)
    {
        ReleaseNotificationChecker checker = new ReleaseNotificationChecker(this);
        checker.checkForRecentReleaseFromString(name);
    }

    /******/


    /* Async Tasks */

    private class JSONParse extends AsyncTask<String, Void, JSONObject>
    {
        private String query;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(String... args)
        {
            JSONObject json = null;

            try
            {
                query = args[0];
                json = new JSONParser().readJsonFromUrl("https://itunes.apple.com/search?term=" + query.replaceAll("\\s","+") + "&entity=album");
            } catch (IOException e)
            {
                e.printStackTrace();
            } catch (JSONException e)
            {
                e.printStackTrace();
            }
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject)
        {
            try
            {
                searchResults = new SearchResults(jsonObject,query);
            } catch (JSONException e)
            {
                e.printStackTrace();
            }

            populateArtistCard(searchResults.doesArtistHaveAlbum());

        }
    }

    /******/
}
