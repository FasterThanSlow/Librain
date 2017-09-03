package com.greenkeycompany.librain;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

/**
 * Created by Alexander on 06.03.2017.
 */

public class RateDialog {

    private static final String RATE_APP_DIALOG_SHARED_PREFERENCES = "rate_app_dialog";

    private static final String DONT_SHOW_AGAIN_KEY = "dont_show_again";
    private static final String DIALOG_LAUNCH_TIMES_KEY = "dialog_launch_times";;

    private final Context context;
    private final SharedPreferences sharedPreferences;

    private final int launchingCountToShowing;

    public RateDialog(Context context) {
        this.context = context;
        this.sharedPreferences = context.getSharedPreferences(RATE_APP_DIALOG_SHARED_PREFERENCES, 0);
        this.launchingCountToShowing = context.getResources().getInteger(R.integer.rate_app_dialog_launch_times);
    }

    public boolean isShouldShow() {
        if (sharedPreferences.getBoolean(DONT_SHOW_AGAIN_KEY, false)) {
            return false;
        }

        int dialogLaunchingCount = sharedPreferences.getInt(DIALOG_LAUNCH_TIMES_KEY, 0) + 1;
        if (dialogLaunchingCount < launchingCountToShowing) {
            sharedPreferences.edit().putInt(DIALOG_LAUNCH_TIMES_KEY, dialogLaunchingCount).apply();
            return false;
        } else {
            return true;
        }
    }

    private AlertDialog dialog;

    public void show() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        final View rateDialog = LayoutInflater.from(context).inflate(R.layout.rate_app_dialog, null);

        final Button rateNowButton = (Button) rateDialog.findViewById(R.id.rate_app_dialog_rate_now);
        rateNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + context.getPackageName())));
                sharedPreferences.edit().putBoolean(DONT_SHOW_AGAIN_KEY, true).apply();

                dialog.dismiss();
            }
        });

        final Button dontShowAgainButton = (Button) rateDialog.findViewById(R.id.rate_app_dialog_dont_show_again);
        dontShowAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPreferences.edit().putBoolean(DONT_SHOW_AGAIN_KEY, true).apply();

                dialog.dismiss();
            }
        });

        final Button remindMeLaterButton = (Button) rateDialog.findViewById(R.id.rate_app_dialog_remind_me_later);
        remindMeLaterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //sharedPreferences.edit().putInt(DIALOG_LAUNCH_TIMES_KEY, 0).apply();
                dialog.dismiss();
            }
        });

        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                sharedPreferences.edit().putInt(DIALOG_LAUNCH_TIMES_KEY, 0).apply();
            }
        });

        builder.setView(rateDialog);

        dialog = builder.create();
        dialog.show();
    }
}
