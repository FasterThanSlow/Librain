package com.greenkeycompany.librain.app;

import android.app.Application;
import android.support.annotation.NonNull;

import com.greenkeycompany.librain.app.util.PremiumUtil;

import org.solovyev.android.checkout.Billing;

/**
 * Created by Alexander on 02.03.2017.
 */

public class App extends Application {

    private static App instance;
    public static App get() {
        return instance;
    }
    public App() {
        instance = this;
    }

    private final Billing billing = new Billing(this, new Billing.DefaultConfiguration() {
        @Override @NonNull
        public String getPublicKey() {
            return  "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAkNvqeGQ2iVsWLsldKjAh9U454IRNopgIFL2jpGGFPSUUc0h8RZuFnxHwaNjc3Rf9zhxVPOlYr4bZugT01x5XiyABPvFDJygC3Xujgp2bOYybVqyblMuu5Cs8ab6ARMrvDCptrl88pwU8yjZlQ5T4NDQKCx8kOS1oqH1bGZH9hOAJmBhF7eZBGPJEv8Cl6lci/XWA4ATAyiA0+Pyawg+a7eYD68sdjXanKn1mXjBQl0gtPAomY3NjjCXrE6PDXI92zieMfW+6kEJCOT9VEyOkqXQt0vqVZh0UhBFntPEbz7DFLyZ+/glG2VuzmFGBMASjiKhUNplR3Wpz/b6eo4tBOQIDAQAB";
        }
    });
    public Billing getBilling() {
        return billing;
    }

    private PremiumUtil premiumUtil;
    public PremiumUtil getPremiumUtil() {
        return premiumUtil;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        premiumUtil = new PremiumUtil(this);
    }
}
