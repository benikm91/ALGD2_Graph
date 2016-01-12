package com.hambbe.graph;

import com.hambbe.graph.data.TestData;
import org.junit.Test;

import java.util.List;
import java.util.stream.StreamSupport;

import static org.junit.Assert.assertFalse;
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
        long count = StreamSupport.stream(graph.getVertexes().spliterator(), false)
                .filter(vertex -> graph.getValue(vertex).equals("Test")).count();
        assertTrue("Expected: 1, Actual: " + count, count == 1);
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
        Graph.Vertex a, c, d, e, f;
        a = c = d = e = f = null;
        for (Graph.Vertex vertex : graph.getVertexes()) {
            if (graph.getValue(vertex).equals("A")) a = vertex;
            if (graph.getValue(vertex).equals("C")) c = vertex;
            if (graph.getValue(vertex).equals("D")) d = vertex;
            if (graph.getValue(vertex).equals("E")) e = vertex;
            if (graph.getValue(vertex).equals("F")) f = vertex;
        }
        graph.connect(a, d, 1);
        graph.connect(a, f, 1);
        graph.connect(c, e, 1);

        List<Graph.Vertex> actualNeighbours = graph.neighbors(star);
        // check neighbour count.
        assertTrue(String.format("Wrong number of neighbours. Expected: %d, Actual: %s", neighbours.length, actualNeighbours.size()),
                   actualNeighbours.size() == neighbours.length);
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

    @Test
    public void testGetEdges() {
        IntGraph<String> graph = TestData.ABCintGraph(2);
        for(Graph.Edge e : graph.getEdges()) {
            System.out.println(graph.getEdgeValue(e));
        }
    }
}
