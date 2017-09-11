package com.greenkeycompany.librain.app.util;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;

/**
 * Created by tert0 on 11.09.2017.
 */

public class GoogleApiUtil {

    private static GoogleApiClient googleApiClient;

    public static GoogleApiClient getGoogleApi(Context context) {
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(context)
                    .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                        @Override
                        public void onConnected(@Nullable Bundle bundle) {
                            Log.d("GoogleApiClient", "onConnected");
                        }

                        @Override
                        public void onConnectionSuspended(int i) {
                            Log.d("GoogleApiClient", "onConnectionSuspended " + i);
                        }
                    })
                    .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                            Log.d("GoogleApiClient", "onConnectionFailed " + connectionResult.getErrorMessage());
                        }
                    })
                    .addApi(Games.API).addScope(Games.SCOPE_GAMES)
                    .build();
        }

        return googleApiClient;
    }
}
