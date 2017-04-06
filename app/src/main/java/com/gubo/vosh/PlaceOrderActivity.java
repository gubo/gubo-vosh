
package com.gubo.vosh;

import java.util.*;

import android.location.Address;
import android.os.*;
import android.databinding.*;
import android.support.v7.app.*;
import android.support.annotation.*;

import com.gubo.vosh.databinding.PlaceorderBinding;

/**
 * Created by GUBO on 4/4/2017.
 */
public class PlaceOrderActivity extends AppCompatActivity
{
    public ObservableArrayList<Object> times = new ObservableArrayList<>();
    public ObservableInt timeselection = new ObservableInt();
    public ObservableField<String> address1 = new ObservableField<>();
    public ObservableField<String> address2 = new ObservableField<>();
    public ObservableField<String> address3 = new ObservableField<>();
    public ObservableField<String> summary = new ObservableField<>();
    public ObservableField<String> promo = new ObservableField<>();
    public ObservableField<String> cardno = new ObservableField<>();
    public ObservableField<String> cardexpiration = new ObservableField<>();
    public ObservableField<String> cardcode = new ObservableField<>();
    public ObservableField<String> total = new ObservableField<>( "TOTAL CHARGE: $27.00" );

    @Override
    protected void onCreate( @Nullable final Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );

        PlaceorderBinding binding = DataBindingUtil.setContentView( this,R.layout.placeorder );
        binding.setBinding( this );

        times.addAll( Arrays.asList( getResources().getStringArray( R.array.times ) ) );

        final Address address = getIntent().getParcelableExtra( "address" );
        if ( address != null ) {
            address1.set( address.getAddressLine( 0 ) );
            address2.set( address.getAddressLine( 1 ) );
            address3.set( address.getAddressLine( 2 ) );
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
