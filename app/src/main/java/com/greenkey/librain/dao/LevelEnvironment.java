package com.greenkey.librain.dao;

import android.os.Parcel;
import android.os.Parcelable;

import com.greenkey.librain.entity.ResourceType;
import com.greenkey.librain.entity.Rule;

import java.util.Arrays;
import java.util.Random;

/**
 * Created by Alexander on 11.03.2017.
 */

public class LevelEnvironment implements Parcelable {

    public enum LevelEnvironmentType {

        SPACE(new ResourceType[] {ResourceType.MARS, ResourceType.EARTH}),
        FRUIT(new ResourceType[] {ResourceType.ORANGE, ResourceType.CHERRY, ResourceType.PUMPKIN, ResourceType.PEAS});

        private ResourceType[] resources;
        LevelEnvironmentType(ResourceType[] resources) {
            this.resources = resources;
        }

        public ResourceType[] getResources() {
            return resources;
        }
    }

    private final LevelEnvironmentType currentType;
    private final Rule[] rules;

    public LevelEnvironmentType getCurrentType() {
        return currentType;
    }

    public Rule[] getRules() {
        return rules;
    }

    public LevelEnvironment(LevelEnvironmentType type, int[] items) { //В каждой ячейки количество элементов типа
        this.currentType = type;                                           //[0] -> 5 => [0] -> 5 * ResourceType.EARTH
                                                                           //[1] -> 5 => [1] -> 5 * ResourceType.MARS
        ResourceType[] resourceTypes = type.getResources();

        int count =  items.length;
        if (count > resourceTypes.length) {
            throw new IllegalArgumentException("params count: " + count + " needs less than: " + resourceTypes.length);
        }

        this.rules = createRules(resourceTypes, items);
    }

    public LevelEnvironment(Parcel in) {
        this.currentType = LevelEnvironmentType.valueOf(in.readString());
        this.rules = in.createTypedArray(Rule.CREATOR);
    }

    private Rule[] createRules(ResourceType[] resourceTypes, int[] items) {
        int itemsTypesCount = items.length;
        final ResourceType[] uniqueResources = getUniqueResourceTypes(resourceTypes, itemsTypesCount);

        final Rule[] rules = new Rule[itemsTypesCount];
        for (int i = 0; i < itemsTypesCount; i++) {
            rules[i] = (new Rule(items[i], uniqueResources[i]));
        }

        return rules;
    }

    //Выборка X ресурсов из Y; 2 из 3 (Например)
    private ResourceType[] getUniqueResourceTypes(ResourceType[] source, int itemsCount) {
        Random random = new Random();

        int[] usedItemIndexes = new int[itemsCount];
        Arrays.fill(usedItemIndexes, -1);

        int index;
        int itemsTypesCount = source.length;

        for (int i = 0; i < itemsCount; i++) {
            do {
                index = random.nextInt(itemsTypesCount);
            } while (containsInArray(usedItemIndexes, index));

            usedItemIndexes[i] = index;
        }

        ResourceType[] result = new ResourceType[itemsCount];
        for (int i = 0; i < itemsCount; i++) {
            result[i] = source[usedItemIndexes[i]];
        }

        return result;
    }

    private boolean containsInArray(int[] array, int value) {
        for (int item : array) {
            if (item == value) {
                return true;
            }
        }

        return false;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(currentType.name());
        dest.writeTypedArray(rules, flags);
    }

    public static final Parcelable.Creator<LevelEnvironment> CREATOR = new Parcelable.Creator<LevelEnvironment>() {

        @Override
        public LevelEnvironment createFromParcel(Parcel source) {
            return new LevelEnvironment(source);
        }

        @Override
        public LevelEnvironment[] newArray(int size) {
            return new LevelEnvironment[size];
        }
    };
}
