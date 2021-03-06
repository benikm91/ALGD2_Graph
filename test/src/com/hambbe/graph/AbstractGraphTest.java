package com.hambbe.graph;

import com.hambbe.graph.data.TestData;

import org.junit.Test;

import java.util.List;
import java.util.stream.StreamSupport;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test {@link UnweightedGraph} methods.
 * Because <tt>UnweightedGraph</tt> is abstract, simple implementation {@link IntGraph} will be used.
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
        Vertex test = graph.addVertex("Test");
        Vertex testA = graph.addVertex("TestA");
        assertFalse("Expected: Items are not adjacent, Actual: Items are adjacent.", graph.adjacent(test, testA));
        graph.connect(test, testA, 1);
        assertTrue("Expected: Items are adjacent, Actual: Items are not adjacent.", graph.adjacent(test, testA));
    }

    @Test
    public void testNeighbour() {
        String[] neighbours = { "A", "B", "C", "D", "E", "F" };
        Vertex[] neighboursVertexes = new Vertex[neighbours.length];
        IntGraph<String> graph = new IntGraph<>();
        Vertex star = graph.addVertex("*");
        int i = 0;
        for (String neighbour : neighbours) {
            Vertex neighbourVertex = graph.addVertex(neighbour);
            graph.connect(star, neighbourVertex, 1);
            neighboursVertexes[i++] = neighbourVertex;
        }

        // add some noise connections
        Vertex a, c, d, e, f;
        a = c = d = e = f = null;
        for (Vertex vertex : graph.getVertexes()) {
            if (graph.getValue(vertex).equals("A")) a = vertex;
            if (graph.getValue(vertex).equals("C")) c = vertex;
            if (graph.getValue(vertex).equals("D")) d = vertex;
            if (graph.getValue(vertex).equals("E")) e = vertex;
            if (graph.getValue(vertex).equals("F")) f = vertex;
        }
        graph.connect(a, d, 1);
        graph.connect(a, f, 1);
        graph.connect(c, e, 1);

        List<Vertex> actualNeighbours = graph.neighbors(star);
        // check neighbour count.
        assertTrue(String.format("Wrong number of neighbours. Expected: %d, Actual: %s", neighbours.length, actualNeighbours.size()),
                   actualNeighbours.size() == neighbours.length);
    }

    @Test
    public void testGetValue() {
        IntGraph<String> graph = new IntGraph<>();
        Vertex value = graph.addVertex("Value");
        Vertex notValue = graph.addVertex("Not value");
        assertFalse(graph.getValue(notValue).equals("Value"));
        assertTrue(graph.getValue(value).equals("Value"));
    }

    @Test
    public void testSetValue() {
        IntGraph<String> graph = new IntGraph<>();
        Vertex value = graph.addVertex("Not value");
        assertFalse(graph.getValue(value).equals("Value"));
        graph.setValue(value, "Value");
        assertTrue(graph.getValue(value).equals("Value"));
    }

    @Test
    public void testGetEdges() {
        IntGraph<String> graph = TestData.ABCintGraph(2);

        StringBuilder expectedSb = new StringBuilder();
        int expectedCount = 0;
        for(Edge e : graph.getEdges()) {
            expectedSb.append(e);
            expectedCount++;
        }

        StringBuilder sb = new StringBuilder();
        int count = 0;
        for (Vertex v : graph.getVertexes()) {
            for (Edge e : v.getEdges()) {
                sb.append(e);
                count++;
            }
        }

        assertEquals("Unexpected amount of edges", expectedCount, count);
        assertEquals("Unexpected order of edges", expectedSb.toString(), sb.toString());

        Iterable<? extends Edge> edges = graph.getEdges();

        int firstIteration = 0, secondIteration = 0;
        for (Edge e : edges) {
            firstIteration++;
        }
        for (Edge e : edges) {
            secondIteration++;
        }
        assertEquals("Iterating twice over the same List with different outcome!", firstIteration, secondIteration);
    }
}
