
package com.gubo.vosh;

import io.reactivex.*;

import gubo.slipwire.*;

import com.gubo.vosh.model.*;
import com.gubo.vosh.webapi.*;

/**
 * Created by GUBO on 4/4/2017.
 */
public class Domain
{
    public void ping() {
        DBG.m( "Domain.ping" );
    }

    public Observable<Deal> getDeals( final int offset,final int limit ) {
        return new GETDeals().fetch( offset,limit );
    }
}
