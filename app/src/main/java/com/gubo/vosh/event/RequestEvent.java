
package com.gubo.vosh.event;

import android.location.*;

/**
 * Created by GUBO on 4/4/2017.
 */
public class RequestEvent
{
    public final Address address;

    public RequestEvent( final Address address ) {
        this.address = address;
    }
}
