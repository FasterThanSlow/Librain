package com.greenkeycompany.librain;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;


import org.solovyev.android.checkout.ActivityCheckout;
import org.solovyev.android.checkout.ProductTypes;
import org.solovyev.android.checkout.Purchase;
import org.solovyev.android.checkout.RequestListener;

import javax.annotation.Nonnull;

/**
 * Created by Alexander on 08.05.2017.
 */

public class PremiumHelper {

    public static final String PREMIUM_USER_SKU = "com.greenkeycompany.premium";

    private static boolean isPremiumUser;
    public static boolean isPremiumUser() {
        return isPremiumUser;
    }
    public static void setIsPremiumUser(boolean isPremiumUser) {
        PremiumHelper.isPremiumUser = isPremiumUser;
    }

    public static class PremiumDialogPurchaseListener implements RequestListener<Purchase> {
        @Override
        public void onSuccess(@Nonnull Purchase result) {
            PremiumHelper.setIsPremiumUser(true);
        }

        @Override
        public void onError(int response, @Nonnull Exception e) {

        }
    }

    public static class PremiumDialog {
        private final Context context;
        private final ActivityCheckout checkout;

        private PremiumDialogPurchaseListener purchaseListener;
        public void setPurchaseListener(PremiumDialogPurchaseListener purchaseListener) {
            this.purchaseListener = purchaseListener;
        }

        public PremiumDialog(Context context, ActivityCheckout startedCheckout) {
            this.context = context;
            this.checkout = startedCheckout;
        }

        private AlertDialog dialog;

        public void show() {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);

            final View rateDialog = LayoutInflater.from(context).inflate(R.layout.premium_dialog, null);

            final ImageView buyImageView = (ImageView) rateDialog.findViewById(R.id.premium_dialog_buy_image_view);
            buyImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (purchaseListener != null) {
                        checkout.startPurchaseFlow(ProductTypes.IN_APP, PREMIUM_USER_SKU, null, purchaseListener);
                    } else {
                        checkout.startPurchaseFlow(ProductTypes.IN_APP, PREMIUM_USER_SKU, null, new PremiumDialogPurchaseListener());
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
}
