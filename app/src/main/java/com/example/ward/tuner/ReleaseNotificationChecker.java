package  com.example.ward.tuner;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Created by Tyler on 1/17/15.
 */
public class ReleaseNotificationChecker
{
    private ArrayList<WeakAlbum> artistsWithNewAlbum = new ArrayList<WeakAlbum>();
    private NotificationCompat.Builder mBuilder;
    private Context context;
    private Intent notificationIntent;

    public ReleaseNotificationChecker(Context context)
    {
        this.context = context;
        notificationIntent = new Intent(context,AlbumListActivity.class);
    }

    public void checkForRecentReleaseFromFile()
    {
        File file = new File(context.getFilesDir(), MainActivity.ARTIST_URL_FILE);
        Scanner in = null;
        ArrayList<String> artists = new ArrayList<String>();
        try
        {
            in = new Scanner(file);
            while (in.hasNextLine())
            {
                artists.add(in.nextLine());
                in.nextLine();
            }
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

        if(!artists.isEmpty())
        {
            parseQueries(artists.toArray(new String[artists.size()]));
        }
    }

    public void checkForRecentReleaseFromString(String artist)
    {
        parseQueries(artist);
    }

    public void clearCache()
    {
        context.deleteFile(MainActivity.COLLECTION_ID_FILE);
    }



    private void buildNotification(WeakAlbum... weakAlbums)
    {
        notificationIntent = new Intent(context,MainActivity.class);

        PendingIntent notificationPendingIntent =
                PendingIntent.getActivity(
                        context,
                        0,
                        notificationIntent,
                        PendingIntent.FLAG_CANCEL_CURRENT
                );


        mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_album_notification)
                        .setContentTitle("New Releases!")
                        .setContentIntent(notificationPendingIntent)
                        .setAutoCancel(true);

        StringBuilder sb = new StringBuilder();

        for(WeakAlbum album : weakAlbums)
        {
            sb.append(album.getArtistName() + ", ");
        }

        mBuilder.setContentText(sb.toString().substring(0,sb.length()));
    }

    private void buildNotification(WeakAlbum weakAlbum)
    {
        notificationIntent.putExtra(MainActivity.ARTIST_NAME_NAME,weakAlbum.getArtistName());
        notificationIntent.putExtra(MainActivity.IS_DISPLAYING_RECENT_ALBUMS_NAME,true);

        PendingIntent notificationPendingIntent =
                PendingIntent.getActivity(
                        context,
                        0,
                        notificationIntent,
                        PendingIntent.FLAG_CANCEL_CURRENT
                );


        mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_album_notification)
                        .setContentTitle("New Release!")
                        .setContentIntent(notificationPendingIntent)
                        .setAutoCancel(true);

        Future<Bitmap> future = Ion.with(context)
                .load(weakAlbum.artworkUrl).asBitmap();
        Bitmap bitmap = null;
        try
        {
            bitmap = future.get();
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        } catch (ExecutionException e)
        {
            e.printStackTrace();
        }

        mBuilder.setContentText(weakAlbum.getArtistName());
        if(bitmap != null)  mBuilder.setLargeIcon(bitmap);
    }


    private void parseQueries(String... artists)
    {
        artistsWithNewAlbum.clear();
        new JSONParse().execute(artists);
    }

    private void sendNotifications(WeakAlbum[] albums)
    {
        WeakAlbum[] weakAlbums = getNotificationAlbums(albums);

        if(weakAlbums.length > 1)
        {
            buildNotification(weakAlbums);
        }
        else
        {
            buildNotification(weakAlbums[0]);
        }

        NotificationManager notificationManager =
                (NotificationManager)context.getSystemService(context.NOTIFICATION_SERVICE);
        Notification notification = mBuilder.build();

        if(shouldRing())
        {
            notification.defaults |= Notification.DEFAULT_SOUND;
        }
        if(shouldVibrate())
        {
            notification.defaults |= Notification.DEFAULT_VIBRATE;
        }
        notificationManager.notify(001, notification);
    }

    private WeakAlbum[] getNotificationAlbums(WeakAlbum[] albums)
    {
        ArrayList<WeakAlbum> albumList = new ArrayList<WeakAlbum>();
        StringBuilder sb = new StringBuilder();
        final String collectionIDs = retrieveCollectionIDs();

        for(WeakAlbum weakAlbum : albums)
        {
            if(!collectionIDs.contains(weakAlbum.getCollectionID()))
            {
                albumList.add(weakAlbum);
                sb.append(weakAlbum.getCollectionID());
            }
        }

        writeToFile(sb.toString());

        return albumList.toArray(new WeakAlbum[albumList.size()]);
    }

    private boolean shouldVibrate()
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean("notifications_vibrate",false);
    }

    private boolean shouldRing()
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean("notifications_sound",false);
    }

    private void writeToFile(String text)
    {
        FileOutputStream outputStream;

        try
        {
            outputStream = context.openFileOutput
                    (MainActivity.COLLECTION_ID_FILE, Context.MODE_APPEND);
            outputStream.write(text.getBytes());
            outputStream.close();

        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    private String retrieveCollectionIDs()
    {
        StringBuilder stringBuilder = new StringBuilder();
        Scanner in;
        try
        {
            in = new Scanner(new File(context.getFilesDir(), MainActivity.COLLECTION_ID_FILE));
            while(in.hasNext())
            {
                stringBuilder.append(in.next() + " ");
            }
            in.close();
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    private boolean isDateRecent(String releaseDate)
    {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Calendar thirtyDaysAgo = Calendar.getInstance();
        thirtyDaysAgo.add(Calendar.DAY_OF_MONTH,-30);


        Calendar releaseDateCalendar = Calendar.getInstance();
        try
        {
            releaseDateCalendar.setTime(
                    simpleDateFormat.parse(releaseDate.substring(0,10)));

            if(releaseDateCalendar.after(thirtyDaysAgo))
            {
                return true;
            }
        } catch (ParseException e)
        {
            e.printStackTrace();
        }

        return false;
    }


    private class JSONParse extends AsyncTask<String, Void, JSONObject[]>
    {
        String queries[];


        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        @Override
        protected JSONObject[] doInBackground(String... args)
        {
            JSONObject[] jsonArray = null;
            queries = args;

            try
            {
                jsonArray = new JSONObject[args.length];
                for(int i = 0;i < args.length;i++)
                {
                    jsonArray[i] = new JSONParser().readJsonFromUrl("https://itunes.apple.com/search?term=" + args[i].replaceAll("\\s","+") + "&entity=album");
                }

            } catch (IOException e)
            {
                e.printStackTrace();
            } catch (JSONException e)
            {
                e.printStackTrace();
            }
            return jsonArray;
        }

        @Override
        protected void onPostExecute(JSONObject[] jsonObjects)
        {

            for(int i = 0; i < jsonObjects.length;i++)
            {
                try
                {
                    if(jsonObjects[i] != null && jsonObjects[i].getInt("resultCount") > 0)
                    {
                        JSONArray jsonArray = (JSONArray)jsonObjects[i].get("results");

                        for(int k = 0;k < jsonArray.length();k++)
                        {
                            JSONObject jsonAlbum = (JSONObject)jsonArray.get(k);
                            String releaseDate = jsonAlbum.getString("releaseDate");
                            String artistName = jsonAlbum.getString("artistName");
                            String artworkUrl = jsonAlbum.getString("artworkUrl100");
                            String collectionID = jsonAlbum.getString("collectionId");

                            if(artistName.equals(queries[i]) && isDateRecent(releaseDate))
                            {

                                artistsWithNewAlbum.add(new WeakAlbum
                                        (artistName, artworkUrl, releaseDate,collectionID));
                                break;
                            }
                        }
                    }

                } catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }



            if(!artistsWithNewAlbum.isEmpty())
            {
                WeakAlbum[] albumArray =
                        artistsWithNewAlbum.toArray(new WeakAlbum[artistsWithNewAlbum.size()]);
                sendNotifications(albumArray);
            }
            Log.i("LIST: ", artistsWithNewAlbum.toString());
        }
    }

    private class WeakAlbum
    {
        private String artistName;
        private String artworkUrl;
        private String releaseDate;
        private String collectionID;


        public WeakAlbum(String artistName,String url,String date, String collectionID)
        {
            this.artistName = artistName;
            this.artworkUrl = url;
            this.releaseDate = date;
            this.collectionID = collectionID;
        }

        public String getArtistName()
        {
            return artistName;
        }

        public String getArtworkUrl()
        {
            return artworkUrl;
        }

        public String getReleaseDate()
        {
            return releaseDate;
        }

        public String getCollectionID()
        {
            return collectionID;
        }

        @Override
        public String toString()
        {
            return artistName;
        }
    }
}


