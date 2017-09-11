package com.greenkeycompany.librain.app.util;

import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import java.lang.reflect.Method;

/**
 * Created by tert0 on 15.08.2017.
 */

public class ValueAnimatorEnablerUtil {

    //DOESN'T WORK WITH APP CONTEXT. WORK ONLY IN ACTIVITY
    public static void init(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1){
            float animatorDurationScale = Settings.Global.getFloat(context.getContentResolver(), Settings.Global.ANIMATOR_DURATION_SCALE, 0);

            if (animatorDurationScale != 1) {
                try {
                    Method targetMethod = ValueAnimator.class.getDeclaredMethod("setDurationScale", float.class);
                    targetMethod.setAccessible(true);
                    targetMethod.invoke(null, 1f);
                } catch (Exception e) {
                    Log.d("ValueAnimatorUtil", "setDurationScale failed: " + e.getMessage());
                }
            }
        }
    }
}
