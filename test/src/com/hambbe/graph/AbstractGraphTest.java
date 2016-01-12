package com.hambbe.graph;

import com.hambbe.graph.data.TestData;
import org.junit.Test;

import java.util.List;
import java.util.stream.StreamSupport;

import static org.junit.Assert.*;

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
        /*StringBuilder sb1 = new StringBuilder();
        int count1 = 0;
        for(Graph.Edge e : graph.getEdges()) {
            sb1.append(e);
            count1++;
        }
        char[] chars1 = sb1.toString().toCharArray();
        int hash1 = 0;
        for (int i = 0; i < chars1.length; i++) {
            hash1 = hash1 ^ ((int)chars1[i]*i);
        }*/
        //assertEquals("Unexpected amount of edges", count1, count2);
        //assertEquals("Unexpected order of edges", hash1, hash2 + 1);

        StringBuilder sb = new StringBuilder();
        int count = 0, hash = 0;

        for (Graph.Vertex v : graph.getVertexes()) {
            for (Graph.Edge e : v.getEdges()) {
                sb.append(e);
                count++;
            }
        }

        char[] chars2 = sb.toString().toCharArray();
        for (int i = 0; i < chars2.length; i++) {
            hash = hash ^ ((int)chars2[i]*i);
        }

        assertEquals("Unexpected amount of edges", 676, count);
        assertEquals("Unexpected order of edges", 3176526, hash);

        Iterable<? extends Graph.Edge> edges = graph.getEdges();

        int firstIteration = 0, secondIteration = 0;
        for (Graph.Edge e : edges) {
            firstIteration++;
        }
        for (Graph.Edge e : edges) {
            secondIteration++;
        }
        assertEquals("Iterating twice over the same List with different outcome!", firstIteration, secondIteration);
    }
}
