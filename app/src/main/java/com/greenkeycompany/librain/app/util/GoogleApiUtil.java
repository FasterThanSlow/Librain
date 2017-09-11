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
import com.greenkeycompany.librain.mainmenu.view.MainMenuActivity;

/**
 * Created by tert0 on 11.09.2017.
 */

public class GoogleApiUtil {

    private static GoogleApiClient googleApiClient;

    public static GoogleApiClient getGoogleApi(final @NonNull Context context, @NonNull GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener) {
        return new GoogleApiClient.Builder(context)
                .addOnConnectionFailedListener(onConnectionFailedListener)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(@Nullable Bundle bundle) {
                        Toast.makeText(context, "onConnected", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onConnectionSuspended(int i) {
                        Toast.makeText(context, "onConnectionSuspended", Toast.LENGTH_LONG).show();
                    }
                })
                .addApi(Games.API).addScope(Games.SCOPE_GAMES)
                .build();
    }

    public static GoogleApiClient getGoogleApi(final @NonNull Context context) {
        return new GoogleApiClient.Builder(context)
                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(context, "onConnectionFailed " + connectionResult.getErrorCode(), Toast.LENGTH_LONG).show();
                    }
                })
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(@Nullable Bundle bundle) {
                        Toast.makeText(context, "onConnected", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onConnectionSuspended(int i) {
                        Toast.makeText(context, "onConnectionSuspended", Toast.LENGTH_LONG).show();
                    }
                })
                .addApi(Games.API).addScope(Games.SCOPE_GAMES)
                .build();
    }
}
