package com.greenkeycompany.librain.level;

import com.greenkeycompany.librain.entity.ItemType;
import com.greenkeycompany.librain.entity.Rule;
import com.greenkeycompany.librain.level.gameround.FirstGameRound;
import com.greenkeycompany.librain.level.gameround.GameRound;
import com.greenkeycompany.librain.level.gameround.SecondGameRound;
import com.greenkeycompany.librain.level.gameround.ThirdGameRound;

import java.util.Arrays;
import java.util.Random;

/**
 * Created by Alexander on 09.02.2017.
 */

public class Generator {

    private static final Random random = new Random();
    private static final Level.LevelType[] levelTypes = Level.LevelType.values();

    public static ItemType[] createFullBoardItems(Rule[] rules, int boardSize) {
        ItemType[] items = new ItemType[boardSize];
        Arrays.fill(items, ItemType.NONE);

        int rulesCount = rules.length;
        for (int i = 0; i < rulesCount; i++) {
            int itemsCount = rules[i].getItemsCount();
            ItemType itemType = rules[i].getItemType();

            int index;
            for (int j = 0; j < itemsCount; j++) {
                do {
                    index = random.nextInt(boardSize);
                } while (items[index] != ItemType.NONE);

                items[index] = itemType;
            }
        }

        return items;
    }

    public static GameRound createRound1Items(Rule[] rules, int boardSize) {
        return new FirstGameRound(createFullBoardItems(rules, boardSize));
    }

    public static GameRound createRound2Items(Rule[] rules, int boardSize) {
        ItemType[] trueAnswer =  createFullBoardItems(rules, boardSize);

        ItemType[] firstPartItems =  Arrays.copyOf(trueAnswer, boardSize);

        ItemType[] secondPartItems = new ItemType[boardSize];
        Arrays.fill(secondPartItems, ItemType.NONE);

        int itemsCount = 0;
        for (Rule rule : rules) {
            itemsCount += rule.getItemsCount();
        }

        int firstPartItemsCount =  itemsCount / 2;
        int secondPartItemsCount =  itemsCount - firstPartItemsCount;

        int usedItems = 0;
        while (usedItems < secondPartItemsCount) {
            for (int i = 0; i < boardSize; i++) {
                if (firstPartItems[i] != ItemType.NONE) {
                    if (random.nextBoolean()) {
                        secondPartItems[i] = firstPartItems[i];

                        firstPartItems[i] = ItemType.NONE;
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

    public static GameRound createRound3Items(Rule[] rules, int boardSize) {
        ItemType[] firstPart = createFullBoardItems(rules, boardSize);
        ItemType[] secondPart = createFullBoardItems(rules, boardSize);

        if (random.nextBoolean()) {
            return new ThirdGameRound(firstPart, 1, firstPart, secondPart);
        } else {
            return new ThirdGameRound(secondPart, 2, firstPart, secondPart);
        }
    }

    public static RatingGameStage createRatingStage(int stageNumber) {
        int rowCount;
        int columnCount;
        int itemsCount = stageNumber + 2;
        int[] items;

        int showingTime = 1000;

        if(itemsCount < 7){
            rowCount = 3;
            columnCount = 3;
        }
        else if(itemsCount < 13){
            rowCount = 4;
            columnCount = 4;
        }
        else{
            rowCount = 5;
            columnCount = 5;
        }


        if(itemsCount < 7){
            items = new int[] {itemsCount};
        }
        else if(itemsCount < 15){
            int firstTypeCount;
            int secondTypeCount;

            if(itemsCount % 2 == 0){
                if (itemsCount > 9)
                    firstTypeCount = itemsCount / 2 - 3;
                else
                    firstTypeCount = itemsCount / 2 - 2;
            }
            else
            {
                if (itemsCount > 9)
                    firstTypeCount = itemsCount / 2 - 3 + 1;
                else
                    firstTypeCount = itemsCount / 2 - 2 + 1;
            }


            secondTypeCount = itemsCount / 2 - 2;
            items = new int[] {firstTypeCount, secondTypeCount};
        }
        else{
            int firstTypeCount;
            int secondTypeCount;
            int thirdTypeCount;

            switch (itemsCount % 3){
                case 2:
                    firstTypeCount = itemsCount / 3 - 3 + 1;
                    secondTypeCount = itemsCount / 3  - 3 + 1;
                    thirdTypeCount = itemsCount / 3 - 3;
                    break;
                case 1:
                    firstTypeCount = itemsCount / 3 - 3 + 1;
                    secondTypeCount = itemsCount / 3 - 3;
                    thirdTypeCount = itemsCount / 3 - 3;
                    break;
                default:
                    firstTypeCount = itemsCount / 3 - 3;
                    secondTypeCount = itemsCount / 3 - 3;
                    thirdTypeCount = itemsCount / 3 - 3;
                    break;
            }

            items = new int[] {firstTypeCount, secondTypeCount, thirdTypeCount};
        }

        Rule[] rules = createRules(levelTypes[random.nextInt(levelTypes.length)], items);

        return new RatingGameStage(showingTime, rowCount, columnCount, items, rules);
    }



    public static Rule[] createRules(Level.LevelType levelType, int[] items) {
        int requiredTypesCount = items.length;
        final ItemType[] uniqueResources = getUniqueResourceTypes(levelType.getResources(), requiredTypesCount);

        final Rule[] rules = new Rule[requiredTypesCount];
        for (int i = 0; i < requiredTypesCount; i++) {
            rules[i] = (new Rule(items[i], uniqueResources[i]));
        }

        return rules;
    }

    //Выборка X ресурсов из Y; Например: 2 из 3
    private static ItemType[] getUniqueResourceTypes(ItemType[] source, int itemsCount) {
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

        ItemType[] result = new ItemType[itemsCount];
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
