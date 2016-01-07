package com.hambbe.graph;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test {@link Graph} methods.
 * Because <tt>Graph</tt> is abstract, simple implementation {@link IntGraph} will be used.
 *
 */
public class GraphTest {

    @Test
    public void testAddVertex() {
        IntGraph<String> graph = new IntGraph<>();
        graph.addVertex("Test");
        assertNotNull(graph.getItem("Test"));
    }

}
