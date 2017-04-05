
package com.gubo.vosh.home;

import android.os.*;
import android.view.*;
import android.databinding.*;
import android.support.v4.app.*;
import android.support.annotation.*;

import com.gubo.vosh.R;
import com.gubo.vosh.databinding.HomeBinding;

/**
 * Created by GUBO on 4/4/2017.
 */
public class HomeFragment extends Fragment
{
    public static HomeFragment newInstance() {
        final HomeFragment homeFragment = new HomeFragment();
        return homeFragment;
    }

    @Override
    public void onCreate( @Nullable final Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
    }

    @Nullable
    @Override
    public View onCreateView( final LayoutInflater inflater,@Nullable final ViewGroup container,@Nullable final Bundle savedInstanceState ) {
        final HomeBinding binding = DataBindingUtil.inflate( inflater,R.layout.home,container,false );

        final View view = binding.getRoot();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
