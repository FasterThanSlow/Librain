package com.greenkeycompany.librain.app.util;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;

/**
 * Created by tert0 on 11.09.2017.
 */

public class GoogleApiUtil {

    private static GoogleApiClient googleApiClient;

    public static GoogleApiClient getGoogleApi(final Context context) {
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(context)
                    .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                        @Override
                        public void onConnected(@Nullable Bundle bundle) {
                            Toast.makeText(context, "GoogleApiClient" + "onConnected", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onConnectionSuspended(int i) {
                            Toast.makeText(context, "GoogleApiClient" + "onConnectionSuspended " + i, Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                            Toast.makeText(context, "GoogleApiClient" + "onConnectionFailed " + connectionResult.getErrorCode(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addApi(Games.API).addScope(Games.SCOPE_GAMES)
                    .build();
        }

        return googleApiClient;
    }
}
