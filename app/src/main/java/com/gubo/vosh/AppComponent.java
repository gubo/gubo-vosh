
package com.gubo.vosh;

import dagger.*;

import com.gubo.vosh.deals.*;
import com.gubo.vosh.home.*;
import com.gubo.vosh.wash.*;

/**
 * Created by GUBO on 4/4/2017.
 */
@AppScope
@Component( modules = { AppModule.class,NetModule.class } )
public interface AppComponent
{
    void inject( MainActivity mainActivity );
    void inject( HomeFragment homeFragment );
    void inject( WashFragment washFragment );
    void inject( DealsFragment dealsFragment );
}
