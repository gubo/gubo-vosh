
package com.gubo.vosh;

import com.google.firebase.messaging.*;

import gubo.slipwire.*;

/**
 * Created by GUBO on 4/4/2017.
 */
public class VoshFirebaseMessagingService extends FirebaseMessagingService
{
    @Override
    public void onMessageReceived( final RemoteMessage remoteMessage ) {
        final RemoteMessage.Notification notification = remoteMessage.getNotification();
        if ( notification != null ) {
            final String body = notification.getBody();
            DBG.v( "VoshFirebaseMessagingService.onMessageReceived " + body );
        }
    }
}
