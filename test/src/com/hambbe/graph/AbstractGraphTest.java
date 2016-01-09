package com.hambbe.graph;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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

    @Test
    public void testAdjacent() {
        IntGraph<String> graph = new IntGraph<>();
        Graph.Item test = graph.addVertex("Test");
        Graph.Item testA = graph.addVertex("TestA");
        assertFalse("Expected: Items are not adjacent, Actual: Items are adjacent.", graph.adjacent(test, testA));
        graph.connect(test, testA, 1);
        assertTrue("Expected: Items are adjacent, Actual: Items are not adjacent.", graph.adjacent(test, testA));
    }

    @Test
    public void testNeighbour() {
        String[] neighbours = { "A", "B", "C", "D", "E", "F" };
        Graph.Item[] neighboursItems = new Graph.Item[neighbours.length];
        IntGraph<String> graph = new IntGraph<>();
        Graph.Item star = graph.addVertex("*");
        int i = 0;
        for (String neighbour : neighbours) {
            Graph.Item neighbourItem = graph.addVertex(neighbour);
            graph.connect(star, neighbourItem, 1);
            neighboursItems[i++] = neighbourItem;
        }
        // add some noise connections
        graph.connect(graph.getItem("A"), graph.getItem("D"), 1);
        graph.connect(graph.getItem("A"), graph.getItem("F"), 1);
        graph.connect(graph.getItem("C"), graph.getItem("E"), 1);

        List<Graph.Item> actualNeighbours = graph.neighbors(star);
        // check neighbour count.
        assertTrue(String.format("Wrong number of neighbours. Expected: %d, Actual: %s", neighbours.length, actualNeighbours.size()),
                   actualNeighbours.size() == neighbours.length);
        // check actual neighbours.
        for (String neighbour : neighbours) {
            actualNeighbours.remove(graph.getItem(neighbour));
        }
        assertTrue(String.format("Wrong number of remaining neighbours. Expected: %d, Actual: %s", 0, actualNeighbours.size()),
                actualNeighbours.isEmpty());
    }

}
