
package com.gubo.vosh.webapi;

import java.util.*;
import javax.inject.*;

import com.google.gson.annotations.*;

import io.reactivex.Observable;
import io.reactivex.functions.*;
import io.reactivex.schedulers.*;
import io.reactivex.observables.*;
import io.reactivex.android.schedulers.*;

import retrofit2.*;
import retrofit2.http.*;

import com.gubo.vosh.*;
import com.gubo.vosh.model.*;

/**
 * Created by GUBO on 4/5/2017.
 */
public class GETDeals
{
    public static class Response
    {
        public @SerializedName( "deals" ) List<Deal> deals;
        public @SerializedName( "facets" ) List<Facet> facets;
        public @SerializedName( "pagination" ) Pagination pagination;
        public @SerializedName( "relevanceService" ) RelevanceService relevanceService;
        public @SerializedName( "dealSubsetAttributes" ) List<DealSubsetAttributes> dealSubsetAttributes;
    }

    /*
     * https://partner-api.groupon.com/deals.json?tsToken=US_AFF_0_201236_212556_0&filters=category:automotive&offset=0&limit=5
     *
     * https://www.groupon.com/browse/los-angeles?lat=34.0522342&lng=-118.2436849&locality=Los+Angeles&administrative_area=CA&address=Los+Angeles%2C+CA&query=car+wash&hasLocCookie=true&locale=en_US
     */

    private interface deals {
        @Headers( { "User-Agent: com.statusnotquo.csi" } )
        @GET( "/deals.json" )
        public Observable<Response> get(
                @Query( "tsToken" ) String tsToken,
                @Query( "filters" ) String filters,
                @Query( "query" ) String query,
                @Query( "offset" ) int offset,
                @Query( "limit" ) int limit
        );
    }

    @Inject @Named( "dealHttpRetrofitAdapter" ) Retrofit restAdapter;

    public GETDeals() {
        Vosh.appComponent.inject( this );
    }

    public Observable<Deal> fetch( final int offset,final int limit ) {
        Observable<Deal> observable = Observable.empty();

        final Function<Response,Observable<Deal>> mapper = new Function<Response,Observable<Deal>>() {
            @Override
            public Observable<Deal> apply( final Response response ) throws Exception {
                return GroupedObservable.fromIterable( response.deals );
            }
        };

        final String tsToken = "US_AFF_0_201236_212556_0";
        final String filters = "category:automotive";
        final String query = "car wash";

        observable = restAdapter.create( deals.class )
                .get( tsToken,filters,query,offset,limit )
                .subscribeOn( Schedulers.io() )
                .observeOn( AndroidSchedulers.mainThread() )
                .concatMap( mapper );

        return observable;
    }
}
