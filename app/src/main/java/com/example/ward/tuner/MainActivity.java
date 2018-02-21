package com.example.ward.tuner;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.Scanner;
import java.util.WeakHashMap;


public class MainActivity extends Activity
{
    public final static String ARTIST_NAME_NAME = "com.example.tward.AlbumFinder.ARTIST_NAME";
    public final static String IS_DISPLAYING_RECENT_ALBUMS_NAME =
            "com.example.tward.AlbumFinder.IS_DISPLAYING_RECENT_ALBUMS";
    public final static String ARTIST_URL_FILE = "com.example.tward.AlbumFinder.artist_url_file";
    public final static String COLLECTION_ID_FILE = "com.example.tward.AlbumFinder.collection_id_file";


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpAlarmManager();

        Log.i("MAIN_ACTIVITY",getArtistUrlMap().keySet().toString());
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        populateArtistList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        ComponentName cn = (new ComponentName(this, ArtistActivity.class));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will®
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings)
        {
            //Open settings activity
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        else if(id == R.id.action_legal_text)
        {
            Intent intent = new Intent(this,LegalTextActivity.class);
            startActivity(intent);
            return true;
        }
        else if(id == R.id.action_search)
        {
            return onSearchRequested();
        }
//        else if(id == R.id.action_refresh)
//        {
//            ReleaseNotificationChecker checker = new ReleaseNotificationChecker(this);
//            checker.clearCache();
//            checker.checkForRecentReleaseFromFile();
//            return true;
//        }
        return super.onOptionsItemSelected(item);
    }

    private void setUpAlarmManager()
    {
        Intent intent = new Intent(this, ReleaseService.class);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent, 0);

        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 8);

        manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pendingIntent);

        Log.i("MAIN ACTIVITY", "Alarm Manager set up method complete");
    }

    private void populateArtistList()
    {
        WeakHashMap<String,String> artistMap = getArtistUrlMap();
        String[] keySet = artistMap.keySet().toArray(new String[artistMap.size()]);

        if(!artistMap.isEmpty())
        {

            ChosenArtistsAdapter adapter = new ChosenArtistsAdapter
                    (this, keySet, artistMap);
            ListView listView = (ListView) findViewById(R.id.artistList);
            listView.setAdapter(adapter);
            listView.setVisibility(View.VISIBLE);
        }
    }

    public void search(View view)
    {
        onSearchRequested();
    }

    private WeakHashMap<String,String> getArtistUrlMap()
    {
        Scanner in = null;
        WeakHashMap<String, String> artistMap = new WeakHashMap<String, String>();
        try
        {
            in = new Scanner(new File(this.getFilesDir(), MainActivity.ARTIST_URL_FILE));
            while (in.hasNextLine())
            {
                artistMap.put(in.nextLine(),in.nextLine());
            }
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

        return artistMap;
    }

    public void onCardClick(View view)
    {
        TextView textView = (TextView)view.findViewById(R.id.cardTitle);
        final String artistName = textView.getText().toString().trim();


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(artistName)
                .setItems(R.array.artist_options,
                        new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int index)
                            {
                                switch (index)
                                {
                                    case 0:
                                        startAlbumListActivity(artistName,true);
                                        break;
                                    case 1:
                                        startAlbumListActivity(artistName,false);
                                        break;
                                    case 2:
                                        removeArtist(artistName);
                                        break;
                                }
                            }
                        });

        builder.create().show();
    }

    private void removeArtist(String artist)
    {
        WeakHashMap<String,String> artistMap = getArtistUrlMap();
        artistMap.remove(artist);

        StringBuilder textToWrite = new StringBuilder();
        for(String s : artistMap.keySet())
        {
            textToWrite.append(s + "\n" + artistMap.get(s) + "\n");
        }

        FileOutputStream outputStream;

        try
        {
            outputStream = openFileOutput
                    (MainActivity.ARTIST_URL_FILE, Context.MODE_PRIVATE);
            outputStream.write(textToWrite.toString().getBytes());
            outputStream.close();

        } catch (Exception e)
        {
            e.printStackTrace();
        }
        refreshList();
    }

    private void startAlbumListActivity(String artistName, boolean isSearchRecentOnly)
    {
        Intent intent = new Intent(this,AlbumListActivity.class);
        intent.putExtra(MainActivity.ARTIST_NAME_NAME, artistName);
        intent.putExtra(MainActivity.IS_DISPLAYING_RECENT_ALBUMS_NAME,isSearchRecentOnly);

        startActivity(intent);
    }

    private void refreshList()
    {
        ListView listView = (ListView)findViewById(R.id.artistList);
        listView.invalidateViews();
        WeakHashMap<String,String> artistMap = getArtistUrlMap();
        String[] keySet = artistMap.keySet().toArray(new String[artistMap.size()]);

        ChosenArtistsAdapter adapter = new ChosenArtistsAdapter
                (this, keySet, artistMap);
        listView.setAdapter(adapter);
        listView.setVisibility(View.VISIBLE);
    }


}
