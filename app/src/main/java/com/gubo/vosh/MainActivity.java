
package com.gubo.vosh;

import javax.inject.*;

import android.os.*;
import android.content.*;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.support.v4.app.*;
import android.support.v7.app.*;
import android.support.v4.view.*;
import android.support.v4.widget.*;
import android.support.v7.widget.*;
import android.support.design.widget.*;
import android.support.v7.app.ActionBarDrawerToggle;

import io.reactivex.*;
import io.reactivex.functions.*;
import io.reactivex.schedulers.*;
import io.reactivex.disposables.*;
import io.reactivex.android.schedulers.*;

import gubo.slipwire.*;

import com.gubo.vosh.event.RequestEvent;
import com.gubo.vosh.home.*;
import com.gubo.vosh.wash.*;
import com.gubo.vosh.deals.*;
import com.gubo.vosh.webapi.PlaceOrder;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,ViewPager.OnPageChangeListener
{
    private AppBarLayout appBarLayout;
    private ViewPager viewPager;
    private Button mainhomebutton;
    private Button mainwashbutton;
    private Button maindealsbutton;

    private final Consumer<Object> consumer = new Consumer<Object>() {
        @Override
        public void accept( final Object o ) throws Exception {
            onEvent( o );
        }
    };
    private Disposable disposable;

    @Inject EventBus eventBus;

    @Override
    protected void onCreate( final Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        Vosh.appComponent.inject( this );
        setContentView( R.layout.main );
        DBG.m( "MainActivity.onCreate" );

        configure();
        setup();
    }

    @Override
    protected void onResume() {
        super.onResume();
        DBG.m( "MainActivity.onResume" );

        disposable = eventBus.toObservable().subscribeOn( Schedulers.io() ).observeOn( AndroidSchedulers.mainThread() ).subscribe( consumer );
    }

    @Override
    public void onBackPressed() {
        final DrawerLayout drawer = ( DrawerLayout )findViewById( R.id.maindrawerlayout );
        if ( drawer.isDrawerOpen( GravityCompat.START ) ) {
            drawer.closeDrawer( GravityCompat.START );
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu( final Menu menu ) {
        getMenuInflater().inflate( R.menu.main,menu );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected( final MenuItem item ) {
        final int id = item.getItemId();

        if ( id == R.id.action_settings ) {
            return true;
        }

        return super.onOptionsItemSelected( item );
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected( final MenuItem item ) {
        final int id = item.getItemId();

        if ( id == R.id.nav_camera ) {
        } else if ( id == R.id.nav_gallery ) {
        } else if ( id == R.id.nav_slideshow ) {
        } else if ( id == R.id.nav_manage ) {
        } else if ( id == R.id.nav_share ) {
        } else if ( id == R.id.nav_send ) {
        }

        final DrawerLayout drawer = ( DrawerLayout )findViewById( R.id.maindrawerlayout );
        drawer.closeDrawer( GravityCompat.START );
        return true;
    }

    @Override
    public void onPageScrolled( final int position,final float positionOffset,final int positionOffsetPixels ) {

    }

    @Override
    public void onPageSelected( final int position ) {
        mainhomebutton.setSelected( false );
        mainwashbutton.setSelected( false );
        maindealsbutton.setSelected( false );
        switch ( position ) {
        case 0:
            mainhomebutton.setSelected( true );
            appBarLayout.setExpanded( true );
            break;
        case 1:
            mainwashbutton.setSelected( true );
            appBarLayout.setExpanded( false );
            break;
        case 2:
            maindealsbutton.setSelected( true );
            appBarLayout.setExpanded( false );
            break;
        }
    }

    @Override
    public void onPageScrollStateChanged( final int state ) {

    }

    @Override
    protected void onPause() {
        super.onPause();

        if ( disposable != null ) {
            disposable.dispose();
            disposable = null;
        }

        DBG.m( "MainActivity.onPause" );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DBG.m( "MainActivity.onDestroy" );
    }

    /******************************************************************************************************************************************/

    private void configure() {
        final Toolbar toolbar = ( Toolbar )findViewById( R.id.maintoolbar );
        toolbar.setTitle( "VOSH" );
        setSupportActionBar( toolbar );

        final FloatingActionButton fab = ( FloatingActionButton )findViewById( R.id.mainfab );
        //fab.setOnClickListener( ... );

        final DrawerLayout drawer = ( DrawerLayout )findViewById( R.id.maindrawerlayout );
        final  ActionBarDrawerToggle toggle = new ActionBarDrawerToggle( this,drawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close );
        drawer.setDrawerListener( toggle );
        toggle.syncState();

        final NavigationView navigationView = ( NavigationView )findViewById( R.id.mainnavview );
        navigationView.setNavigationItemSelectedListener( this );

        appBarLayout = ( AppBarLayout)findViewById( R.id.mainappbar );
    }

    private void setup() {
        final MainFragmentStatePagerAdapter mainFragmentStatePagerAdapter = new MainFragmentStatePagerAdapter( getSupportFragmentManager() );
        viewPager = ( ViewPager )findViewById( R.id.mainviewpager );
        viewPager.setAdapter( mainFragmentStatePagerAdapter );
        viewPager.addOnPageChangeListener( this );
        viewPager.setOffscreenPageLimit( 3 );
        viewPager.setCurrentItem( 0 );

        mainhomebutton = ( Button)findViewById( R.id.mainhomebutton );
        mainwashbutton = ( Button)findViewById( R.id.mainwashbutton );
        maindealsbutton = ( Button)findViewById( R.id.maindealsbutton );

        final View.OnClickListener onHomeClickListener = new View.OnClickListener() {
            @Override
            public void onClick( final View v ) {
                viewPager.setCurrentItem( 0 );
            }
        };
        mainhomebutton.setOnClickListener( onHomeClickListener );

        final View.OnClickListener onWashClickListener = new View.OnClickListener() {
            @Override
            public void onClick( final View v ) {
                viewPager.setCurrentItem( 1 );
            }
        };
        mainwashbutton.setOnClickListener( onWashClickListener );

        final View.OnClickListener onDealsClickListener = new View.OnClickListener() {
            @Override
            public void onClick( final View v ) {
                viewPager.setCurrentItem( 2 );
            }
        };
        maindealsbutton.setOnClickListener( onDealsClickListener );
    }

    private void onEvent( final Object o ) {
        // TODO: make base Event and use visitor pattern ... evenet.visit( this ) or such
        if ( o instanceof RequestEvent ) {
            final Intent intent = new Intent( this,PlaceOrderActivity.class );
            startActivityForResult( intent,1 );
        }
    }

    private class MainFragmentStatePagerAdapter extends FragmentStatePagerAdapter
    {
        MainFragmentStatePagerAdapter( final android.support.v4.app.FragmentManager fragmentManager ) {
            super( fragmentManager );
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public Fragment getItem( final int position ) {
            Fragment fragment = null;
            switch ( position ) {
            case 0:
                fragment = HomeFragment.newInstance();
                break;
            case 1:
                fragment = WashFragment.newInstance();
                break;
            case 2:
                fragment = DealsFragment.newInstance();
                break;
            }
            return fragment;
        }
    }
}
