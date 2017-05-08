package com.greenkey.librain;

import android.app.Application;
import android.support.annotation.NonNull;
import android.util.Base64;
import android.util.Log;

import com.greenkey.librain.mainmenu.MainMenuActivity;

import org.solovyev.android.checkout.Billing;
import org.solovyev.android.checkout.BillingRequests;
import org.solovyev.android.checkout.Checkout;
import org.solovyev.android.checkout.Inventory;
import org.solovyev.android.checkout.ProductTypes;

import javax.annotation.Nonnull;

/**
 * Created by Alexander on 02.03.2017.
 */

public class MyApplication extends Application {

    private final Billing billing = new Billing(this, new Billing.DefaultConfiguration() {
        @Override @NonNull
        public String getPublicKey() {
            return  "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAkNvqeGQ2iVsWLsldKjAh9U454IRNopgIFL2jpGGFPSUUc0h8RZuFnxHwaNjc3Rf9zhxVPOlYr4bZugT01x5XiyABPvFDJygC3Xujgp2bOYybVqyblMuu5Cs8ab6ARMrvDCptrl88pwU8yjZlQ5T4NDQKCx8kOS1oqH1bGZH9hOAJmBhF7eZBGPJEv8Cl6lci/XWA4ATAyiA0+Pyawg+a7eYD68sdjXanKn1mXjBQl0gtPAomY3NjjCXrE6PDXI92zieMfW+6kEJCOT9VEyOkqXQt0vqVZh0UhBFntPEbz7DFLyZ+/glG2VuzmFGBMASjiKhUNplR3Wpz/b6eo4tBOQIDAQAB";
        }
    });

    private static MyApplication instance;
    public MyApplication() {
        instance = this;
    }

    public static MyApplication getInstance() {
        return instance;
    }

    public Billing getBilling() {
        return billing;
    }

    //private Checkout checkout;

    @Override
    public void onCreate() {
        super.onCreate();

        /*
        checkout = Checkout.forApplication(billing);
        checkout.start();
        checkout.loadInventory(Inventory.Request.create().
                        loadAllPurchases(), new Inventory.Callback() {
            @Override
            public void onLoaded(@Nonnull Inventory.Products products) {
                final Inventory.Product product = products.get(ProductTypes.IN_APP);
                if ( ! product.supported) {
                    return;
                }

                boolean isPremiumUser = product.isPurchased(PremiumHelper.PREMIUM_USER_SKU);
                Log.d("BillingTest", "isPremium " + isPremiumUser);
                PremiumHelper.setIsPremiumUser(isPremiumUser);

                checkout.stop();
            }
        });
        */
    }
}
