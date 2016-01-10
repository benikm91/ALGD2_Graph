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
        Graph.Vertex test = graph.addVertex("Test");
        Graph.Vertex testA = graph.addVertex("TestA");
        assertFalse("Expected: Items are not adjacent, Actual: Items are adjacent.", graph.adjacent(test, testA));
        graph.connect(test, testA, 1);
        assertTrue("Expected: Items are adjacent, Actual: Items are not adjacent.", graph.adjacent(test, testA));
    }

    @Test
    public void testNeighbour() {
        String[] neighbours = { "A", "B", "C", "D", "E", "F" };
        Graph.Vertex[] neighboursVertexes = new Graph.Vertex[neighbours.length];
        IntGraph<String> graph = new IntGraph<>();
        Graph.Vertex star = graph.addVertex("*");
        int i = 0;
        for (String neighbour : neighbours) {
            Graph.Vertex neighbourVertex = graph.addVertex(neighbour);
            graph.connect(star, neighbourVertex, 1);
            neighboursVertexes[i++] = neighbourVertex;
        }
        // add some noise connections
        graph.connect(graph.getItem("A"), graph.getItem("D"), 1);
        graph.connect(graph.getItem("A"), graph.getItem("F"), 1);
        graph.connect(graph.getItem("C"), graph.getItem("E"), 1);

        List<Graph.Vertex> actualNeighbours = graph.neighbors(star);
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

    @Test
    public void testGetValue() {
        IntGraph<String> graph = new IntGraph<>();
        Graph.Vertex value = graph.addVertex("Value");
        Graph.Vertex notValue = graph.addVertex("Not value");
        assertFalse(graph.getValue(notValue).equals("Value"));
        assertTrue(graph.getValue(value).equals("Value"));
    }

    @Test
    public void testSetValue() {
        IntGraph<String> graph = new IntGraph<>();
        Graph.Vertex value = graph.addVertex("Not value");
        assertFalse(graph.getValue(value).equals("Value"));
        graph.setValue(value, "Value");
        assertTrue(graph.getValue(value).equals("Value"));
    }

}
