package com.greenkeycompany.librain.app;

import android.app.Application;
import android.support.annotation.NonNull;

import com.google.android.gms.ads.MobileAds;
import com.greenkeycompany.librain.R;
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
            return  "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAjgpMzqY6S8Fe5UyOdJNKsB7w5iQuBZojYDP5ZG7wKM9+97lUdqF3+RWQsFWL5tHkWQP1EBR8NlzhvnsvMXxwmrhebeeOFtaLavPwrVp0mlfajPuvCHwDi/8Gg8I8hv2t1r5zbHbCoeiy6CT629SPeCEyrXc5vtqloCXn2csCtFC/fr8Oujb49OeC6wsqcCzcvOs4m2lFyDCZa4rB69Q4oXtSnOVWdLxZ4nATwj3Y832H0Y7hVN5owcEtHyYFeXfhJkosngoRNi9O2U4ZhpsyKS/g7WRHR0mt0J+JtTiqkyW4GiFgRlmBAnnVsUa/0hmFlhl3yVmxMW8BrBn7KBV4EQIDAQAB";
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
        MobileAds.initialize(this, getString(R.string.application_admob_id));
        premiumUtil = new PremiumUtil(this);
    }
}
