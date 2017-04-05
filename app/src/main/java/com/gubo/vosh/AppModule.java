
package com.gubo.vosh;

import android.content.*;

import dagger.*;

import com.gubo.vosh.database.*;

/**
 * Created by GUBO on 4/4/2017.
 */
@Module
public class AppModule
{
    private final Context context;

    public AppModule( final Context context ) {
        this.context = context;
    }

    @Provides
    @AppScope
    Context provideContext() { return context; }

    @Provides
    @AppScope
    EventBus provideEventBus() { return new EventBus(); }

    @Provides
    @AppScope
    Database provideDatabase() { return new Database(); }

    @Provides
    @AppScope
    Domain provideDomain() { return new Domain(); }
}
