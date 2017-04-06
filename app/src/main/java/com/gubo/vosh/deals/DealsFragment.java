
package com.gubo.vosh.deals;

import java.util.*;
import javax.inject.*;

import android.os.*;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.TextViewCompat;
import android.view.*;
import android.content.*;
import android.databinding.*;
import android.support.v4.app.*;
import android.support.v7.widget.*;
import android.support.annotation.*;
import android.widget.ImageView;
import android.widget.TextView;

import io.reactivex.Observer;
import io.reactivex.disposables.*;

import com.squareup.picasso.*;

import gubo.slipwire.*;

import com.gubo.vosh.*;
import com.gubo.vosh.R;
import com.gubo.vosh.model.*;
import com.gubo.vosh.databinding.DealsBinding;

/**
 * Created by GUBO on 4/4/2017.
 */
public class DealsFragment extends Fragment
{
    private final List<Deal> deals = new ArrayList<>();

    private SwipeRefreshLayout swipeRefreshLayout;
    private GridLayoutManager gridLayoutManager;
    private DealsAdapter dealsAdapter;
    private RecyclerView recyclerView;
    private Disposable disposable;

    @Inject Context context;
    @Inject Domain domain;

    public static DealsFragment newInstance() {
        final DealsFragment dealsFragment = new DealsFragment();
        return dealsFragment;
    }

    @Override
    public void onCreate( @Nullable final Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        Vosh.appComponent.inject( this );
    }

    @Nullable
    @Override
    public View onCreateView( final LayoutInflater inflater,@Nullable final ViewGroup container,@Nullable final Bundle savedInstanceState ) {
        final DealsBinding binding = DataBindingUtil.inflate( inflater,R.layout.deals,container,false );

        final View view = binding.getRoot();

        gridLayoutManager = new GridLayoutManager( context,1 );
        swipeRefreshLayout = ( SwipeRefreshLayout )view.findViewById( R.id.dealsswiperefreshlayout );
        dealsAdapter = new DealsAdapter();


        recyclerView = ( RecyclerView)view.findViewById( R.id.dealsrecyclerview );
        recyclerView.setLayoutManager( gridLayoutManager );
        recyclerView.setAdapter( dealsAdapter );
        recyclerView.setHasFixedSize( true );

        fetchDeals();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if ( disposable != null ) {
            disposable.dispose();
        }
    }

    private void fetchDeals() {
        deals.clear();

        final Observer<Deal> observer = new Observer<Deal>() {
            @Override
            public void onSubscribe( final Disposable d ) {
                disposable = d;
            }

            @Override
            public void onNext( final Deal deal ) {
                if ( deal != null ) {
                    final int indexOf = deals.indexOf( deal );
                    if ( indexOf == -1 ) {
                        deals.add( deal );
                    } else {
                        deals.set( indexOf,deal );
                    }
                }
            }

            @Override
            public void onError( final Throwable x ) {
                DBG.m( x );
            }

            @Override
            public void onComplete() {
                update();
            }
        };

        domain.getDeals( 0,100 ).subscribe( observer );
    }

    private void update() {
        dealsAdapter.setItemCount( deals.size() );
        dealsAdapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing( false );
    }

    private class DealHolder extends RecyclerView.ViewHolder
    {
        final ImageView imageView;
        final TextView merchantView;
        final TextView titleView;

        DealHolder( final View view ) {
            super( view );
            imageView = ( ImageView)view.findViewById( R.id.dealimageview );
            merchantView = ( TextView)view.findViewById( R.id.dealmerchantview );
            titleView = ( TextView)view.findViewById( R.id.dealtitleview );
        }

        void bind( final int position ) {
            try {
                final Deal deal = deals.get( position );
                final String url = deal.getLargeImageUrl();
                Picasso.with( itemView.getContext() ).load( url ).into( imageView );
                merchantView.setText( deal.getShortAnnouncementTitle() );
                titleView.setText( deal.getAnnouncementTitle() );
            } catch ( Exception x ) {
                DBG.m( x );
            }
        }
    }

    private class DealsAdapter extends RecyclerView.Adapter<DealHolder>
    {
        private int itemCount;

        @Override
        public int getItemCount() {
            return itemCount;
        }

        void setItemCount( final int itemCount ) {
            this.itemCount = itemCount;
        }

        @Override
        public DealHolder onCreateViewHolder( final ViewGroup parent,final int viewtype ) {
            final View view = LayoutInflater.from( parent.getContext() ).inflate( R.layout.deal,parent,false );
            return new DealHolder( view );
        }

        @Override
        public void onBindViewHolder( final DealHolder holder,final int position ) {
            holder.bind( position );
        }
    }
}
