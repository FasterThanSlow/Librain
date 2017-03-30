package com.greenkey.librain.level;

import com.greenkey.librain.entity.ResourceType;
import com.greenkey.librain.entity.Rule;
import com.greenkey.librain.level.Level;

import java.util.Arrays;
import java.util.Random;

/**
 * Created by Alexander on 09.02.2017.
 */

public class Generator {

    private static Random random = new Random();

    public static ResourceType[] createRound1Items(Rule[] rules, int boardSize) {
        ResourceType[] result = new ResourceType[boardSize];
        Arrays.fill(result, ResourceType.NONE);

        int rulesCount = rules.length;
        for (int i = 0; i < rulesCount; i++) {
            int itemsCount = rules[i].getItemsCount();
            ResourceType itemsType = rules[i].getResourceType();

            int index;
            for (int j = 0; j < itemsCount; j++) {
                do {
                    index = random.nextInt(boardSize);
                } while (result[index] != ResourceType.NONE);

                result[index] = itemsType;
            }
        }
        return result;
    }

    /*
    public static ResourceType[][] createRound2Items(Rule[] rules, int boardSize) {
        int rulesCount = rules.length;
        int usedRulesCount = 0;

        ResourceType[] boardResources =  createRound1Items(rules, boardSize);

        for (ResourceType resourceType : boardResources) {
            if (resourceType != ResourceType.NONE) {

            }
        }
    }
    */


    public static Rule[] createRules(Level.LevelType levelType, int[] items) {
        int requiredTypesCount = items.length;
        final ResourceType[] uniqueResources = getUniqueResourceTypes(levelType.getResources(), requiredTypesCount);

        final Rule[] rules = new Rule[requiredTypesCount];
        for (int i = 0; i < requiredTypesCount; i++) {
            rules[i] = (new Rule(items[i], uniqueResources[i]));
        }

        return rules;
    }

    //Выборка X ресурсов из Y; 2 из 3 (Например)
    private static ResourceType[] getUniqueResourceTypes(ResourceType[] source, int itemsCount) {
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

    private static boolean containsInArray(int[] array, int value) {
        for (int item : array) {
            if (item == value) {
                return true;
            }
        }

        return false;
    }

}
