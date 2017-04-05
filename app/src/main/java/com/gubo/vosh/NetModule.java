
package com.gubo.vosh;

import java.io.*;
import javax.inject.*;
import java.util.concurrent.*;

import com.google.gson.*;

import dagger.*;

import okhttp3.*;
import okhttp3.logging.*;

import retrofit2.*;
import retrofit2.converter.gson.*;
import com.jakewharton.retrofit2.adapter.rxjava2.*;

import gubo.slipwire.*;

/**
 * Created by GUBO on 4/4/2017.
 */
@Module
public class NetModule
{
    @Provides
    @AppScope
    @Named( "baseHttpRetrofitAdapter" )
    Retrofit provideBaseHttpRetrofitAdapter() {
        final class Log implements HttpLoggingInterceptor.Logger {
            @Override
            public void log( final String message ) {
                DBG.m( message );
            }
        }

        final ExecutorService executorService = Executors.newCachedThreadPool();

        final HttpLoggingInterceptor logger = new HttpLoggingInterceptor( new Log() );
        logger.setLevel( DBG.verbose ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE );

        final Interceptor debuggingInterceptor = new Interceptor() {
            @Override
            public okhttp3.Response intercept( final Chain chain ) throws IOException {
                final Request request = chain.request();
                final okhttp3.Response response = chain.proceed( request );

                /*
                final String E201 = "{ \"apiVersion\": \"1\",\"result\": \"Error\",\"error\": { \"errorCode\": \"201\",\"message\": \"No matching account was found.\" } }";
                final ResponseBody body = ResponseBody.create( response.body().contentType(),E201 );
                return response.newBuilder().code( 201 ).message( "INTERCEPTED" ).body( body ).build();
                */
                return response;
            }
        };

        final Interceptor errorInterceptor = new Interceptor() {
            @Override
            public okhttp3.Response intercept( final Chain chain ) throws IOException {
                final Request request = chain.request();
                final okhttp3.Response response = chain.proceed( request );

                try {
                    final int code = response.code();
                    final String version = request.header( "Version" );
                    switch ( code ) {
                    case 401:
                    case 426:
                        //new Intercept( code,version );
                        break;
                    }
                } catch ( Exception x ) {
                    x.printStackTrace();
                }

                return response;
            }
        };

        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout( 15000,TimeUnit.MILLISECONDS )
                //.addInterceptor( debuggingInterceptor )
                .addInterceptor( errorInterceptor )
                .addInterceptor( logger )
                .build();

        final Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();

        final Retrofit restadapter = new Retrofit.Builder()
                .addCallAdapterFactory( RxJava2CallAdapterFactory.create() )
                .addConverterFactory( GsonConverterFactory.create( gson  ))
                .baseUrl( Vosh.APIENDPOINT )
                .client( okHttpClient )
                .callbackExecutor( executorService )
                .build();

        return restadapter;
    }
}
