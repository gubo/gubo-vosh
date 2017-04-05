
package com.gubo.vosh;

import java.util.concurrent.*;

import android.os.*;
import android.content.*;
import android.support.v7.app.*;
import android.support.annotation.*;

import io.reactivex.*;
import io.reactivex.schedulers.*;
import io.reactivex.disposables.*;
import io.reactivex.android.schedulers.*;

/**
 * Created by GUBO on 4/4/2017.
 */
public class LaunchActivity extends AppCompatActivity
{
    @Override
    protected void onCreate( @Nullable final Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.splash  );
    }

    @Override
    protected void onResume() {
        super.onResume();

        Observer<String> observer = new Observer<String>() {
            @Override
            public void onSubscribe( final Disposable d ) {

            }

            @Override
            public void onNext( final String value ) {

            }

            @Override
            public void onComplete() {
                go();
            }

            @Override
            public void onError( final Throwable e ) {

            }
        };

        Observable.just( "go" )
                .subscribeOn( Schedulers.computation() )
                .observeOn( AndroidSchedulers.mainThread() )
                .delay( 1000,TimeUnit.MILLISECONDS )
                .subscribe( observer );
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void go() {
        final Intent intent = new Intent( this,MainActivity.class );
        startActivity( intent );
        finish();
    }
}
