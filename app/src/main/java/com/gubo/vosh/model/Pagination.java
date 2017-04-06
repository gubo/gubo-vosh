
package com.gubo.vosh.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by GUBO on 4/5/2017.
 */
public class Pagination
{
    private @SerializedName( "offset" ) long offset;
    private @SerializedName( "count" ) long count;

    public long getOffset() {
        return offset;
    }
    public void setOffset( final long offset ) {
        this.offset = offset;
    }

    public long getCount() {
        return count;
    }
    public void setCount( final long count ) {
        this.count = count;
    }

    @Override
    public boolean equals( final Object o ) {
        if ( this == o ) return true;
        if ( o == null || getClass() != o.getClass() ) return false;

        final Pagination that = ( Pagination )o;

        if ( offset != that.offset ) return false;
        return count == that.count;

    }

    @Override
    public int hashCode() {
        int result = ( int )( offset ^ ( offset >>> 32 ) );
        result = 31 * result + ( int )( count ^ ( count >>> 32 ) );
        return result;
    }
}
