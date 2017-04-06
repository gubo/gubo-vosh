
package com.gubo.vosh.model;

import com.google.gson.annotations.*;

/**
 * Created by GUBO on 4/5/2017.
 */
public class RelevanceService
{
    private @SerializedName( "context" ) String context;
    private @SerializedName( "persistentRanking" ) String persistentRanking;
    private @SerializedName( "treatment" ) String treatment;

    public String getContext() {
        return context;
    }
    public void setContext( final String context ) {
        this.context = context;
    }

    public String getPersistentRanking() {
        return persistentRanking;
    }
    public void setPersistentRanking( final String persistentRanking ) {
        this.persistentRanking = persistentRanking;
    }

    public String getTreatment() {
        return treatment;
    }
    public void setTreatment( final String treatment ) {
        this.treatment = treatment;
    }
}
