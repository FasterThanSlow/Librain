package com.greenkey.librain;

import android.app.Application;
import android.support.annotation.NonNull;

import org.solovyev.android.checkout.Billing;

/**
 * Created by Alexander on 02.03.2017.
 */

public class MyApplication extends Application {

    private static MyApplication myApplication;

    private final Billing billing = new Billing(this, new Billing.DefaultConfiguration() {
        @Override @NonNull
        public String getPublicKey() {
            return "Your public key, don't forget about encryption";

        }
    });

    public MyApplication() {
        myApplication = this;
    }

    public static MyApplication get() {
        return myApplication;
    }

    public Billing getBilling() {
        return billing;
    }
}
