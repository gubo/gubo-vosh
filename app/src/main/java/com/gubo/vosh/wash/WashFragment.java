
package com.gubo.vosh.wash;

import javax.inject.*;

import android.os.*;
import android.view.*;
import android.content.*;
import android.location.*;
import android.databinding.*;
import android.support.v4.app.*;
import android.support.annotation.*;

import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;

import gubo.slipwire.*;

import com.gubo.vosh.*;
import com.gubo.vosh.R;
import com.gubo.vosh.event.*;
import com.gubo.vosh.databinding.WashBinding;

import java.util.List;
import java.util.Locale;

/**
 * Created by GUBO on 4/4/2017.
 */
public class WashFragment extends Fragment implements OnMapReadyCallback
{
    public static WashFragment newInstance() {
        final WashFragment mapFragment = new WashFragment();
        return mapFragment;
    }

    private GoogleMap googleMap;
    private MapView mapView;

    @Inject EventBus eventBus;
    @Inject Context context;
    @Inject Domain domain;

    @Override
    public void onCreate( @Nullable final Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        Vosh.appComponent.inject( this );
    }

    @Nullable
    @Override
    public View onCreateView( final LayoutInflater inflater,@Nullable final ViewGroup container,@Nullable final Bundle savedInstanceState ) {
        final WashBinding binding = DataBindingUtil.inflate( inflater,R.layout.wash,container,false );
        binding.setBinding( this );
        final View view = binding.getRoot();
        mapView = ( MapView)view.findViewById( R.id.washmap );
        mapView.getMapAsync( this );
        return view;
    }

    @Override
    public void onActivityCreated( @Nullable final Bundle savedInstanceState ) {
        super.onActivityCreated( savedInstanceState );
        mapView.onCreate( savedInstanceState );
    }

    @Override
    public void onMapReady( final GoogleMap googleMap ) {
        try {
            this.googleMap = googleMap;

            android.location.LocationManager locationManager = ( android.location.LocationManager )getContext().getSystemService( Context.LOCATION_SERVICE );
            final android.location.Location location = locationManager.getLastKnownLocation( LocationManager.NETWORK_PROVIDER );

            final LatLng spot = new LatLng( location.getLatitude(),location.getLongitude() );
            googleMap.setMapType( GoogleMap.MAP_TYPE_NORMAL );
            googleMap.addMarker( new MarkerOptions().position( spot ).title( "Current Location" ) );
            final CameraUpdate center = CameraUpdateFactory.newLatLng( spot );
            final CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);
            googleMap.setMyLocationEnabled( true );
            googleMap.moveCamera( center );
            googleMap.animateCamera( zoom );
        } catch ( SecurityException x  ) {
            DBG.m( x );
        } catch ( Throwable x ) {
            DBG.m( x );
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        domain.ping();
        mapView.onResume();
    }

    public void onRequest() {
        if ( googleMap == null ) { return; }

        Address address = null;
        try {
            final Location location = googleMap.getMyLocation();
            final Geocoder geocoder = new Geocoder( context,Locale.getDefault() );
            final List<Address> addresses = geocoder.getFromLocation( location.getLatitude(),location.getLongitude(),1 );
            if ( (addresses != null)  && (addresses.size() > 0) ) {
                address = addresses.get( 0 );
            }
        } catch ( Exception x ) {
            DBG.m( x );
        }
        eventBus.send( new RequestEvent( address ) );
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }
}
