package com.greenkey.librain;

import android.os.Parcelable;

/**
 * Created by Alexander on 09.02.2017.
 */

public enum ResourceType {
    NONE(0,0),

    GB(R.drawable.ic_united_kingdom, R.drawable.ic_united_kingdom_dis),
    EU(R.drawable.ic_european_union, R.drawable.ic_european_union_dis),
    EN(R.drawable.ic_england, R.drawable.ic_england_dis);

    private int enabledItemResourceId;
    private int disabledItemResourceId;

    ResourceType(int enabledItemResourceId, int disabledItemResourceId) {
        this.enabledItemResourceId = enabledItemResourceId;
        this.disabledItemResourceId = disabledItemResourceId;
    }

    public int getEnabledItemResourceId() {
        return enabledItemResourceId;
    }

    public int getDisabledItemResourceId() {
        return disabledItemResourceId;
    }
}
