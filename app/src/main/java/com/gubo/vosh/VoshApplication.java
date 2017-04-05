
package com.gubo.vosh;

import android.app.*;

import gubo.slipwire.*;

/**
 * Created by GUBO on 4/4/2017.
 */
public class VoshApplication extends Application
{
    @Override
    public void onCreate() {
        super.onCreate();

        DBG.m( "Vosh V" + BuildConfig.VERSION_NAME );
        DBG.verbose = true;

        final AppComponent appComponent = com.gubo.vosh.DaggerAppComponent.builder()
                .appModule( new AppModule( this ) )
                .netModule( new NetModule() )
                .build();
        Vosh.appComponent = appComponent;
    }
}
