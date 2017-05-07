package com.greenkey.librain;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

/**
 * Created by Alexander on 07.05.2017.
 */

public class PremiumDialog {

    private Context context;

    public PremiumDialog(Context context) {
        this.context = context;
    }

    private View.OnClickListener buyOnClickListener;
    public void setBuyOnClickListener(View.OnClickListener buyOnClickListener) {
        this.buyOnClickListener = buyOnClickListener;
    }

    private AlertDialog dialog;

    public void show() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        final View rateDialog = LayoutInflater.from(context).inflate(R.layout.premium_dialog, null);

        final ImageView buyImageView = (ImageView) rateDialog.findViewById(R.id.premium_dialog_buy_image_view);
        buyImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (buyOnClickListener != null) {
                    buyOnClickListener.onClick(v);
                }

                dialog.dismiss();
            }
        });

        final ImageView closeImageView = (ImageView) rateDialog.findViewById(R.id.premium_dialog_close_image_view);
        closeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        builder.setView(rateDialog);

        dialog = builder.create();
        dialog.show();
    }
}
