package com.example.ward.tuner;


import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class AlbumListActivity extends ListActivity
{

    private String artistName;
    private boolean isDisplayingRecentAlbums;
    private ArrayList<Album> albumsList;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_list);
        startSearch();

        Log.i("ALBUM_LIST_ACTIVITY",artistName);
    }

    private void startSearch()
    {
        Intent intent = getIntent();
        artistName = intent.getStringExtra(MainActivity.ARTIST_NAME_NAME);
        isDisplayingRecentAlbums = intent.getBooleanExtra
                (MainActivity.IS_DISPLAYING_RECENT_ALBUMS_NAME, false);

        if(isDisplayingRecentAlbums)
        {
            new GetRecentAlbumsTask().execute(artistName.trim());
        }
        else
        {
            new GetAllAlbumsTask().execute(artistName.trim());
        }
    }

    private void populateList()
    {
        if(albumsList.isEmpty())
        {
            CardView cardView = (CardView)findViewById(R.id.artistCard);
            cardView.setVisibility(View.VISIBLE);
        }
        else
        {
            AlbumAdapter adapter = new AlbumAdapter(this, albumsList);

            ListView listView = getListView();
            listView.setAdapter(adapter);
        }

    }

    public void onCardClick(View view)
    {
        TextView textView = (TextView)view.findViewById(R.id.cardTitle);
        final String albumName = textView.getText().toString().trim();
        final int position = (Integer)textView.getTag();


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(albumName)
                .setItems(R.array.album_options,
                        new DialogInterface.OnClickListener()
                        {
                            String url;
                            public void onClick(DialogInterface dialog, int index)
                            {
                                switch (index)
                                {
                                    case 0: //Links to iTunes via collection ID
                                        Album album = albumsList.get(position);
                                        url = album.getCollectionViewUrl();
                                        openUrl(url);
                                        break;
                                    case 1: //Search Google Play for artist name
                                        url = "https://play.google.com/store/search?q=" + artistName + "&c=music";
                                        openUrl(url);
                                        break;
                                    case 2: //Search YouTube
                                        Intent intent = new Intent(Intent.ACTION_SEARCH);
                                        intent.setPackage("com.google.android.youtube");
                                        intent.putExtra("query", artistName);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        break;
                                    case 3: //Search Amazon for artist name
                                        url = "http://www.amazon.com/s?url=search&field-keywords=" + artistName;
                                        openUrl(url);
                                }
                            }
                        });

        builder.create().show();
    }

    private void openUrl(String url)
    {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    private class GetAllAlbumsTask extends AsyncTask<String, Void, JSONObject>
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
                json = new JSONParser().readJsonFromUrl
                        ("https://itunes.apple.com/search?term=" + query.replaceAll("\\s","+") + "&entity=album");
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
                SearchResults searchResults = new SearchResults(jsonObject,query);
                albumsList = searchResults.getAlbumsList();
                populateList();
            } catch (JSONException e)
            {
                e.printStackTrace();
            }



        }
    }

    private class GetRecentAlbumsTask extends AsyncTask<String, Void, JSONObject>
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
                json = new JSONParser().readJsonFromUrl
                        ("https://itunes.apple.com/search?term=" + query.replaceAll("\\s","+") + "&entity=album");
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
                SearchResults searchResults = new SearchResults(jsonObject,query);
                albumsList = searchResults.getRecentAlbumsList();
                populateList();
            } catch (JSONException e)
            {
                e.printStackTrace();
            }



        }
    }
}
