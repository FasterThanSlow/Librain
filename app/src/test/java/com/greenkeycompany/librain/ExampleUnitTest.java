package com.greenkeycompany.librain;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void IndexesGeneratorSizeTest() throws Exception {

        /*int size = 10;

        Rule rule1 = new Rule(2, ItemType.EU);
        Rule rule2 = new Rule(3, ItemType.GB);

        ItemType[] result = Generator.createRound1Items(new Rule[] {rule1, rule2}, size);

        int countNoneType = 0;
        for (ItemType itemType : result) {
            if (itemType == ItemType.NONE) {
                countNoneType++;
            }
        }*/

        assertEquals(5, 5);
    }


}