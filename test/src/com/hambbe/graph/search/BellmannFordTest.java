package com.hambbe.graph.search;

import com.hambbe.graph.DirectedGraph;
import com.hambbe.graph.Graph;
import com.hambbe.graph.Graphs;
import junit.framework.TestCase;

import java.util.*;

public class BellmannFordTest extends TestCase {

    class Test {
        int number;
        String name;

        public Test(int number, String name) {
            this.number = number;
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    @org.junit.Test
    public void testBellmanFord() throws Exception {
        DirectedGraph<Test, Integer> graph = new DirectedGraph<>(e -> e.doubleValue());

        Graph.Vertex H = graph.addVertex(new Test(12, "Hambbe"));
        Graph.Vertex I = graph.addVertex(new Test(786, "ist"));
        Graph.Vertex J = graph.addVertex(new Test(34, "Jaja"));
        Graph.Vertex D = graph.addVertex(new Test(56, "der"));
        Graph.Vertex E = graph.addVertex(new Test(34, "Wie bitte?"));
        Graph.Vertex W = graph.addVertex(new Test(45, "wahre"));
        Graph.Vertex K = graph.addVertex(new Test(42, "König"));

        graph.connect(H,I,1);
        graph.connect(I,D,5);
        graph.connect(D,W,3);
        graph.connect(W,K,-10);
        graph.connect(H,J,1);
        graph.connect(J,K,7);
        graph.connect(I,E,1);
        graph.connect(E,K,1);

        List<Graphs.Link> route = Graphs.bellmanFord(graph, H, K);

        assertNotNull(route);

        String result = graph.getValue(route.get(0).getFrom()).name + " " + route.stream().map(s -> graph.getValue(s.getTo()).name).reduce((s,a) -> s + " " + a).get();

        assertEquals("Hambbe ist der wahre König", result);

        graph.connect(K,D,-2);

        assertNull("Negative cycle not detected!", Graphs.bellmanFord(graph, H, K));

        graph.disconnect(H,I);

        assertNull("Non existing path found!", Graphs.bellmanFord(graph, H, I));

    }

    @org.junit.Test
    public void testBellmanFord1() throws Exception {
        DirectedGraph<Test, Integer> graph = new DirectedGraph<>(e -> e.doubleValue());

        Graph.Vertex H = graph.addVertex(new Test(12, "Hambbe"));
        Graph.Vertex I = graph.addVertex(new Test(786, "ist"));
        Graph.Vertex J = graph.addVertex(new Test(34, "Jaja"));
        Graph.Vertex D = graph.addVertex(new Test(56, "der"));
        Graph.Vertex E = graph.addVertex(new Test(34, "Wie bitte?"));
        Graph.Vertex W = graph.addVertex(new Test(45, "wahre"));
        Graph.Vertex K = graph.addVertex(new Test(42, "König"));

        graph.connect(H,I,1);
        graph.connect(I,D,5);
        graph.connect(D,W,3);
        graph.connect(W,K,-10);
        graph.connect(H,J,1);
        graph.connect(J,K,7);
        graph.connect(I,E,1);
        graph.connect(E,K,1);

        HashMap<Graph.Vertex, LinkedList<Graphs.Link>> routes = Graphs.bellmanFord(graph, H);

        assertNotNull(routes);

        HashMap<Graph.Vertex, Double> expectedTotalValue = new HashMap<>();
        expectedTotalValue.put(I, 1.0);
        expectedTotalValue.put(J, 1.0);
        expectedTotalValue.put(K, -1.0);
        expectedTotalValue.put(E, 2.0);
        expectedTotalValue.put(D, 6.0);
        expectedTotalValue.put(W, 9.0);

        Iterator<Map.Entry<Graph.Vertex, LinkedList<Graphs.Link>>> it = routes.entrySet().iterator();
        while (it.hasNext()) {
            HashMap.Entry<Graph.Vertex, LinkedList<Graphs.Link>> pair = it.next();  /*(HashMap.Entry)*/
            assertTrue(Math.abs(pair.getValue().getLast().getTotalCost() - expectedTotalValue.get(pair.getKey())) < 0.0000000001);
        }
    }

}