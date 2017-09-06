package com.greenkeycompany.librain.app.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Alexander on 08.05.2017.
 */

public class PremiumUtil {

    public static final String PREMIUM_USER_SKU = "com.greenkeycompany.premium";

    private boolean premiumUser;
    public boolean isPremiumUser() {
        return premiumUser;
    }
    public void setPremiumUser(boolean premiumUser) {
        this.premiumUser = premiumUser;
        this.sharedPreferences.edit().putBoolean(PREMIUM_USER_KEY, premiumUser).apply();
    }

    private static final String PREMIUM_USER_KEY = "premium_user";

    private SharedPreferences sharedPreferences;
    public PremiumUtil(Context context) {
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        this.premiumUser = sharedPreferences.getBoolean(PREMIUM_USER_KEY, false);
    }
}
