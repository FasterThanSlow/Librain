package com.greenkey.librain;

import com.greenkey.librain.view.boardview.BoardViewItemGenerator;
import com.greenkey.librain.entity.ResourceType;
import com.greenkey.librain.entity.Rule;

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
        int size = 10;

        Rule rule1 = new Rule(2, ResourceType.EU);
        Rule rule2 = new Rule(3, ResourceType.GB);

        ResourceType[] result = BoardViewItemGenerator.createResources(new Rule[] {rule1, rule2}, size);

        int countNoneType = 0;
        for (ResourceType resourceType : result) {
            if (resourceType == ResourceType.NONE) {
                countNoneType++;
            }
        }

        assertEquals(countNoneType, 5);
    }


}