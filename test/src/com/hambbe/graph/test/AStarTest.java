package com.hambbe.graph.test;

import com.hambbe.graph.Graph;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class AStarTest {

    @Parameterized.Parameters
    public static TestData.AStarTestData[] data() {
        return new TestData.AStarTestData[] {
            TestData.getTestIntGraph(),
            TestData.getTestIntGraph()
        };
    }

    @Parameterized.Parameter
    public TestData.AStarTestData data;

    @Test
    public void testAStar() throws Exception {
        Graph.Path p = data.graph.aStar(data.from, data.to, data.heuristic);
        assertNotNull("Reachable value not found.", p);
    }

}
