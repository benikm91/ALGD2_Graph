package com.hambbe.graph;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * Test {@link AbstractGraph} methods.
 * Because <tt>AbstractGraph</tt> is abstract, simple implementation {@link IntGraph} will be used.
 *
 */
public class AbstractGraphTest {

    @Test
    public void testAddVertex() {
        IntGraph<String> graph = new IntGraph<>();
        graph.addVertex("Test");
        assertNotNull(graph.getItem("Test"));
    }

}
