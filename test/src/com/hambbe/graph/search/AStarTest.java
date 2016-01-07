package com.hambbe.graph.search;

import com.hambbe.graph.Graph;
import com.hambbe.graph.data.TestData;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class AStarTest {

    @Parameterized.Parameters
    public static TestData.AStarTestData[] data() {
        return new TestData.AStarTestData[] {
            TestData.getTestIntGraph()
        };
    }

    @Parameterized.Parameter
    public TestData.AStarTestData data;

    @Test
    public void testAStar() throws Exception {
        List<Graph.Step> r = data.graph.aStar(data.from, data.to, data.heuristic);
        assertNotNull("Reachable value not found.", r);
        assertTrue("Number of steps size wrong.", r.size() > 0);
    }

}
