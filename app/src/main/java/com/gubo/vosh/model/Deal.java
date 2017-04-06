
package com.gubo.vosh.model;

import com.google.gson.annotations.*;

/**
 * Created by GUBO on 4/5/2017.
 */
public class Deal
{
    private @SerializedName( "id" ) String id;
    private @SerializedName( "uuid" ) String uuid;
    private @SerializedName( "dealUrl" ) String dealUrl;
    private @SerializedName( "title" ) String title;
    private @SerializedName( "announcementTitle" ) String announcementTitle;
    private @SerializedName( "shortAnnouncementTitle" ) String shortAnnouncementTitle;
    private @SerializedName( "finePrint" ) String finePrint;
    private @SerializedName( "smallImageUrl" ) String smallImageUrl;
    private @SerializedName( "mediumImageUrl" ) String mediumImageUrl;
    private @SerializedName( "largeImageUrl" ) String largeImageUrl;

    public String getId() {
        return id;
    }
    public void setId( final String id ) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }
    public void setUuid( final String uuid ) {
        this.uuid = uuid;
    }

    public String getDealUrl() {
        return dealUrl;
    }
    public void setDealUrl( final String dealUrl ) {
        this.dealUrl = dealUrl;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle( final String title ) {
        this.title = title;
    }

    public String getAnnouncementTitle() {
        return announcementTitle;
    }
    public void setAnnouncementTitle( final String announcementTitle ) {
        this.announcementTitle = announcementTitle;
    }

    public String getShortAnnouncementTitle() {
        return shortAnnouncementTitle;
    }
    public void setShortAnnouncementTitle( final String shortAnnouncementTitle ) {
        this.shortAnnouncementTitle = shortAnnouncementTitle;
    }

    public String getFinePrint() {
        return finePrint;
    }
    public void setFinePrint( final String finePrint ) {
        this.finePrint = finePrint;
    }

    public String getSmallImageUrl() {
        return smallImageUrl;
    }
    public void setSmallImageUrl( final String smallImageUrl ) {
        this.smallImageUrl = smallImageUrl;
    }

    public String getMediumImageUrl() {
        return mediumImageUrl;
    }
    public void setMediumImageUrl( final String mediumImageUrl ) {
        this.mediumImageUrl = mediumImageUrl;
    }

    public String getLargeImageUrl() {
        return largeImageUrl;
    }
    public void setLargeImageUrl( final String largeImageUrl ) {
        this.largeImageUrl = largeImageUrl;
    }

    @Override
    public boolean equals( final Object o ) {
        if ( o instanceof Deal ) {
            final Deal that = ( Deal)o;
            return ( (this.id != null) && this.id.equals( that.id ) );
        }
        return false;
    }

    @Override
    public int hashCode() {
        return ( id != null ? id.hashCode() : 0 );
    }
}
