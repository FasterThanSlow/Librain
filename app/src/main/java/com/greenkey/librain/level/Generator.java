package com.greenkey.librain.level;

import com.greenkey.librain.entity.ResourceType;
import com.greenkey.librain.entity.Rule;
import com.greenkey.librain.level.Level;
import com.greenkey.librain.level.gameround.FirstGameRound;
import com.greenkey.librain.level.gameround.GameRound;
import com.greenkey.librain.level.gameround.SecondGameRound;

import java.util.Arrays;
import java.util.Random;

/**
 * Created by Alexander on 09.02.2017.
 */

public class Generator {

    private static Random random = new Random();

    private static ResourceType[] createItems(Rule[] rules, int boardSize) {
        ResourceType[] items = new ResourceType[boardSize];
        Arrays.fill(items, ResourceType.NONE);

        int rulesCount = rules.length;
        for (int i = 0; i < rulesCount; i++) {
            int itemsCount = rules[i].getItemsCount();
            ResourceType itemsType = rules[i].getResourceType();

            int index;
            for (int j = 0; j < itemsCount; j++) {
                do {
                    index = random.nextInt(boardSize);
                } while (items[index] != ResourceType.NONE);

                items[index] = itemsType;
            }
        }

        return items;
    }

    public static GameRound createRound1Items(Rule[] rules, int boardSize) {
        return new FirstGameRound(createItems(rules, boardSize));
    }

    public static GameRound createRound2Items(Rule[] rules, int boardSize) {
        ResourceType[] trueAnswer =  createItems(rules, boardSize);

        ResourceType[] firstPartItems =  Arrays.copyOf(trueAnswer, boardSize);

        ResourceType[] secondPartItems = new ResourceType[boardSize];
        Arrays.fill(secondPartItems, ResourceType.NONE);

        int itemsCount = 0;
        for (Rule rule : rules) {
            itemsCount += rule.getItemsCount();
        }

        int firstPartItemsCount =  itemsCount / 2;
        int secondPartItemsCount =  itemsCount - firstPartItemsCount;

        int usedItems = 0;
        while (usedItems < secondPartItemsCount) {
            for (int i = 0; i < boardSize; i++) {
                if (firstPartItems[i] != ResourceType.NONE) {
                    if (random.nextBoolean()) {
                        secondPartItems[i] = firstPartItems[i];

                        firstPartItems[i] = ResourceType.NONE;
                        usedItems++;
                    }

                    if (usedItems == secondPartItemsCount) {
                        break;
                    }
                }
            }
        }

        return new SecondGameRound(trueAnswer, firstPartItems, secondPartItems);
    }



    public static Rule[] createRules(Level.LevelType levelType, int[] items) {
        int requiredTypesCount = items.length;
        final ResourceType[] uniqueResources = getUniqueResourceTypes(levelType.getResources(), requiredTypesCount);

        final Rule[] rules = new Rule[requiredTypesCount];
        for (int i = 0; i < requiredTypesCount; i++) {
            rules[i] = (new Rule(items[i], uniqueResources[i]));
        }

        return rules;
    }

    //Выборка X ресурсов из Y; Например: 2 из 3
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
