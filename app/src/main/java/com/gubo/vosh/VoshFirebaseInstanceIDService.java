
package com.gubo.vosh;

import com.google.firebase.iid.*;

import gubo.slipwire.*;

/**
 * Created by GUBO on 4/4/2017.
 */
public class VoshFirebaseInstanceIDService extends FirebaseInstanceIdService
{
    @Override
    public void onTokenRefresh() {
        try {
            final String token = FirebaseInstanceId.getInstance().getToken();
            DBG.v( "VoshFirebaseInstanceIDService.onTokenRefresh " + token );
            // TODO: register token w vosh.me server
        } catch ( Exception x ) {
            DBG.m( x );
        }
    }
}
