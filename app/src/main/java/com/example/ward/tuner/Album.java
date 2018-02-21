package com.example.ward.tuner;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by 15wardj on 10/6/2014.
 */
@SuppressWarnings("unused")
public class Album implements Serializable
{
    private String collectionExplicitness;

    private String collectionType;

    private String artworkUrl60;

    private String primaryGenreName;

    private String collectionId;

    private String wrapperType;

    private String collectionViewUrl;

    private String copyright;

    private String currency;

    private String country;

    private String releaseDate;

    private String artistId;

    private String artistViewUrl;

    private String artistName;

    private String collectionCensoredName;

    private String artworkUrl100;

    private String amgArtistId;

    private String collectionName;

    private String trackCount;

    @Override
    public String toString()
    {
        return collectionCensoredName;
    }

    private String collectionPrice;

    public Album(JSONObject jsonObject)
    {
//        try
//        {
//            collectionExplicitness = jsonObject.getString("collectionExplicitness");
//        } catch (JSONException e)
//        {
//            e.printStackTrace();
//        }
//        try
//        {
//            collectionType = jsonObject.getString("collectionType");
//        } catch (JSONException e)
//        {
//            e.printStackTrace();
//        }
//        try
//        {
//            primaryGenreName = jsonObject.getString("primaryGenreName");
//        } catch (JSONException e)
//        {
//            e.printStackTrace();
//        }
        try
        {
            collectionId = jsonObject.getString("collectionId");
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
//        try
//        {
//            wrapperType = jsonObject.getString("wrapperType");
//        } catch (JSONException e)
//        {
//            e.printStackTrace();
//        }
//        try
//        {
//            copyright = jsonObject.getString("copyright");
//        } catch (JSONException e)
//        {
//            e.printStackTrace();
//        }
//        try
//        {
//            currency = jsonObject.getString("currency");
//        } catch (JSONException e)
//        {
//            e.printStackTrace();
//        }
//        try
//        {
//            country = jsonObject.getString("country");
//        } catch (JSONException e)
//        {
//            e.printStackTrace();
//        }
//        try
//        {
//            artistId = jsonObject.getString("artistId");
//        } catch (JSONException e)
//        {
//            e.printStackTrace();
//        }
//        try
//        {
//            amgArtistId = jsonObject.getString("amgArtistId");
//        } catch (JSONException e)
//        {
//            e.printStackTrace();
//        }
//        try
//        {
//            trackCount = jsonObject.getString("trackCount");
//        } catch (JSONException e)
//        {
//            e.printStackTrace();
//        }
//        try
//        {
//            collectionPrice = jsonObject.getString("collectionPrice");
//        } catch (JSONException e)
//        {
//            e.printStackTrace();
//        }
        try
        {
            collectionName = jsonObject.getString("collectionName");
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
        try
        {
            artistViewUrl = jsonObject.getString("artistViewUrl");
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
        try
        {
            artistName = jsonObject.getString("artistName");
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
        try
        {
            collectionCensoredName = jsonObject.getString("collectionCensoredName");
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
        try
        {
            artworkUrl100 = jsonObject.getString("artworkUrl100");
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
        try
        {
            releaseDate = jsonObject.getString("releaseDate");
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
        try
        {
            collectionViewUrl = jsonObject.getString("collectionViewUrl");
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
        try
        {
            artworkUrl60 = jsonObject.getString("artworkUrl60");
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    public Album()
    {
        collectionCensoredName = "No Albums Found";
    }

    public String getCollectionExplicitness ()
    {
        return collectionExplicitness;
    }

    public void setCollectionExplicitness (String collectionExplicitness)
    {
        this.collectionExplicitness = collectionExplicitness;
    }

    public String getCollectionType ()
    {
        return collectionType;
    }

    public void setCollectionType (String collectionType)
    {
        this.collectionType = collectionType;
    }

    public String getArtworkUrl60 ()
    {
        return artworkUrl60;
    }

    public void setArtworkUrl60 (String artworkUrl60)
    {
        this.artworkUrl60 = artworkUrl60;
    }

    public String getPrimaryGenreName ()
    {
        return primaryGenreName;
    }

    public void setPrimaryGenreName (String primaryGenreName)
    {
        this.primaryGenreName = primaryGenreName;
    }

    public String getCollectionId ()
    {
        return collectionId;
    }

    public void setCollectionId (String collectionId)
    {
        this.collectionId = collectionId;
    }

    public String getWrapperType ()
    {
        return wrapperType;
    }

    public void setWrapperType (String wrapperType)
    {
        this.wrapperType = wrapperType;
    }

    public String getCollectionViewUrl ()
    {
        return collectionViewUrl;
    }

    public void setCollectionViewUrl (String collectionViewUrl)
    {
        this.collectionViewUrl = collectionViewUrl;
    }

    public String getCopyright ()
    {
        return copyright;
    }

    public void setCopyright (String copyright)
    {
        this.copyright = copyright;
    }

    public String getCurrency ()
    {
        return currency;
    }

    public void setCurrency (String currency)
    {
        this.currency = currency;
    }

    public String getCountry ()
    {
        return country;
    }

    public void setCountry (String country)
    {
        this.country = country;
    }

    public String getReleaseDate ()
    {
        return releaseDate;
    }

    public void setReleaseDate (String releaseDate)
    {
        this.releaseDate = releaseDate;
    }

    public String getArtistId ()
    {
        return artistId;
    }

    public void setArtistId (String artistId)
    {
        this.artistId = artistId;
    }

    public String getArtistViewUrl ()
    {
        return artistViewUrl;
    }

    public void setArtistViewUrl (String artistViewUrl)
    {
        this.artistViewUrl = artistViewUrl;
    }

    public String getArtistName ()
    {
        return artistName;
    }

    public void setArtistName (String artistName)
    {
        this.artistName = artistName;
    }

    public String getCollectionCensoredName ()
    {
        return collectionCensoredName;
    }

    public void setCollectionCensoredName (String collectionCensoredName)
    {
        this.collectionCensoredName = collectionCensoredName;
    }

    public String getArtworkUrl100 ()
    {
        return artworkUrl100;
    }

    public void setArtworkUrl100 (String artworkUrl100)
    {
        this.artworkUrl100 = artworkUrl100;
    }

    public String getAmgArtistId ()
    {
        return amgArtistId;
    }

    public void setAmgArtistId (String amgArtistId)
    {
        this.amgArtistId = amgArtistId;
    }

    public String getCollectionName ()
    {
        return collectionName;
    }

    public void setCollectionName (String collectionName)
    {
        this.collectionName = collectionName;
    }

    public String getTrackCount ()
    {
        return trackCount;
    }

    public void setTrackCount (String trackCount)
    {
        this.trackCount = trackCount;
    }

    public String getCollectionPrice ()
    {
        return collectionPrice;
    }

    public void setCollectionPrice (String collectionPrice)
    {
        this.collectionPrice = collectionPrice;
    }
}
